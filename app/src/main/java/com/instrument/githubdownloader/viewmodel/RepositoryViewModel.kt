package com.instrument.githubdownloader.viewmodel

import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instrument.githubdownloader.MyApp
import com.instrument.githubdownloader.R
import com.instrument.githubdownloader.model.Branch
import com.instrument.githubdownloader.model.Load
import com.instrument.githubdownloader.repository.NetworkRepository
import com.instrument.githubdownloader.repository.RoomRepository
import com.instrument.githubdownloader.repository.UserScreenRepository
import com.instrument.githubdownloader.util.MyUtils
import com.instrument.githubdownloader.util.ProgressResponseBody
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import javax.inject.Inject

sealed class QueryBranch{
    data object LoadStart: QueryBranch()
    data object onNotFoundResult: QueryBranch()
    data class onError(val message: String): QueryBranch()
    data class onResult(val listRepositories: List<Branch>): QueryBranch()
    data object LoadStop: QueryBranch()
}

sealed class Download{
    data object START: Download()
    data class onProgress(val progress: String): Download()
    data class onError(val message: String): Download()
    data object onComplete: Download()
    data object END: Download()
}

class RepositoryViewModel
@Inject
constructor(
    private val networkRepository: NetworkRepository,
    private val userScreenRepository: UserScreenRepository,
    private val roomRepository: RoomRepository
) : ViewModel(){

    private var _searchBranchQuery = MutableStateFlow<Pair<String, String>?>(null)
    val searchBranchQuery: StateFlow<QueryBranch?> = _searchBranchQuery
        .filterNotNull()
        .flatMapLatest { (user, repository) ->
            flow {
                emit(QueryBranch.LoadStart)
                try {
                    val result = networkRepository.getBranches(user, repository)
                    if(result.code==404){
                        emit(QueryBranch.onNotFoundResult)
                    }else if(result.code!=200){
                        throw Exception()
                    }else{
                        val branches = result.body ?: listOf()
                        if(branches.isEmpty()){
                            emit(QueryBranch.onNotFoundResult)
                        }else{
                            emit(QueryBranch.onResult(branches))
                        }
                    }
                }catch (e: Exception){
                    emit(QueryBranch.onError("Возникла ошибка при выполнени запроса"))
                }
                emit(QueryBranch.LoadStop)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun runQueryBranches(user: String, repository: String){
        viewModelScope.launch {
            _searchBranchQuery.value = Pair(user, repository)
        }
    }

    fun openBranchLink(user: String, repository: String, branch: String){
        val webLink = "https://github.com/$user/$repository/tree/$branch"
        userScreenRepository.openBranchLink(webLink)
    }

    private var _downloadFile = MutableStateFlow<Triple<String, String, String>?>(null)
    val downloadFile: StateFlow<Download?> = _downloadFile
        .filterNotNull()
        .flatMapConcat { (user, repository, branch)->
            channelFlow {
                send(Download.START)
                val progressListener = object : ProgressResponseBody.DownloadProgressListener {
                    override fun onProgressUpdate(totalBytesRead: Long) {
                        trySend(
                            Download.onProgress(
                                MyUtils.getMemorySize(totalBytesRead)
                            )
                        )
                    }
                }
                try {
                    val result = networkRepository.downloadRepository(progressListener, user, repository, branch)
                    if(result.isSuccessful){
                        val contentDisposition = result.headers?.get("Content-Disposition") ?:""
                        var fileName = contentDisposition.let { extractFileName(it) } ?: "default_filename"

                        val pathFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        val projectDir = File(pathFile, MyApp.globalContext.getString(R.string.app_name))
                        if(!projectDir.exists()){
                            projectDir.mkdirs()
                        }
                        var destination = "$projectDir/$fileName"
                        var file = File(destination)

                        val inputStream = result.body?.byteStream()

                        var outputStream: FileOutputStream? = null

                        while (true) {
                            try {
                                outputStream = FileOutputStream(file)
                                break
                            } catch (e: FileNotFoundException) {
                                fileName = MyUtils.assignOrIncrementIndex(fileName)
                                file = File(projectDir, fileName)
                                while (file.exists()){
                                    fileName = MyUtils.assignOrIncrementIndex(fileName)
                                    file = File(projectDir, fileName)
                                }
                            }
                        }

                        inputStream?.use {input ->
                            outputStream.use { output->
                                input.copyTo(output!!)
                            }
                        }

                        val load = Load().apply {
                            this.fileName = fileName
                            this.userName = user
                            this.repository = repository
                            size = file.length()
                        }
                        roomRepository.addLoad(load)

                        send(Download.onComplete)
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    send(Download.onError("Возникла ошибка при выполнени загрузки"))
                }
                send(Download.END)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun downloadRepository(user: String, repository: String, branch: String){
        viewModelScope.launch {
            _downloadFile.value = Triple(user, repository, branch)
        }
    }

    private fun extractFileName(contentDisposition: String): String {
        val regex = "filename[^;=\\n]*=(['\"]?)([^'\";\\n]*)".toRegex()
        val match = regex.find(contentDisposition)
        return match?.groupValues?.get(2) ?: "unknown_filename"
    }
}
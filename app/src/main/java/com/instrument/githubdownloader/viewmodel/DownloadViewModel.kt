package com.instrument.githubdownloader.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instrument.githubdownloader.model.Load
import com.instrument.githubdownloader.repository.RoomRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DownloadViewModel @Inject constructor(private val roomRepository: RoomRepository): ViewModel() {

//    private val _downloadFiles = MutableLiveData<List<Load>>()
//    val downloadFiles: LiveData<List<Load>> get() = _downloadFiles

    fun getDownloadFiles(): LiveData<List<Load>>{
        return roomRepository.getDownloads()
    }
}
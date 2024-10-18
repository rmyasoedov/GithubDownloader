package com.instrument.githubdownloader.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instrument.githubdownloader.model.Repository
import com.instrument.githubdownloader.repository.NetworkRepository
import com.instrument.githubdownloader.repository.UserScreenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class QueryUser{
    data object LoadStart: QueryUser()
    data object onNotFoundResult: QueryUser()
    data class onError(val message: String): QueryUser()
    data class onResult(val listRepositories: List<Repository>): QueryUser()
    data object LoadStop: QueryUser()
}

class SearchViewModel
@Inject
constructor(
    private val networkRepository: NetworkRepository,
    private val userScreenRepository: UserScreenRepository
) : ViewModel() {

    private val _searchUserQuery = MutableStateFlow<String?>(null)

    val searchUserQuery: StateFlow<QueryUser?> = _searchUserQuery
        .debounce(300)
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { user ->
            flow {
                emit(QueryUser.LoadStart)
                try {
                    val result = networkRepository.findRepositoryUser(user)

                    if(result.code==404){
                        emit(QueryUser.onNotFoundResult)
                    }else if(result.code!=200){
                        throw Exception()
                    }else{
                        val repositories = result.body ?: listOf()
                        if(repositories.isEmpty()){
                            emit(QueryUser.onNotFoundResult)
                        }else{
                            emit(QueryUser.onResult(repositories))
                        }
                    }
                }catch (e: Exception){
                    emit(QueryUser.onError("Возникла ошибка при выполнени запроса"))
                }
                emit(QueryUser.LoadStop)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun onSearchUserQuery(user: String){
        viewModelScope.launch {
            _searchUserQuery.value = user
        }
    }

    fun openScreenRepository(repository: String){
        userScreenRepository.openRepositoryFragment(
            _searchUserQuery.value.toString(), repository
        )
    }
}
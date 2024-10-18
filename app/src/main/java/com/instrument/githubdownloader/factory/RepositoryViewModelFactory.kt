package com.instrument.githubdownloader.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.instrument.githubdownloader.viewmodel.RepositoryViewModel
import javax.inject.Inject
import javax.inject.Provider

class RepositoryViewModelFactory @Inject constructor(
    private val viewModelProvider: Provider<RepositoryViewModel>
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RepositoryViewModel::class.java)){
            return viewModelProvider.get() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
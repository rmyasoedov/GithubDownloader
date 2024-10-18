package com.instrument.githubdownloader.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.instrument.githubdownloader.viewmodel.DownloadViewModel
import javax.inject.Inject
import javax.inject.Provider

class DownloadViewModelFactory @Inject constructor(
    private val viewModelProvider: Provider<DownloadViewModel>
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DownloadViewModel::class.java)){
            return viewModelProvider.get() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
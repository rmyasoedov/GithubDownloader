package com.instrument.githubdownloader.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.instrument.githubdownloader.viewmodel.SearchViewModel
import javax.inject.Inject
import javax.inject.Provider

class SearchViewModelFactory @Inject constructor(
    private val viewModelProvider: Provider<SearchViewModel>
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SearchViewModel::class.java)){
            return viewModelProvider.get() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
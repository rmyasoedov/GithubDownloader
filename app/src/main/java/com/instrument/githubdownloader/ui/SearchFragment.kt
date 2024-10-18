package com.instrument.githubdownloader.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.instrument.githubdownloader.databinding.FragmentSearchBinding
import com.instrument.githubdownloader.factory.SearchViewModelFactory
import com.instrument.githubdownloader.model.Repository
import com.instrument.githubdownloader.repository.UserScreenRepository
import com.instrument.githubdownloader.ui.adapter.AdapterRepositories
import com.instrument.githubdownloader.viewmodel.QueryUser
import com.instrument.githubdownloader.viewmodel.SearchViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


class SearchFragment : BaseFragment() {

    private var _binding: FragmentSearchBinding?=null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: SearchViewModelFactory
    private val searchViewModel: SearchViewModel by viewModels { viewModelFactory }

    private lateinit var adapterRepositories: AdapterRepositories
    private var rootView: View? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        subComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView?.let {
            _binding = FragmentSearchBinding.bind(it)
            return it
        }
        _binding = FragmentSearchBinding.inflate(inflater, container,false)

        updateUI()

        rootView = binding.root
        return rootView
    }

    private fun updateUI(){

        val textWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val inputUser = binding.etUser.text.toString()
                if(inputUser.isEmpty()){
                    binding.progressBar.isVisible = false
                    resetVisibilityView()
                    return
                }

                searchViewModel.onSearchUserQuery(inputUser)
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.etUser.addTextChangedListener(textWatcher)

        lifecycleScope.launch {
            searchViewModel.searchUserQuery.collect{ searchUserQuery->
                when(searchUserQuery){
                    QueryUser.LoadStart -> {
                        binding.progressBar.isVisible = true
                    }
                    QueryUser.LoadStop -> {
                        binding.progressBar.isVisible = false
                    }
                    is QueryUser.onError -> {
                        Toast
                            .makeText(requireContext(), searchUserQuery.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    QueryUser.onNotFoundResult -> {
                        binding.clResultBlock.isVisible = false
                        binding.tvNullResult.isVisible = true
                    }
                    is QueryUser.onResult -> {
                        onResult(searchUserQuery.listRepositories)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun resetVisibilityView(){
        binding.clResultBlock.isVisible = false
        binding.tvNullResult.isVisible = false
    }

    private fun onResult(listRepository: List<Repository>){
        resetVisibilityView()
        if(binding.etUser.text.toString().isEmpty()) return
        binding.clResultBlock.isVisible = true
        adapterRepositories = AdapterRepositories(listRepository, object : AdapterRepositories.IEvent{
            override fun onClick(repository: String) {
                searchViewModel.openScreenRepository(repository)
            }
        })
        binding.rvRepositories.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRepositories.adapter = adapterRepositories
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
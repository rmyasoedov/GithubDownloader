package com.instrument.githubdownloader.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.instrument.githubdownloader.databinding.FragmentRepositoryBinding
import com.instrument.githubdownloader.databinding.FragmentSearchBinding
import com.instrument.githubdownloader.factory.RepositoryViewModelFactory
import com.instrument.githubdownloader.model.Branch
import com.instrument.githubdownloader.ui.adapter.AdapterBranch
import com.instrument.githubdownloader.util.KeyString
import com.instrument.githubdownloader.util.Permission
import com.instrument.githubdownloader.viewmodel.Download
import com.instrument.githubdownloader.viewmodel.QueryBranch
import com.instrument.githubdownloader.viewmodel.RepositoryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class RepositoryFragment : BaseFragment() {

    private var _binding: FragmentRepositoryBinding?=null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: RepositoryViewModelFactory
    private val repositoryViewModel: RepositoryViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        subComponent.inject(this)
    }

    private var user: String = ""
    private var repository: String = ""
    private var branch: String = ""
    private var rootView: View? = null

    private lateinit var adapterBranch: AdapterBranch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = arguments?.getString(KeyString.USER) ?: ""
        repository = arguments?.getString(KeyString.REPOSITORY) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView?.let {
            _binding = FragmentRepositoryBinding.bind(it)
            return it
        }
        _binding = FragmentRepositoryBinding.inflate(inflater, container,false)

        updateUI()
        rootView = binding.root
        return rootView
    }

    private fun updateUI(){
        binding.tvNameRepository.text = repository


        lifecycleScope.launch {
            repositoryViewModel.downloadFile.collect{download->
                when(download){
                    Download.END -> {
                        onEndLoad()
                    }
                    Download.START -> {
                        onStartLoad()
                    }
                    Download.onComplete -> {
                        onCompleteLoafFile()
                    }
                    is Download.onError -> {
                        onErrorMessage(download.message)
                        onEndLoad()
                    }
                    is Download.onProgress -> {
                        onProgress(download.progress)
                    }
                    else -> {}
                }
            }
        }

        lifecycleScope.launch {

            repositoryViewModel.searchBranchQuery.collect{ searchBranchQuery->
                when(searchBranchQuery){
                    QueryBranch.LoadStart -> {
                        binding.progressBar.isVisible = true
                    }
                    QueryBranch.LoadStop -> {
                        binding.progressBar.isVisible = false
                    }
                    is QueryBranch.onError -> {
                        onErrorMessage(searchBranchQuery.message)
                    }
                    QueryBranch.onNotFoundResult -> {

                    }
                    is QueryBranch.onResult -> {
                        onResult(searchBranchQuery.listRepositories)
                    }
                    else -> {}
                }
            }
        }

        repositoryViewModel.runQueryBranches(user, repository)
    }

    private fun onStartLoad(){
        binding.tvProgress.setText("")
        binding.clProgressBlock.isVisible = true
    }

    private fun onEndLoad(){
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            binding.clProgressBlock.isVisible = false
        }
    }

    private fun onProgress(progress: String){
        binding.tvProgress.text = progress
    }

    private fun onCompleteLoafFile(){
        binding.labelLoad.text = "Загрузка завершена: "
    }

    private fun onErrorMessage(message: String){
        Toast
            .makeText(requireContext(), message, Toast.LENGTH_LONG)
            .show()
    }

    private fun onResult(branches: List<Branch>){
        adapterBranch = AdapterBranch(branches, object : AdapterBranch.IEvent{
            override fun onSite(branch: Branch) {
                repositoryViewModel.openBranchLink(user, repository, branch.name?:"")
            }

            override fun onDownload(branch: Branch) {
                this@RepositoryFragment.branch = branch.name?:""
                checkStoragePermissions()
            }
        })

        binding.rvBranch.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBranch.adapter = adapterBranch
    }

    fun checkStoragePermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !Permission.checkRequestPermission(requireContext(), android.Manifest.permission.READ_MEDIA_IMAGES)){
            requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
        }else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU &&
            !Permission.checkRequestPermission(requireContext(), "android.permission.WRITE_EXTERNAL_STORAGE")){
            requestPermissionLauncher.launch("android.permission.WRITE_EXTERNAL_STORAGE")
        }else{
            repositoryViewModel.downloadRepository(user, repository, branch)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted->
            if(isGranted){
                repositoryViewModel.downloadRepository(user, repository, branch)
            }else{
                Toast
                    .makeText(requireContext(), "Предоставьте доступ к хранилищу", Toast.LENGTH_LONG)
                    .show()
            }
        }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}
package com.instrument.githubdownloader.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.instrument.githubdownloader.R
import com.instrument.githubdownloader.databinding.FragmentDownloadBinding
import com.instrument.githubdownloader.databinding.FragmentRepositoryBinding
import com.instrument.githubdownloader.factory.DownloadViewModelFactory
import com.instrument.githubdownloader.factory.RepositoryViewModelFactory
import com.instrument.githubdownloader.ui.adapter.AdapterDownload
import com.instrument.githubdownloader.viewmodel.DownloadViewModel
import com.instrument.githubdownloader.viewmodel.RepositoryViewModel
import javax.inject.Inject

class DownloadFragment : BaseFragment() {

    private var _binding: FragmentDownloadBinding?=null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: DownloadViewModelFactory
    private val downloadViewModel: DownloadViewModel by viewModels { viewModelFactory }

    private lateinit var adapterDownload: AdapterDownload

    override fun onAttach(context: Context) {
        super.onAttach(context)
        subComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDownloadBinding.inflate(inflater, container,false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI(){

        downloadViewModel.getDownloadFiles().observe(viewLifecycleOwner){listDownloads->
            binding.tvQuantityFiles.text = "Всего: ${listDownloads.size}"

            adapterDownload = AdapterDownload(listDownloads)
            binding.rvDownloads.layoutManager = LinearLayoutManager(requireContext())
            binding.rvDownloads.adapter = adapterDownload
        }

        //downloadViewModel.getDownloadFiles()

    }

}
package com.instrument.githubdownloader.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.instrument.githubdownloader.R
import com.instrument.githubdownloader.databinding.ItemDownloadBinding
import com.instrument.githubdownloader.model.Load
import com.instrument.githubdownloader.util.MyUtils

class AdapterDownload(val listDownload: List<Load>):
RecyclerView.Adapter<AdapterDownload.DownloadViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadViewHolder {
        val binding =
            ItemDownloadBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return DownloadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DownloadViewHolder, position: Int) {
        holder.binding.apply {

            if(MyUtils.isExistFile(listDownload[position].fileName)){
                ivIssue.setImageResource(R.drawable.ic_issue_file)
            }else{
                ivIssue.setImageResource(R.drawable.ic_not_issue_fiel)
            }

            tvRepository.text = "${listDownload[position].userName} / ${listDownload[position].repository}"

            tvFileName.text = listDownload[position].fileName
            tvSize.text = MyUtils.getMemorySize(listDownload[position].size)
            tvDateTime.text = MyUtils.formatUnixTime(listDownload[position].timestamp)
        }
    }

    override fun getItemCount() = listDownload.size

    class DownloadViewHolder(val binding: ItemDownloadBinding): RecyclerView.ViewHolder(binding.root)
}
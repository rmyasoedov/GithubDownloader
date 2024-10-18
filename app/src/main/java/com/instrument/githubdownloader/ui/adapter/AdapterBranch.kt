package com.instrument.githubdownloader.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.instrument.githubdownloader.databinding.ItemBranchBinding
import com.instrument.githubdownloader.model.Branch

class AdapterBranch(val listBranch: List<Branch>, val listener: IEvent) :
RecyclerView.Adapter<AdapterBranch.BranchViewHolder>(){

    interface IEvent{
        fun onSite(branch: Branch)
        fun onDownload(branch: Branch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BranchViewHolder {
        val binding =
            ItemBranchBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return BranchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BranchViewHolder, position: Int) {
        holder.binding.apply {
            tvNameBranch.text = listBranch[position].name

            ivSite.setOnClickListener {
                listener.onSite(listBranch[position])
            }

            ivDownload.setOnClickListener {
                listener.onDownload(listBranch[position])
            }
        }
    }

    override fun getItemCount() = listBranch.size

    class BranchViewHolder(val binding: ItemBranchBinding): RecyclerView.ViewHolder(binding.root)
}
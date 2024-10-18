package com.instrument.githubdownloader.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.instrument.githubdownloader.databinding.ItemRepositoryBinding
import com.instrument.githubdownloader.model.Repository

class AdapterRepositories(val listRepositories: List<Repository>, val listener: IEvent):
    RecyclerView.Adapter<AdapterRepositories.RepositoryViewHolder>(){

    interface IEvent{
        fun onClick(repository: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val binding =
            ItemRepositoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RepositoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.binding.apply {
            tvNameRepository.text = listRepositories[position].name ?: ""

            root.setOnClickListener {
                listener.onClick(listRepositories[position].name ?: "")
            }
        }
    }

    override fun getItemCount() = listRepositories.size

    class RepositoryViewHolder(val binding: ItemRepositoryBinding): RecyclerView.ViewHolder(binding.root)
}
package com.wikkot.drzewkav1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.wikkot.drzewkav1.databinding.NameBinding

class NameListAdapter(private val names: List<String>) : RecyclerView.Adapter<NameListAdapter.NameVh>() {

    inner class NameVh(binding: NameBinding): ViewHolder(binding.root){
        val name = binding.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameVh {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NameBinding.inflate(inflater, parent, false)
        return NameVh(binding)
    }

    override fun getItemCount(): Int {
        return names.size
    }

    override fun onBindViewHolder(holder: NameVh, position: Int) {
        holder.name.text = names[position]
    }
}
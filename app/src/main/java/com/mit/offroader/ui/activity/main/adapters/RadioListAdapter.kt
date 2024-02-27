package com.mit.offroader.ui.activity.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mit.offroader.databinding.ItemRadioChannelListBinding

class RadioListAdapter(private val list: ArrayList<String>) : RecyclerView.Adapter<RadioListAdapter.Holder>() {

    inner class Holder(private val binding: ItemRadioChannelListBinding) : ViewHolder(binding.root) {
        fun bind() {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRadioChannelListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        list[position].run {
            holder.bind()
        }
    }
}
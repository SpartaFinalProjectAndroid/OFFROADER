package com.mit.offroader.ui.activity.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mit.offroader.databinding.ItemRadioChannelListBinding
import com.mit.offroader.ui.activity.main.MainViewModel

class RadioListAdapter(private val list: MutableList<String>) : RecyclerView.Adapter<RadioListAdapter.Holder>() {

    interface ItemClick {
        fun onClick(key: String)
    }

    var itemClick : ItemClick ?= null

    inner class Holder(private val binding: ItemRadioChannelListBinding) : ViewHolder(binding.root) {
        fun bind(pos : Int) {
            binding.tvChannelTitle.text = list[pos]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRadioChannelListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        list[position].run {
            holder.bind(position)
            holder.itemView.setOnClickListener {
                itemClick?.onClick(this)
            }
        }
    }
}
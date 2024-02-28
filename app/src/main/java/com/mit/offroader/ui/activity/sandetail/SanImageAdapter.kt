package com.mit.offroader.ui.activity.sandetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mit.offroader.databinding.ItemAdapterMountainBinding

class SanImageAdapter: RecyclerView.Adapter<SanImageAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemAdapterMountainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    inner class Holder(val binding: ItemAdapterMountainBinding): RecyclerView.ViewHolder(binding.root){}
}
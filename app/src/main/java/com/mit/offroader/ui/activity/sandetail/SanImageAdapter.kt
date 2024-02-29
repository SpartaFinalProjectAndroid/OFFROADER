package com.mit.offroader.ui.activity.sandetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mit.offroader.databinding.ItemAdapterMountainBinding

class SanImageAdapter(var mItems: List<SanDetailImageData>): RecyclerView.Adapter<SanImageAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemAdapterMountainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val realPosition = position % 5

        holder.mountainImage.setImageResource(mItems[realPosition].img)
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    inner class Holder(val binding: ItemAdapterMountainBinding): RecyclerView.ViewHolder(binding.root){
        var mountainImage = binding.ivMountain
    }
}
package com.mit.offroader.ui.activity.sandetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.mit.offroader.databinding.ItemAdapterMountainBinding

class SanImageAdapter(
    var mItems: MutableList<SanDetailImageData>,
    private val viewPager2: ViewPager2
) : RecyclerView.Adapter<SanImageAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemAdapterMountainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.mountainImage.setImageResource(mItems[position].img)

        if (position == mItems.size - 1) {
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    inner class Holder(val binding: ItemAdapterMountainBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var mountainImage = binding.ivMountain
    }

    private val runnable = Runnable {
        mItems.addAll(mItems)
        notifyDataSetChanged()
    }
}
package com.ing.offroader.ui.fragment.mydetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ing.offroader.databinding.ItemSanListBinding
import com.ing.offroader.ui.activity.sandetail.MyLikedSan

class MyBookmarkAdapter(val mItems: MutableList<MyLikedSan>) : RecyclerView.Adapter<MyBookmarkAdapter.Holder>() {

    // 산 클릭 시 기능 추가
    interface SanClick {
        fun onClick(view: View, position: Int)
    }

    var sanClick: SanClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemSanListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.mountainName.text = mItems[position].mountain

        Glide.with(holder.mountainImage.context)
            .load(mItems[position].thumbnail)
            .into(holder.mountainImage)

        holder.itemView.setOnClickListener {
            sanClick?.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    inner class Holder(val binding: ItemSanListBinding) : RecyclerView.ViewHolder(binding.root) {
        val mountainImage = binding.ivSanImage
        val mountainName = binding.tvSanName
    }
}



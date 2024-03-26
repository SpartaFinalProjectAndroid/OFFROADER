package com.ing.offroader.ui.fragment.mydetail

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ing.offroader.R
import com.ing.offroader.data.model.ai.Message
import com.ing.offroader.databinding.ItemSanListBinding
import com.ing.offroader.ui.activity.sandetail.MyLikedSan

private const val TAG = "태그 : MyBookmarkAdapter"
class MyBookmarkAdapter : ListAdapter<MyLikedSan, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MyLikedSan>() {
            override fun areItemsTheSame(oldItem: MyLikedSan, newItem: MyLikedSan): Boolean {
                return oldItem.mountain == newItem.mountain
            }

            override fun areContentsTheSame(oldItem: MyLikedSan, newItem: MyLikedSan): Boolean {
                return oldItem == newItem
            }
        }
    }

    // 산 클릭 시 기능 추가
    interface SanClick {
        fun onClick(item: MyLikedSan)
    }

    var sanClick: SanClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemSanListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = getItem(position)

        (holder as Holder).apply {
            mountainName.text = item.mountain

            try {
                Glide.with(itemView).load(item.thumbnail).placeholder(R.drawable.ic_favorite).into(mountainImage)
            } catch (e: Exception) {
                Log.e(TAG, "onBindViewHolder: ",e )
            }
        }


        holder.itemView.setOnClickListener {
            sanClick?.onClick(item)
        }    }


    inner class Holder(val binding: ItemSanListBinding) : RecyclerView.ViewHolder(binding.root) {
        val mountainImage = binding.ivSanImage
        val mountainName = binding.tvSanName
    }
}



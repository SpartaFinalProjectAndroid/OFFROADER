package com.mit.offroader.ui.activity.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.INVISIBLE
import androidx.recyclerview.widget.RecyclerView.VISIBLE
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mit.offroader.R
import com.mit.offroader.databinding.ItemRadioChannelListBinding
import com.mit.offroader.ui.activity.main.MainViewModel

class RadioListAdapter(
    private val list: MutableList<String>,
    private val likeList: MutableList<String> = mutableListOf()
) : RecyclerView.Adapter<RadioListAdapter.Holder>() {

    interface ItemClick { fun onClick(key: String) }
    interface HeartClick { fun heartClick(key: String) }

    var itemClick : ItemClick ?= null
    var heartClick : HeartClick ?= null

    private var isPlay = false

    inner class Holder(private val binding: ItemRadioChannelListBinding) : ViewHolder(binding.root) {

        private var isLike = false

        fun bind(pos : Int) { binding.tvChannelTitle.text = list[pos]}

        fun checkPlay(pos: Int) {
            list[pos].run {
                binding.cvPlayStatus.visibility = VISIBLE
                binding.clRadioChannelItem.setBackgroundResource(R.color.offroader_outline)
            }
//            if (list[pos]) {
//                binding.cvPlayStatus.visibility = VISIBLE
//                binding.clRadioChannelItem.setBackgroundResource(R.color.offroader_outline)
//            } else {
//                binding.cvPlayStatus.visibility = INVISIBLE
//                binding.clRadioChannelItem.setBackgroundResource(R.color.white)
//            }
        }

        private fun checkIsLike(key: String) {
            if (likeList.contains(key)) {
                binding.ivHeart.setImageResource(R.drawable.ic_fill_heart)
                isLike = true
            } else {
                binding.ivHeart.setImageResource(R.drawable.ic_empty_heart)
                isLike = false
            }
        }
        fun likeSetting(key: String) {

            checkIsLike(key)

            binding.llHeart.setOnClickListener {
                heartClick?.heartClick(key)

                if (isLike) {
                    binding.ivHeart.setImageResource(R.drawable.ic_empty_heart)
                    isLike = false
                } else {
                    binding.ivHeart.setImageResource(R.drawable.ic_fill_heart)
                    isLike = true
                }
            }
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
            holder.likeSetting(this)
            holder.itemView.setOnClickListener {
                isPlay = true
                holder.checkPlay(position)
                itemClick?.onClick(this)
            }
        }
    }
}
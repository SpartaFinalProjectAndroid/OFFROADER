package com.ing.offroader.ui.activity.main.adapters


import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ing.offroader.R
import com.ing.offroader.databinding.ItemRadioChannelListBinding
import com.ing.offroader.ui.activity.main.MainViewModel

class RadioListAdapter(private val mainViewModel: MainViewModel)
    : ListAdapter<RadioChannelItem, RadioListAdapter.Holder>(differCallback) {
    companion object {
        val differCallback = object : DiffUtil.ItemCallback<RadioChannelItem>() {
            override fun areItemsTheSame(oldItem: RadioChannelItem, newItem: RadioChannelItem): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: RadioChannelItem, newItem: RadioChannelItem): Boolean {
                return oldItem == newItem
            }
        }
    }
    //val differ = AsyncListDiffer(this, differCallback)
    interface ItemClick { fun onClick(key: String, pos: Int) }
    interface HeartClick { fun heartClick(key: String) }

    var itemClick : ItemClick ?= null
    var heartClick : HeartClick ?= null

    inner class Holder(val binding : ItemRadioChannelListBinding) : RecyclerView.ViewHolder(binding.root){
        private var isLike = false

        fun bind(pos: Int) {
            binding.tvChannelTitle.text = currentList[pos].title
            if (currentList[pos].isPlay || mainViewModel.whoPlay.value == currentList[pos].title) {
                binding.cvPlayStatus.visibility = VISIBLE
                binding.clRadioChannelItem.setBackgroundResource(R.color.offroader_outline)
                currentList[pos].isPlay = true
            } else {
                binding.cvPlayStatus.visibility = INVISIBLE
                binding.clRadioChannelItem.setBackgroundResource(R.color.white)
            }
        }

        private fun checkIsLike(key: String) {
            if (mainViewModel.radioLikeList.value?.contains(key) == true) {
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
                    isLike = false
                    binding.ivHeart.setImageResource(R.drawable.ic_empty_heart)
                } else {
                    isLike = true
                    binding.ivHeart.setImageResource(R.drawable.ic_fill_heart)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRadioChannelListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        currentList[position].run {
            holder.bind(position)
            holder.likeSetting(this.title)
            holder.itemView.setOnClickListener {
                itemClick?.onClick(this.title, position)
            }
        }
    }
}


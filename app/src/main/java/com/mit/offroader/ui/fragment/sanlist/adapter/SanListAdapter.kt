package com.mit.offroader.ui.fragment.sanlist.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mit.offroader.databinding.ItemSanListBinding
import com.mit.offroader.ui.fragment.sanlist.model.SanDTO
import com.mit.offroader.ui.fragment.sanlist.viewmodel.SanListViewModel


class SanListAdapter(private val viewModel: SanListViewModel) :
    ListAdapter<SanDTO, RecyclerView.ViewHolder>(
        DIFF_CALLBACK
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemSanListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SanItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = getItem(position)

        (holder as SanItemViewHolder).apply {

            sanImage.bringToFront()

            sanOutline.isVisible = item.sanSelected
            if (item.sanImage != null) {
                Glide.with(itemView).load(item.sanImage[0]).into(sanImage)
            }
            sanName.text = item.sanName
        }
    }

    inner class SanItemViewHolder(binding: ItemSanListBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private var sanListItem: RelativeLayout = binding.rlItemSanList

        val sanImage = binding.ivSanImage
        val sanName = binding.tvSanName
        val sanOutline = binding.ivSanOutline

        init {
            sanListItem.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
            val item = getItem(position)

            viewModel.getSelectedItem(item)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SanDTO>() {
            override fun areItemsTheSame(oldItem: SanDTO, newItem: SanDTO): Boolean {
                return oldItem.sanName == newItem.sanName
            }

            override fun areContentsTheSame(oldItem: SanDTO, newItem: SanDTO): Boolean {
                return oldItem == newItem
            }
        }
    }


}



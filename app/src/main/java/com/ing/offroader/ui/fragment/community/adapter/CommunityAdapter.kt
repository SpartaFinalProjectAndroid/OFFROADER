package com.ing.offroader.ui.fragment.community.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ing.offroader.databinding.ItemPostBinding
import com.ing.offroader.ui.fragment.community.model.PostDTO
import com.ing.offroader.ui.fragment.community.viewmodel.CommunityViewModel
import com.ing.offroader.ui.fragment.sanlist.model.SanDTO

//class CommunityAdapter(private val viewModel: CommunityViewModel) :
//    ListAdapter<PostDTO, RecyclerView.ViewHolder>(
//        DIFF_CALLBACK
//    ) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return PostItemViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//
//        val item = getItem(position)
//
//        (holder as PostItemViewHolder).apply {
//
////            sanImage.bringToFront()
////
////            sanOutline.isVisible = item.sanSelected
////            if (item.sanImage != null) {
////                Glide.with(itemView).load(item.sanImage[0]).into(sanImage)
////            }
////            sanName.text = item.sanName
//        }
//    }
//
//    inner class PostItemViewHolder(binding: ItemPostBinding) :
//        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
//
//        private var postItem: ConstraintLayout = binding.clPost
////
////        val sanImage = binding.ivSanImage
////        val sanName = binding.tvSanName
////        val sanOutline = binding.ivSanOutline
////
////        init {
////            sanListItem.setOnClickListener(this)
////        }
//
//        override fun onClick(p0: View?) {
//            val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
//            val item = getItem(position)
//
////            viewModel.getSelectedItem(item)
//        }
//    }
//
//    companion object {
//        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SanDTO>() {
//            override fun areItemsTheSame(oldItem: SanDTO, newItem: SanDTO): Boolean {
//                return oldItem.sanName == newItem.sanName
//            }
//
//            override fun areContentsTheSame(oldItem: SanDTO, newItem: SanDTO): Boolean {
//                return oldItem == newItem
//            }
//        }
//    }
//
//
//}
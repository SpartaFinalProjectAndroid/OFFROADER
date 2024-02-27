package com.mit.offroader.ui.fragment.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mit.offroader.databinding.ItemTest1Binding
import com.mit.offroader.databinding.ItemTest2Binding

class HomeMultiViewTypeAdapter(private val context: Context) :
    ListAdapter<HomeUiData, RecyclerView.ViewHolder>(
        object : DiffUtil.ItemCallback<HomeUiData>() {
            override fun areItemsTheSame(oldItem: HomeUiData, newItem: HomeUiData): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: HomeUiData, newItem: HomeUiData): Boolean {
                return oldItem == newItem
            }
        }
    ) {

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is HomeUiData.First -> FIRST
            else -> SECOND
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FIRST -> {
                ProfileViewHolder(
                    ItemTest1Binding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                TitleViewHolder(
                    ItemTest2Binding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItem(position)) {
            is HomeUiData.First -> (holder as ProfileViewHolder).bind(currentList[position] as HomeUiData.First)
            else -> (holder as TitleViewHolder).bind(currentList[position] as HomeUiData.Second)
        }
    }

    // 프로필 영역 뷰홀더
    inner class ProfileViewHolder(private val binding: ItemTest1Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeUiData.First) = with(binding) {
            val vBlur = binding.i1FkView
            val title = binding.cardTitle
            val des = binding.cardDes

//            context.let { vBlur.setBlur(it, vBlur, 20) }
//
//            title.visibility = View.VISIBLE
//            des.visibility = View.VISIBLE

//            context.let { vBlur.setBlurCB(it, vBlur, 20, object : BlurCompletionListener {
//                override fun onCompleted() {
//                    title.visibility = View.VISIBLE
//                    des.visibility = View.VISIBLE
//                }
//            }


            context.let {
                vBlur.setBlurCB(it, vBlur, 40, object : BlurCompletionListener {
                    override fun onCompleted() {
                        title.visibility = View.VISIBLE
                        des.visibility = View.VISIBLE
                    }
                })
            }





        }
    }

    // 텍스트 영역 뷰홀더
    inner class TitleViewHolder(private val binding: ItemTest2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeUiData.Second) = with(binding) {

            // 텍스트 뷰홀더

        }
    }


    companion object {
        private const val FIRST = 1
        private const val SECOND = 2
    }
}


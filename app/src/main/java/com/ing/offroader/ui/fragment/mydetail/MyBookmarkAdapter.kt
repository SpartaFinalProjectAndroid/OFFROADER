package com.ing.offroader.ui.fragment.mydetail

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ing.offroader.data.liked.LikedUtil
import com.ing.offroader.databinding.ItemAdapterBookmarkBinding
import com.ing.offroader.ui.activity.sandetail.SanDetailDTO
import java.text.SimpleDateFormat
import java.util.Date

interface OnBookmarkClickedInMyLikedListener{
    fun onBookmarkClicked()
}
class MyBookmarkAdapter() : RecyclerView.Adapter<MyBookmarkAdapter.Holder>(), OnBookmarkClickedInMyLikedListener {

    var onBookmarkClickedInMyLikedListener: List<OnBookmarkClickedInMyLikedListener>? = null
    private var bookmarkedItem = LikedUtil.getLiked()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemAdapterBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(bookmarkedItem[position])
    }

    override fun getItemCount(): Int {
        return bookmarkedItem.size
    }

    fun refreshRecyclerView() {
        bookmarkedItem = LikedUtil.getLiked()
        notifyDataSetChanged()
    }

    override fun onBookmarkClicked() {
        Log.d("MyBookmarkAdapter", "onBookmarkClickedInMyLiked")
        refreshRecyclerView()
    }

    inner class Holder(val binding: ItemAdapterBookmarkBinding) : RecyclerView.ViewHolder(binding.root) {
        var mountainImage = binding.ivMountain
        var mountainName = binding.tvMountain
        var mountainTime = binding.tvTime
        var mountainDate = binding.tvDate

        fun bind(item: SanDetailDTO) {
            mountainName.text = item.mountain
//            mountainTime.text = item.time

            val now = System.currentTimeMillis()
            val date = Date(now)
            val dateFormat = SimpleDateFormat("yyyy.MM.dd")
            mountainDate.text = dateFormat.format(date)
        }
    }
}
package com.mit.offroader.data.liked

import android.util.Log
import com.mit.offroader.ui.activity.sandetail.SanDetailUiState

object OnBookmarkClickListener {
    private val TAG = "OnBookmarkClicked"

    fun onBookmarkClick(sanData: SanDetailUiState) {
        val likedMountain = LikedUtil.getLiked().toMutableList()
        Log.d(TAG, "before_likedMountain.size = ${likedMountain.size}")

        if(sanData.isLiked) {
            if(!LikedUtil.isSavedInLikedMountain(sanData.mountain)){
                likedMountain.add(sanData)
            }
        } else {
            likedMountain.find { it.mountain == sanData.mountain }?.let{
                likedMountain.remove(it)
            }
        }

        LikedUtil.savedLiked(likedMountain)
        Log.d(TAG, "after_likedVideos.size = ${likedMountain.size}")
    }
}
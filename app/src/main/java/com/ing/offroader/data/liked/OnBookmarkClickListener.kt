package com.ing.offroader.data.liked

import android.util.Log
import com.ing.offroader.ui.activity.sandetail.SanDetailDTO

object OnBookmarkClickListener {
    private val TAG = "OnBookmarkClicked"

    fun onBookmarkClick(sanData: SanDetailDTO) {
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

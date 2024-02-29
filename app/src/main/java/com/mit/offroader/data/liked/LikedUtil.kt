package com.mit.offroader.data.liked

import android.content.Context
import com.mit.offroader.ui.activity.sandetail.SanDetailUiState

object LikedUtil {

//    fun isSavedInLikedMountain() {
//        getLiked().find {it.isLiked == true } != null
//    }

    fun savedLiked(bookmark: List<SanDetailUiState>) {
        val context = MyApplication.appContext

        val prefs = context?.getSharedPreferences(
            LikedConstants.LIKED_PREFS_NAME,
            Context.MODE_PRIVATE
        )

        prefs?.edit()?.putString(LikedConstants.LIKED_PREF_KEY, "")?.apply()
    }

//    fun getLiked() : List<SanDetailUiState> {
//        val context = MyApplication.appContext
//
//        val prefs = context?.getSharedPreferences(
//            LikedConstants.LIKED_PREFS_NAME,
//            Context.MODE_PRIVATE
//        )
//
//        val json = prefs?.getString(LikedConstants.LIKED_PREF_KEY, null)
//        return if(json != null)
//    }
}
package com.ing.offroader.data.liked

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ing.offroader.ui.activity.sandetail.SanDetailDTO
import com.ing.offroader.ui.fragment.chatbot.MyApplication

object LikedUtil {

    fun isSavedInLikedMountain(mountainName: String): Boolean =
        getLiked().find { it.mountain == mountainName } != null

    fun savedLiked(bookmark: MutableList<SanDetailDTO>) {
        val context = MyApplication.appContext

        val prefs = context?.getSharedPreferences(
            LikedConstants.LIKED_PREFS_NAME,
            Context.MODE_PRIVATE
        )

        prefs?.edit()?.putString(LikedConstants.LIKED_PREF_KEY, "")?.apply()
    }

    fun getLiked() : List<SanDetailDTO> {
        val context = MyApplication.appContext

        val prefs = context?.getSharedPreferences(
            LikedConstants.LIKED_PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val json = prefs?.getString(LikedConstants.LIKED_PREF_KEY, null)
        return if(json != null) {
            val type = object : TypeToken<List<SanDetailDTO>>() {}.type
            Gson().fromJson(json, type)
        } else {
            emptyList()
        }
    }
}
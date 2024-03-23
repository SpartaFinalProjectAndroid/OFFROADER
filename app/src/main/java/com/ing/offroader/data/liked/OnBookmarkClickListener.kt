package com.ing.offroader.data.liked

object OnBookmarkClickListener {
//    private val TAG = "OnBookmarkClicked"
//
//    fun onBookmarkClick(sanlist: SanDetailDTO) {
//        val mountain = sanlist.mountain
//
//        val likedMountain = LikedUtil.getLiked()?.toMutableList()
//        Log.d(TAG, "before: likedMountain = $likedMountain")
//
//        val context = MyApplication.appContext
//
//        if (sanlist.isLiked) {
//            if (!LikedUtil.isSavedInLiked(mountain)) {
//                likedMountain?.add(sanlist)
//            }
//        } else {
//            likedMountain?.find { it.mountain == mountain }?.let {
//                likedMountain.remove(it)
//            }
//        }
//
//        likedMountain?.let { LikedUtil.saveLiked(it) }
//
//        // SanDetailDTO 객체를 JSON 문자열로 변환하여 저장
//        val gson = Gson()
//        val json = gson.toJson(likedMountain)
//        Log.d(TAG, "json = $json")
//
//        val prefs = context?.getSharedPreferences(LikedConstants.LIKED_PREFS, Context.MODE_PRIVATE)
//        prefs?.edit()?.putString(LikedConstants.LIKED_PREF_KEY, json)?.apply()
//
//        Log.d(TAG, "likedMountain.size = ${likedMountain?.size}")
//    }
}

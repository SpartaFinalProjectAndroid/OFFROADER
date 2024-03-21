package com.ing.offroader.data.liked

private const val TAG = "LikedUtil"

object LikedUtil {
//
//    fun isSavedInLiked(mountain: String): Boolean =
//        (getLiked()?.find { it.mountain == mountain } != null)
//
//
//    // SharedPreferences에 저장된 데이터를 가져오는 함수 수정
//    fun getLiked(): List<SanDetailDTO> {
//        val context = MyApplication.appContext
//        val prefs = context?.getSharedPreferences(LikedConstants.LIKED_PREFS, Context.MODE_PRIVATE)
//        val json = prefs?.getString(LikedConstants.LIKED_PREF_KEY, null)
//
//        Log.d(TAG, "getLiked: json = $json")
//
//        return if (json != null) {
//            val type = object : TypeToken<List<SanDetailDTO>>() {}.type
//            Gson().fromJson(json, type)
//        } else {
//            emptyList()
//        }
//    }
//
//    // SharedPreferences에 데이터를 저장하는 함수 수정
//    fun saveLiked(itemList: List<SanDetailDTO>) {
//        val context = MyApplication.appContext
//        val prefs = context?.getSharedPreferences(LikedConstants.LIKED_PREFS, Context.MODE_PRIVATE)
//        val editor = prefs?.edit()
//
//        // SanDetailDTO 리스트를 JSON 문자열로 변환하여 저장
//        val gson = Gson()
//        val json = gson.toJson(itemList)
//        editor?.putString(LikedConstants.LIKED_PREF_KEY, json)?.apply()
//    }
}
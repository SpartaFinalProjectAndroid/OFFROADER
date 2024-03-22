package com.ing.offroader.ui.activity.sandetail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SanDetailDTO(
    val mountain: String,
    val address: String,
    val difficulty: Long,
    val height: Double,
    val time: Long,
    val summary: String,
    val recommend: String,
    val img: ArrayList<String>,
    var isLiked: Boolean,
    var lat : Double,
    var lng : Double,
    var thumbnail: String
): Parcelable

data class MyLikedSan(
    val mountain: String,
    val thumbnail: String
)

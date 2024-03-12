package com.ing.offroader.ui.activity.sandetail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SanDetailDTO(
    val mountain: String,
    val address: String,
    val difficulty: Long,
    val height: Double,
    val uphillTime: Long,
    val downhillTime: Long,
    val summary: String,
    val recommend: String,
    val img: ArrayList<String>,
    var isLiked: Boolean
): Parcelable
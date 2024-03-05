package com.mit.offroader.ui.activity.sandetail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SanDetailUiState(
    val mountain: String,
    val address: String,
    val difficulty: Int,
    val height: Int,
    val uphilltime: Int,
    val downhilltime: Int,
    val intro: String,
    val recommend: String,
    var isLiked: Boolean
): Parcelable

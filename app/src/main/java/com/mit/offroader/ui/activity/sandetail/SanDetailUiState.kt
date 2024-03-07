package com.mit.offroader.ui.activity.sandetail

import android.os.Parcelable
import android.view.View
import kotlinx.parcelize.Parcelize

@Parcelize
data class SanDetailUiState(
    val mountain: String,
    val address: String,
    val difficulty: Long,
    val height: Double,
    val uphilltime: Long,
    val downhilltime: Long,
    val summary: String,
    val recommend: String,
    var isLiked: Boolean
): Parcelable

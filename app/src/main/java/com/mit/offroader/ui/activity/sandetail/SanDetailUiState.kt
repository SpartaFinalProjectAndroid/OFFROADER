package com.mit.offroader.ui.activity.sandetail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SanDetailUiState(
    val mountain: String,
    val address: String,
    val difficulty: String,
    val height: Int,
    val time: String,
    val intro: String,
    val recommend: String,
    var isLiked: Boolean
): Parcelable

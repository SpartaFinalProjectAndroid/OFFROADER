package com.ing.offroader.ui.fragment.mydetail

data class MyDetailDTO(
    val mountain: String,
    val address: String,
    val difficulty: Long,
    val height: Double,
    val uphillTime: Long,
    val summary: String,
    val recommend: String,
    val img: ArrayList<String>,
    var isLiked: Boolean
)

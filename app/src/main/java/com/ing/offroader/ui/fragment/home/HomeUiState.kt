package com.ing.offroader.ui.fragment.home

data class HomeUiState(
    val address: String?,
    val difficulty: Long?,
    val height: Long?,
    val images: ArrayList<String>?,
    val isLiked: Boolean?,
    val name: String?,
    val recommend: String?,
    val summary: String?,
    val timeDownhill: Long?,
    val timeUphill: Long?
)

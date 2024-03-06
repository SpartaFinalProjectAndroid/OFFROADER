package com.mit.offroader.ui.fragment.sanlist

data class SanDTO(
    val name: String,
    val address: String,
    val difficulty: Long,
    val height: Long,
    val timeUphill: Long,
    val timeDownhill: Long,
    val summary: String,
    val recommendation: String
)

package com.ing.offroader.ui.fragment.sanlist.model

data class SanDTO(
    val sanImage: ArrayList<String>?,
    val sanName: String?,
    var sanSelected: Boolean,
    val sanHeight: Long?,
    val sanTimeTotal: Long?,
    val sanDifficulty: Long?

)



package com.ing.offroader.ui.activity.main.adapters

data class RadioChannelItem(
    val title : String,
    var isPlay : Boolean = false,
)

data class HttpItem(
    val url : String,
    val key : String,
    val radioIcon : Int,
    val position : Int,
)


package com.ing.offroader.ui.activity.add_post

data class AddPostUiState(
    var title : String?,
    var content: String?,
    val image : ByteArray?,
    val errorMessage : String?,
    val cycle : Boolean = true,
    val edit : String? = null
)

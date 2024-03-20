package com.ing.offroader.ui.activity.add_post

data class AddPostUiState(
    var title : String,
    var contnet: String,
    val images : String,
    val errorMessage : String,
    val cycle : Boolean
)

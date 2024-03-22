package com.ing.offroader.ui.activity.login

import android.net.Uri

data class LoginUser(
    val uid: String,
    val name: String?,
    val email: String?,
    val photoUrl: Uri?,
    var isAuthenticated: Boolean = false,
    var isNew: Boolean? = false,
    var isCreated: Boolean = false

)
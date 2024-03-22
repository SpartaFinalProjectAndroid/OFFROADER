package com.ing.offroader.ui.activity.login

import java.io.Serializable

data class LoginUser(
    val uid: String,
    val name: String?,
    val email: String?,
    var isAuthenticated: Boolean = false,
    var isNew: Boolean? = false,
    var isCreated: Boolean = false

)
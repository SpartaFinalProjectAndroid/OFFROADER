package com.ing.offroader.data.model.addpost

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostModel(
    var userName : String?= null,
    var userProfileImage : Uri? = null,
    var contents : String ?= null,
    var images : Uri?= null,
    val like : Int ?= null,
    val postId : String ?= null,
    val san : String ?= null,
    var title : String ?= null,
    val uid : String ?= null,
    val upload_date : Long ?= null
) : Parcelable



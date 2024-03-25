package com.ing.offroader.ui.fragment.community.model

import android.graphics.Bitmap
import android.net.Uri

data class PostDTO(
    var contents : Any ?= null,
    var images : Uri ?= null,
    val like : Any ?= null,
    val post_id : Any ?= null,
    val san : Any ?= null,
    var title : Any ?= null,
    val uid : Any ?= null,
    val upload_date : Any ?= null
)

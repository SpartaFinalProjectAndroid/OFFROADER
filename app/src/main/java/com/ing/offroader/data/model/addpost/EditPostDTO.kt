package com.ing.offroader.data.model.addpost

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditPostDTO(
    var title :String?,
    var content : String?,
    var postId : String?
) : Parcelable

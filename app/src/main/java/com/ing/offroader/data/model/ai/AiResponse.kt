package com.ing.offroader.data.model.ai

import com.google.gson.annotations.SerializedName

data class AiResponse(
    @SerializedName("choices")
    val choices : List<Choice>,
    val created : Int,
    val id : String,
    val model: String,
    @SerializedName("object")
    val objects: String,
    val usage: Usage
)
package com.ing.offroader.data.model.ai

import com.ing.offroader.data.api.AI_MODEL

data class AiRequest(
    val model: String = AI_MODEL,
    val messages: List<Message>
)
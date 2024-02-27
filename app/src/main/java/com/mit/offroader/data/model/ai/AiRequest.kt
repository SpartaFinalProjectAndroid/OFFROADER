package com.mit.offroader.data.model.ai

import com.mit.offroader.data.api.AI_MODEL

data class AiRequest(
    val model: String = AI_MODEL,
    val messages: List<Message>
)
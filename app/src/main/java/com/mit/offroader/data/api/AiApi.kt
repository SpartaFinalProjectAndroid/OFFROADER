package com.mit.offroader.data.api


import com.mit.offroader.data.model.ai.AiRequest
import com.mit.offroader.data.model.ai.AiResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

private const val AI_API_KEY = "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"
const val AI_MODEL = "gpt-3.5-turbo"

interface AiApi {
    @Headers("Authorization: Bearer $AI_API_KEY")
    @POST("chat/completions")

    suspend fun createChatCompletion(@Body body: AiRequest) : AiResponse
}
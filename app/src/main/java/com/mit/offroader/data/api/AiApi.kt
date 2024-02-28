package com.mit.offroader.data.api


import com.mit.offroader.data.model.ai.AiRequest
import com.mit.offroader.data.model.ai.AiResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

//private val aiApiKey : String = System.getenv("OPENAI_API_KEY") ?: "default_value"
const val AI_MODEL = "gpt-3.5-turbo"

private const val AI_API_KEY = "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"

interface AiApi {
    @Headers("Authorization: Bearer $AI_API_KEY")
    @POST("chat/completions")

    suspend fun createChatCompletion(@Body body: AiRequest) : AiResponse
}
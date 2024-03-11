package com.ing.offroader.data.repository

import android.util.Log
import com.ing.offroader.data.RetrofitInstance
import com.ing.offroader.data.model.ai.AiRequest
import com.ing.offroader.data.model.ai.AiResponse
import com.ing.offroader.data.model.ai.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AiRepository {
    suspend fun createChatCompletion(message: String): AiResponse = withContext(Dispatchers.IO) {
        val request = AiRequest(messages = listOf(Message("user", message)))
        Log.d("Connect ChatGPT", "2. Ai Repository")
        RetrofitInstance.aiApi.createChatCompletion(request)

    }
}
package com.mit.offroader.data.repository

import android.util.Log
import com.mit.offroader.data.RetrofitInstance
import com.mit.offroader.data.model.ai.AiRequest
import com.mit.offroader.data.model.ai.AiResponse
import com.mit.offroader.data.model.ai.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AiRepository {
    suspend fun createChatCompletion(list: List<Message>): AiResponse = withContext(Dispatchers.IO) {
        val request = AiRequest(messages = list)
        Log.d("Connect ChatGPT", "2. Ai Repository")
        RetrofitInstance.aiApi.createChatCompletion(request)

    }
}
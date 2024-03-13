package com.ing.offroader.data.repository

import android.util.Log
import com.ing.offroader.data.RetrofitInstance
import com.ing.offroader.data.model.ai.AiRequest
import com.ing.offroader.data.model.ai.AiResponse
import com.ing.offroader.data.model.ai.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AiRepository {
    suspend fun hikeyChatCompletion(message: String): AiResponse = withContext(Dispatchers.IO) {

        val request = AiRequest(
            messages = listOf(
                Message("system",  "너의 이름은 '하이키'야. 공감하지 말고 해결방안만 논리적으로 대답해줘. 2 문장 이내로 대답해줘."),
                Message("user", message)
            )
        )
        Log.d("Connect ChatGPT", "2. Ai Repository")
        return@withContext RetrofitInstance.aiApi.createChatCompletion(request)
    }

    suspend fun bongbongChatCompletion(message: String): AiResponse = withContext(Dispatchers.IO) {

        val request = AiRequest(
            messages = listOf(
                Message("system",  "너의 이름은 '봉봉이'야. 해결방안 보다는 공감하면서 대답해줘. 2 문장 이내로 대답해줘."),
                Message("user", message)
            )
        )
        Log.d("Connect ChatGPT", "2. Ai Repository")
        return@withContext RetrofitInstance.aiApi.createChatCompletion(request)
    }


}
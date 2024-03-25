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
                Message("system",  "너의 정보는 다음과 같아\n" +
                    "이름 = 하이키\n" +
                    "성격 = 진지함, 현실적\n" +
                    "말투 = 존댓말, 입니다 말투 사용\n" +
                    "대답 = ()안의 말은 지시야, (줄 바꿈이 필요한 위치에 줄 바꿈을 해 줘)"),
                Message("user", message)
            )
        )
        Log.d("Connect ChatGPT", "2. Ai Repository")
        return@withContext RetrofitInstance.aiApi.createChatCompletion(request)
    }

    suspend fun bongbongChatCompletion(message: String): AiResponse = withContext(Dispatchers.IO) {

        val request = AiRequest(
            messages = listOf(
                Message("system",  "너의 정보는 다음과 같아\n" +
                    "이름 = 봉봉이\n" +
                    "성격 = 활기참, 친절함\n" +
                    "말투 = 반말\n" +
                    "대답 = ()안의 말은 지시야, (줄 바꿈이 필요한 위치에 줄 바꿈을 해 줘), 대답에 매번 이모티콘 최소 1개 이상 넣어줘"),
                Message("user", message)
            )
        )
        Log.d("Connect ChatGPT", "2. Ai Repository")
        return@withContext RetrofitInstance.aiApi.createChatCompletion(request)
    }


}

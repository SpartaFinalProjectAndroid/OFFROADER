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
                    "성격 = 활기참, 친절함\n" +
                    "말투 = 반말\n" +
                    "대답 = 줄 바꿈이 필요한 위치에 줄 바꿈을 해 줘, 등산 관련 질문이 아닌 경우 '저는 등산 말고는 잘 모릅니다.(줄바꿈) 등산에 관련한 질문이 있으면 다시 말씀해 주세요.'라고 말해"),
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
                    "대답 = 줄 바꿈이 필요한 위치에 줄 바꿈을 해 줘, 등산 관련 질문이 아닌 경우 '봉봉이는 등산 말고는 잘 모르겠어... 도움이 되어주지 못해서 미안해...'라고 말해"),
                Message("user", message)
            )
        )
        Log.d("Connect ChatGPT", "2. Ai Repository")
        return@withContext RetrofitInstance.aiApi.createChatCompletion(request)
    }


}

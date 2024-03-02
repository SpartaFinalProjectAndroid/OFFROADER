package com.mit.offroader.ui.fragment.chatbot

import com.mit.offroader.data.model.ai.Message

data class ChatBotUiState(
    val chatWithHikey: List<Message>,
    val chatWithBongBong: List<Message>,
    val channelResponse: String,
) {
    companion object {
        fun init() = ChatBotUiState(
            chatWithHikey = listOf(),
            chatWithBongBong = listOf(),
            channelResponse = ""
        )
    }
}

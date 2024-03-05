package com.mit.offroader.ui.fragment.chatbot.database

import com.mit.offroader.data.model.ai.Message

data class ChatUiState(
    val chat: List<Message>,
    val position: String
) {
    companion object {
        fun init() = ChatUiState(
            chat = listOf(),
            position = "hikey",
        )
    }
}

package com.ing.offroader.ui.fragment.chatbot.database

import com.ing.offroader.data.model.ai.Message

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

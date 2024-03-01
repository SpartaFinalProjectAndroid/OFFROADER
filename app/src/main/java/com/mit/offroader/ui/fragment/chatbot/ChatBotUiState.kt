package com.mit.offroader.ui.fragment.chatbot

import com.mit.offroader.data.model.ai.Message

data class ChatBotUiState(
    val chatContent: ArrayList<Message>?,
) {
    companion object {
        fun init() = ChatBotUiState(
            chatContent = Conversation.hikeyConversation
        )
    }
}

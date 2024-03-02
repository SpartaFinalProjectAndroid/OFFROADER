package com.mit.offroader.ui.fragment.chatbot

import com.mit.offroader.data.model.ai.Message
import com.mit.offroader.ui.fragment.chatbot.database.Conversation
import com.mit.offroader.ui.fragment.chatbot.database.ConversationRecord

data class ChatBotUiState(
    val chatWithHikey: List<Message>,
    val chatWithBongBong: List<Message>
) {
    companion object {
        fun init() = ChatBotUiState(
            chatWithHikey = listOf(Message("assistant", "저에게 궁금한걸 물어보세요!")),
            chatWithBongBong = listOf(Message("assistant", "저에게 궁금한걸 물어보세요!"))
        )
    }
}

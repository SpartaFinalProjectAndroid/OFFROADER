package com.mit.offroader.ui.fragment.chatbot

import com.mit.offroader.data.model.ai.Message

object Conversation {
    // 임시 대화 저장 공간

    var hikeyConversation = arrayListOf<Message>(

    )

    // 대화를 추가해줌
    fun addText(chat: Message) {
        hikeyConversation.add(chat)
    }


}
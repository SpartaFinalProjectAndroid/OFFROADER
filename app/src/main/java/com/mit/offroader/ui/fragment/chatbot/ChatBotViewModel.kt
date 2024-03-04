package com.mit.offroader.ui.fragment.chatbot

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.mit.offroader.data.model.ai.Message
import com.mit.offroader.data.repository.AiRepository
import com.mit.offroader.ui.fragment.chatbot.database.ConversationRecord
import kotlinx.coroutines.launch
import java.util.UUID

class ChatBotViewModel(
    private val aiRepo: AiRepository = AiRepository(),
    private val chatBotRepository: ChatBotRepository
) : ViewModel() {


    val chatBotUiState: LiveData<ChatBotUiState> = chatBotRepository.conversation.map { conversationRecords ->
        createChatBotUiState(conversationRecords)
    }

    private fun createChatBotUiState(conversationRecords: List<ConversationRecord>): ChatBotUiState {
        Log.d("Submit", "^^ SUCCESS")
        val hikeyConversation: ArrayList<Message> = arrayListOf()
        val bongbongConversation: ArrayList<Message> = arrayListOf()
        conversationRecords.forEach {record ->
            when (record.aiType) {
                "hikey" -> hikeyConversation.add(Message(record.role,record.content))
                "bongbong" -> bongbongConversation.add(Message(record.role,record.content))
            }

        }

        return ChatBotUiState(
            chatWithHikey = hikeyConversation,
            chatWithBongBong = bongbongConversation
        )
    }



    fun setBotSpinner(position: Int) {
        when (position) {
            0 -> {
                // TODO : 하이키 채팅 목록 업데이트 해주기

            }

            1 -> {
                // TODO : 봉봉이 채팅 목록 업데이트 해주기

            }
        }
    }

    private suspend fun addMessageToDB(role: String, message: String) {
        chatBotRepository.insertChat(
            ConversationRecord(
                UUID.randomUUID().toString(),
                "hikey",
                role,
                message
            )
        )

    }

    fun setSearch(text: String) {
        lateinit var response: String
        viewModelScope.launch {
            runCatching {

                addMessageToDB("user", text)


                addMessageToDB(
                    "assistant",
                    aiRepo.createChatCompletion("$text. 최대 두문장으로 간단히 대답해줘.").choices.first().message.content
                )


            }.onSuccess {
                Log.d("Connect ChatGPT", "^^Successful!")


            }.onFailure {
                Log.d("Connect ChatGPT", "^^Failed!")

            }
        }

    }




    fun setClearChat() {
        viewModelScope.launch {
            chatBotRepository.deleteConversation()
        }

    }


}
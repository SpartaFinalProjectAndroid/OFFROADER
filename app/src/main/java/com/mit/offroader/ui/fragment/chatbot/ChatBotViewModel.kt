package com.mit.offroader.ui.fragment.chatbot

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
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

    private var _chatBotUiState = MutableLiveData<ChatBotUiState>()
    val chatBotUiState: LiveData<ChatBotUiState> = _chatBotUiState

    var conversation: LiveData<List<ConversationRecord>> =
        chatBotRepository.conversation.asLiveData()

    init {
        _chatBotUiState.value = ChatBotUiState.init()
    }

    fun setClearChat() {
        viewModelScope.launch {
            chatBotRepository.deleteConversation()
        }
        setChat()
    }

    fun setSearch(text: String) {
        addSearchToDatabase(text)


    }


    private fun addSearchToDatabase(text: String) {
        Log.i("addSearchToDatabase()", "^ 1. 엔터 누름 저장 됨")
        viewModelScope.launch {
            chatBotRepository.insertChat(
                ConversationRecord(
                    UUID.randomUUID().toString(),
                    "hikey",
                    "user",
                    text
                )
            )
        }
        setChat()
        getResponseFromChatGpt(text)

    }

    private fun getResponseFromChatGpt(text: String) {
        Log.i("getResponseFromChatGPt()", "^ 5. 엔터 누름 저장 됨")

        viewModelScope.launch {
            kotlin.runCatching {


                addResponseToDatabase(
                    aiRepo.createChatCompletion("$text. 한문장으로 대답해줘.").choices.first().message.content
                )
            }.onSuccess {

            }.onFailure {
                Log.d("ChatBotViewModel", "^^ 챗지피티에서 값 가져오기 실패")
            }
        }
    }

    private fun addResponseToDatabase(text: String) {
        Log.i("addResponseToDatabase()", "^ 6. 엔터 누름 저장 됨")

        viewModelScope.launch {
            chatBotRepository.insertChat(
                ConversationRecord(
                    UUID.randomUUID().toString(),
                    "hikey",
                    "assistant",
                    text
                )
            )
        }
        setChat()
    }

    fun setChat() {
        val hikeyConversation: ArrayList<Message> = arrayListOf()
        val bongbongConversation: ArrayList<Message> = arrayListOf()
        Log.i(
            "setChat()",
            "^ 2/7. 챗에 업데이트 해줌 : ${chatBotRepository.conversation.asLiveData().value?.toString()}"
        )

        chatBotRepository.conversation.asLiveData().value?.apply {

            for (i in this.indices) {
                when (this[i].aiType) {
                    "hikey" -> {
                        hikeyConversation.add(Message(this[i].role, this[i].content))
                    }

                    "bongbong" -> {
                        bongbongConversation.add(Message(this[i].role, this[i].content))
                    }
                }
            }
        }
        _chatBotUiState.value = _chatBotUiState.value?.copy(
            chatWithHikey = hikeyConversation,
            chatWithBongBong = bongbongConversation
        )
    }

    fun setBotSpinner(i: Int) {

    }
}
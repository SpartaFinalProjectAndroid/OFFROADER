package com.mit.offroader.ui.fragment.chatbot

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mit.offroader.data.model.ai.Message
import com.mit.offroader.data.repository.AiRepository
import kotlinx.coroutines.launch

class ChatBotViewModel(private val aiRepo: AiRepository = AiRepository()) : ViewModel() {


    private var _chatBotUiState = MutableLiveData<ChatBotUiState>()

    val chatBotUiState: LiveData<ChatBotUiState> = _chatBotUiState

    init {
        _chatBotUiState.value = ChatBotUiState.init()
    }

    fun setBotSpinner(position: Int) {
        when (position) {
            0 -> {
                // TODO : 하이키 채팅 목록 업데이트 해주기
                setChat()
            }

            1 -> {
                // TODO : 봉봉이 채팅 목록 업데이트 해주기
                setChat()
            }
        }

    }

    fun onSearch(text: String) {
        viewModelScope.launch {
            runCatching {

                val message = "한 문장으로 대답해줘. $text"

                Log.d("Connect ChatGpt", "^^ -1. $message")

                val channelResponse =
                    aiRepo.createChatCompletion(text).choices.first().message.content

                Log.d("Connect ChatGPT", "^^ 0. channelResponse = $channelResponse")

                Log.d("Connect ChatGPT", "^^ 1. ViewModel")
                Conversation.addText(Message("user", text))
                Conversation.addText(Message("ai",channelResponse))



            }.onSuccess {
                Log.d("Connect ChatGPT", "^^Successful!")
                _chatBotUiState.value = _chatBotUiState.value?.copy(chatContent = Conversation.hikeyConversation)
            }.onFailure {
                Log.d("Connect ChatGPT", "^^Failed!")

            }
        }

    }

    private fun setChat() {


    }

}
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
            kotlin.runCatching {

                Log.d("Connect ChatGPT", "^^ 1. ViewModel")
                Conversation.addText(Message("user",text))
                val messages = Conversation.hikeyConversation.toList()
                val channelResponse =
                    aiRepo.createChatCompletion(messages).choices.first().message.content

                _chatBotUiState.value = ChatBotUiState(response = channelResponse)

            }.onSuccess {
                Log.d("Connect ChatGPT", "^^Successful!")
            }.onFailure {
                Log.d("Connect ChatGPT", "^^Failed!")

            }
        }

    }

    private fun setChat() {


    }

    private var _chatBotUiState = MutableLiveData<ChatBotUiState>()

    val chatBotUiState: LiveData<ChatBotUiState> = _chatBotUiState
}
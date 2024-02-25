package com.mit.offroader.fragment.chatbot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChatBotViewModel : ViewModel() {
    private var _chatBotUiState = MutableLiveData<ChatBotUiState>()

    val chatBotUiState : LiveData<ChatBotUiState> = _chatBotUiState
}
package com.mit.offroader.ui.fragment.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mit.offroader.data.repository.AiRepository

class ChatBotViewModelFactory(private val hikeyRepository: HikeyRepository,private val bongbongRepository: BongbongRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatBotViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatBotViewModel(AiRepository(), hikeyRepository, bongbongRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")    }
}
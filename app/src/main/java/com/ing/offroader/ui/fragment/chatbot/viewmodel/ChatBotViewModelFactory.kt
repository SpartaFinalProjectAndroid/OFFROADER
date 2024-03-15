package com.ing.offroader.ui.fragment.chatbot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ing.offroader.data.repository.AiRepository
import com.ing.offroader.data.repository.BongbongRepository
import com.ing.offroader.data.repository.HikeyRepository

class ChatBotViewModelFactory(private val hikeyRepository: HikeyRepository, private val bongbongRepository: BongbongRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatBotViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatBotViewModel(AiRepository(), hikeyRepository, bongbongRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")    }
}
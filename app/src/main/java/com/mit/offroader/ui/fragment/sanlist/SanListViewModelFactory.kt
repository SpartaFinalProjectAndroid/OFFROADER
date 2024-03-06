package com.mit.offroader.ui.fragment.sanlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mit.offroader.data.repository.AiRepository
import com.mit.offroader.ui.fragment.chatbot.viewmodel.BongbongRepository
import com.mit.offroader.ui.fragment.chatbot.viewmodel.ChatBotViewModel
import com.mit.offroader.ui.fragment.chatbot.viewmodel.HikeyRepository

class SanListViewModelFactory(private val sanListRepository: SanListRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SanListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SanListViewModel(sanListRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")    }
}
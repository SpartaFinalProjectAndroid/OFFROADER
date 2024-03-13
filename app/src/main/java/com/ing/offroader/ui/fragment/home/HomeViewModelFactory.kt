package com.ing.offroader.ui.fragment.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ing.offroader.data.repository.EventRepository
import com.ing.offroader.data.repository.SanListRepository

class HomeViewModelFactory(private val sanListRepository: SanListRepository, private val eventRepository: EventRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(sanListRepository, eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
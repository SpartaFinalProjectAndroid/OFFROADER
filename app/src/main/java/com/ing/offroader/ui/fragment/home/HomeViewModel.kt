package com.ing.offroader.ui.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ing.offroader.data.repository.EventRepository
import com.ing.offroader.data.repository.SanListRepository


class HomeViewModel(sanListRepository: SanListRepository, eventRepository: EventRepository) : ViewModel() {

    private val sanListRepo : SanListRepository = sanListRepository
    private val eventRepo : EventRepository = eventRepository

    val recItems : LiveData<ArrayList<HomeUiState>> = sanListRepo.recItems
    val eventItems : LiveData<ArrayList<HomeUiData.Fourth>> = eventRepo.eventItems

    fun takeAttendance() {

    }


}
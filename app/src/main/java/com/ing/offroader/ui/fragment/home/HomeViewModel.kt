package com.ing.offroader.ui.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel


class HomeViewModel(homeDataRepository: HomeDataRepository) : ViewModel() {

    private val repo : HomeDataRepository = homeDataRepository

    val recItems : LiveData<ArrayList<HomeUiState>> = repo.recItems
    val eventItems : LiveData<ArrayList<HomeUiData.Fourth>> = repo.eventItems

}
package com.mit.offroader.ui.fragment.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private const val TAG = "초기화"

class HomeViewModel(homeDataRepository: HomeDataRepository) : ViewModel() {
    private var _homeUiState = MutableLiveData<HomeUiState>()

    private val repo : HomeDataRepository = homeDataRepository


    val recItems : LiveData<ArrayList<HomeUiState>> = repo.recItems
    val eventItems : LiveData<ArrayList<HomeUiData.Fourth>> = repo.eventItems




}
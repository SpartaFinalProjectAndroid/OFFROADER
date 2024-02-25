package com.mit.offroader.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    private var _homeUiState = MutableLiveData<HomeUiState>()

    val homeUiState : LiveData<HomeUiState> = _homeUiState
}
package com.mit.offroader.ui.activity.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private var _mainUiState = MutableLiveData<MainUiState>()

    val mainUiState : LiveData<MainUiState> = _mainUiState
}
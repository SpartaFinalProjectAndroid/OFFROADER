package com.ing.offroader.ui.fragment.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SanMapViewModel : ViewModel() {
    private var _San_mapUiState = MutableLiveData<SanMapUiState>()

    val sanMapUiState : LiveData<SanMapUiState> = _San_mapUiState
}
package com.mit.offroader.ui.fragment.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {
    private var _mapUiState = MutableLiveData<MapUiState>()

    val mapUiState : LiveData<MapUiState> = _mapUiState
}
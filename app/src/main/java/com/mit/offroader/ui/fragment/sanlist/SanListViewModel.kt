package com.mit.offroader.ui.fragment.sanlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SanListViewModel : ViewModel() {
    private var _sanListUiState = MutableLiveData<SanListUiState>()

    val sanListUiState : LiveData<SanListUiState> = _sanListUiState
}
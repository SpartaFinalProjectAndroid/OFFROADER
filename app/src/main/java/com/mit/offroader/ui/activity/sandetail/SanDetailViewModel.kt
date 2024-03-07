package com.mit.offroader.ui.activity.sandetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SanDetailViewModel : ViewModel() {
    private var _sanDetailUiState = MutableLiveData<SanDetailUiState>()

    val sanDetailUiState : LiveData<SanDetailUiState> = _sanDetailUiState
}

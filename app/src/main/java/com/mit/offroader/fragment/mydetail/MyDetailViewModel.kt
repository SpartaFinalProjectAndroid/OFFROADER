package com.mit.offroader.fragment.mydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyDetailViewModel : ViewModel() {
    private var _myDetailUiState = MutableLiveData<MyDetailUiState>()

    val myDetailUiState : LiveData<MyDetailUiState> = _myDetailUiState
}
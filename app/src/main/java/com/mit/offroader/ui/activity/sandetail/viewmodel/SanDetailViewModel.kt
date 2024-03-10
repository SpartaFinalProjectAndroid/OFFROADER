package com.mit.offroader.ui.activity.sandetail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mit.offroader.ui.activity.sandetail.SanDetailUiState

class SanDetailViewModel(sanDetailRepository: SanDetailRepository) : ViewModel() {
    private var _sanDetailUiState = MutableLiveData<SanDetailUiState>()
    private val repo : SanDetailRepository = sanDetailRepository

    val sanDetailUiState : LiveData<SanDetailUiState> = _sanDetailUiState

    private val recItems : LiveData<ArrayList<SanDetailUiState>> = repo.recItems
}

package com.mit.offroader.ui.activity.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {


    private var _mainUiState = MutableLiveData<MainUiState>()

    val mainUiState : LiveData<MainUiState> = _mainUiState

    // bottom Navigation View 셋팅 (디폴트 프래그먼트: 홈 프래그먼트)
//    fun onBottomItemChanged(id: Int) {
//        when (id) {
//            MainFragmentId.HOME. -> {
//
//            }
//        }
//    }

    
}
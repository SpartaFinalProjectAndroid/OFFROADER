package com.mit.offroader.ui.fragment.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private const val TAG = "초기화"

class HomeViewModel(homeDataRepository: HomeDataRepository) : ViewModel() {
    private var _homeUiState = MutableLiveData<HomeUiState>()

    private val repo : HomeDataRepository = homeDataRepository
//    val homeUiState : LiveData<HomeUiState> = _homeUiState
//
//    private var _uiState = MutableLiveData<UiState>()
//
//    val uiState : LiveData<UiState> = UiState(repo.recItems,repo.eventItems.value)

    val recItems : LiveData<ArrayList<HomeUiState>> = repo.recItems
    val eventItems : LiveData<ArrayList<HomeUiData.Fourth>> = repo.eventItems

    var _uiState : MutableLiveData<UiState> = MutableLiveData(UiState(rvItems = recItems.value, eventItems = eventItems.value))
    val uiState : LiveData<UiState> get() = _uiState
//    val uiState : LiveData<UiState> = UiState(rvItems = recItems.value., eventItems = eventItems)
//    val uiState : LiveData<UiState> = LiveData<UiState>(rvItems = recItems.value., eventItems = eventItems)

    init {
        Log.d(TAG,"${repo.recItems.value}")

//        _uiState.value = UiState(repo.recItems.value, repo.eventItems.value)
    }

//
//    private var _rvItems : MutableLiveData<ArrayList<HomeUiState>?> = MutableLiveData(repo.recItems)
//    val rvItems : LiveData<ArrayList<HomeUiState>?> get() = _rvItems //get은 비밀통로 학습요망
//
//    private var _eventItems : MutableLiveData<ArrayList<HomeUiData.Fourth>?> = MutableLiveData(repo.eventItems)
//    val eventItems : LiveData<ArrayList<HomeUiData.Fourth>?> get() = _eventItems
//

    fun initialise() {
        if (repo.recItems != null && repo.eventItems != null ) {
            _uiState.value = UiState(repo.recItems.value,repo.eventItems.value)
        } else {
            Log.d(TAG, "데이터베이스에서 값이 안넘어온다.")
        }

        Log.d(TAG, repo.recItems.value.toString())
    }
}
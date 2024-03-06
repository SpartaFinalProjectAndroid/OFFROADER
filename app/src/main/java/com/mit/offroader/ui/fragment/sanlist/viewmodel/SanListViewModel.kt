package com.mit.offroader.ui.fragment.sanlist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mit.offroader.ui.fragment.sanlist.model.SanDTO
import com.mit.offroader.ui.fragment.sanlist.model.SanListRepository

private const val TAG = "SanListViewModel"

class SanListViewModel(sanListRepository: SanListRepository) : ViewModel() {
    private var _sanListUiState = MutableLiveData<SanListUiState?>()

    val sanListUiState: LiveData<SanListUiState?> = _sanListUiState

    val sanList: LiveData<List<SanDTO>> = sanListRepository.sanListDTO

    init {

        _sanListUiState.value = SanListUiState.init()
    }

    fun getSelectedItem(item: SanDTO?) {
        Log.d(TAG, "getSelectedItem ${item.toString()}")
        if (item == null) {
            Log.d(TAG, "널값 넘어옴")

        } else {
            _sanListUiState.value = sanListUiState.value?.copy(selectedItem = item)
            Log.d(TAG, _sanListUiState.value.toString())
        }
    }

    fun setInitiallySelectedItem() {
        _sanListUiState.value = sanListUiState.value?.copy(
            selectedItem = sanList.value?.get(0))
    }
}
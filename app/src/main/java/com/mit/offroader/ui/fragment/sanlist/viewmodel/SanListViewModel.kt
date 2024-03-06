package com.mit.offroader.ui.fragment.sanlist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mit.offroader.ui.fragment.sanlist.model.SanDTO
import com.mit.offroader.ui.fragment.sanlist.model.SanListRepository

class SanListViewModel(sanListRepository: SanListRepository) : ViewModel() {
    private var _sanListUiState = MutableLiveData<SanListUiState?>()

    val sanListUiState: LiveData<SanListUiState?> = _sanListUiState

    val sanListRepository : LiveData<List<SanDTO>> = sanListRepository.sanListDTO

    init {
        // sanSelected 값이 true인 sanDTO를 찾아서 sanListUiState안에 있는 selectedItem에 sanDTO를 저장해줌
        sanListUiState.value?.selectedItem = sanListRepository.sanListDTO.value?.find {
            it.sanSelected
        }
    }

    fun getSelectedItem(item: SanDTO?) {
        _sanListUiState.value =
            item?.let { _sanListUiState.value?.copy(selectedItem = it) }
    }
}
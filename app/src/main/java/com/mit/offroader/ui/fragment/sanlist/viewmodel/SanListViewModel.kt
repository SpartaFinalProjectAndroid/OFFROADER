package com.mit.offroader.ui.fragment.sanlist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mit.offroader.ui.fragment.sanlist.model.SanDTO
import com.mit.offroader.ui.fragment.sanlist.model.SanListRepository

private const val TAG = "SanListViewModel"

class SanListViewModel(sanListRepository: SanListRepository) : ViewModel() {

    private val repository : SanListRepository = sanListRepository
    private var _sanListUiState = MutableLiveData<SanListUiState?>()

    val sanListUiState: LiveData<SanListUiState?> = _sanListUiState

    val sanList: LiveData<List<SanDTO>> = repository.sanListDTO



    init {
        // 선택된 산 초기화해주기
        _sanListUiState.value = SanListUiState.init()
    }

    // 선택된 산 값을 업데이트 시켜주는 함수. null이면 로그 띄워줌.
    fun getSelectedItem(item: SanDTO?) {
        Log.d(TAG, "getSelectedItem ${item.toString()}")
        if (item == null) {
            Log.d(TAG, "널값 넘어옴")

        } else {
            _sanListUiState.value = sanListUiState.value?.copy(selectedItem = item)
            Log.d(TAG, _sanListUiState.value.toString())




        }
    }

    // 값 업데이트
    fun setInitiallySelectedItem() {
        _sanListUiState.value = sanListUiState.value?.copy(
            selectedItem = sanList.value?.get(0)
        )
    }


    // 디비에서 아이템 값을 다시 가져옴. 매개변수로 들어가는 item은 선택된 item이다.
    // repository에서 디비에서 값을 가져올 때 선택된 아이템의 불리언 값만 트루로 세팅해주는데
    // 이때 매개변수로 들어간 아이템과 이름이 같은 걸 찾아주고 그 아이템의 불리언값을 수정해주는 방식이다.
    fun updateSelectedItemOnDTO(item: SanDTO) {
        repository.editSelectedItem(item)
    }
}
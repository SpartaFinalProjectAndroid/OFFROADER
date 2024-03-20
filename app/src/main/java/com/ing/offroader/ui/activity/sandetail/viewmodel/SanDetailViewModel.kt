package com.ing.offroader.ui.activity.sandetail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ing.offroader.data.repository.SanListRepository
import com.ing.offroader.ui.activity.sandetail.SanDetailDTO

private const val TAG = "SanDetailViewModel"

class SanDetailViewModel(sanListRepository: SanListRepository) : ViewModel() {
    private val repo : SanListRepository = sanListRepository

    val info : LiveData<SanDetailDTO> = repo.detailInfo


    fun getSelectedSan(sanName: String?) {
        repo.getSelectedItemFromRepository(sanName)
        Log.d(TAG, "getSelectedSan: $sanName")
    }

    // ---------------------------------- 산 좋아요 기능 구현부 -----------------------------------

    private val _sanLikedList : MutableLiveData<MutableList<String>> = MutableLiveData()
    private val sanLikedCopyList : MutableList<String> = mutableListOf()
    val sanLikedList : LiveData<MutableList<String>> = _sanLikedList

    fun addSanLikedList(name: String) {
        sanLikedCopyList.add(name)
        _sanLikedList.value = sanLikedCopyList
    }

    fun removeSanLikedList(name: String) {
        sanLikedCopyList.remove(name)
        _sanLikedList.value = sanLikedCopyList
    }

}

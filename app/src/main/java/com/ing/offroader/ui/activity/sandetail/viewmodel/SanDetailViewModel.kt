package com.ing.offroader.ui.activity.sandetail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
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

}

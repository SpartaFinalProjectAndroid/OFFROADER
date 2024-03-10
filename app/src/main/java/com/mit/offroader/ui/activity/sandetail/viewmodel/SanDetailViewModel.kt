package com.mit.offroader.ui.activity.sandetail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mit.offroader.ui.activity.sandetail.SanDetailDTO

private const val TAG = "SanDetailViewModel"

class SanDetailViewModel(sanDetailRepository: SanDetailRepository) : ViewModel() {

    private var _sanDetailDTO = MutableLiveData<SanDetailDTO>()
    private val repo : SanDetailRepository = sanDetailRepository

    val sanDetailDTO : LiveData<SanDetailDTO> = _sanDetailDTO

    val info : LiveData<SanDetailDTO> = repo.info


    fun getSelectedSan(sanName: String?) {
        repo.getSelectedItemFromRepository(sanName)
        Log.d(TAG, "getSelectedSan: $sanName")
    }

}

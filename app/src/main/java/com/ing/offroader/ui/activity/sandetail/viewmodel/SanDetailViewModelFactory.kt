package com.ing.offroader.ui.activity.sandetail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ing.offroader.data.repository.SanListRepository

class SanDetailViewModelFactory(private val sanListRepository: SanListRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SanDetailViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return SanDetailViewModel(sanListRepository) as T
        }
        throw IllegalArgumentException("gg")
    }
}
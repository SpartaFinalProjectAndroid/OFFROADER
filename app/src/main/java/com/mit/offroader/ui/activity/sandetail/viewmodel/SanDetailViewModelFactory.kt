package com.mit.offroader.ui.activity.sandetail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SanDetailViewModelFactory(private val sandetailRepository: SanDetailRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SanDetailViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return SanDetailViewModel(sandetailRepository) as T
        }
        throw IllegalArgumentException("gg")
    }
}
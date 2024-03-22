package com.ing.offroader.ui.fragment.mydetail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ing.offroader.data.repository.SanListRepository

class MyDetailViewModelFactory(private val sanListRepository: SanListRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MyDetailViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return MyDetailViewModel(sanListRepository) as T
        }
        throw IllegalArgumentException("gg")
    }
}
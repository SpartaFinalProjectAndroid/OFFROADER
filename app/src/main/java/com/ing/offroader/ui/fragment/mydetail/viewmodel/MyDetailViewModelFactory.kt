package com.ing.offroader.ui.fragment.mydetail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ing.offroader.data.repository.SanListRepository
import com.ing.offroader.ui.activity.add_post.PostRepository

class MyDetailViewModelFactory(private val sanListRepository: SanListRepository, private val postRepository: PostRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MyDetailViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return MyDetailViewModel(sanListRepository, postRepository) as T
        }
        throw IllegalArgumentException("gg")
    }
}
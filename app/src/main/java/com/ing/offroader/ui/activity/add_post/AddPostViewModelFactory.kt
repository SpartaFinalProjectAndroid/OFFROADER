package com.ing.offroader.ui.activity.add_post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddPostViewModelFactory(private val postRepository: PostRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddPostViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddPostViewModel(postRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")    }
}
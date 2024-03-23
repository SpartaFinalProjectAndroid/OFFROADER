package com.ing.offroader.ui.activity.my_post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ing.offroader.ui.activity.add_post.PostRepository

class MyPostViewModelFactory(private val postRepository: PostRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyPostViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyPostViewModel(postRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")    }
}
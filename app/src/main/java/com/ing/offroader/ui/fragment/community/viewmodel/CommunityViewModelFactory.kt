package com.ing.offroader.ui.fragment.community.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ing.offroader.data.repository.SanListRepository
import com.ing.offroader.ui.activity.add_post.PostRepository
import com.ing.offroader.ui.fragment.sanlist.viewmodel.SanListViewModel

class CommunityViewModelFactory(private val postRepository: PostRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommunityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CommunityViewModel(postRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")    }
}
package com.ing.offroader.ui.fragment.community.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ing.offroader.ui.activity.add_post.PostRepository
import kotlinx.coroutines.launch

private const val TAG = "태그 : CommunityViewModel"
class CommunityViewModel: ViewModel() {

    private val postRepository : PostRepository = PostRepository()

    fun setPosts() {
        Log.d(TAG, "setPosts: ")
        viewModelScope.launch {
            Log.d(TAG, "viewModelScope Launcched")
            postRepository.setPost()

        }
    }


}
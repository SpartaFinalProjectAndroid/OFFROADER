package com.ing.offroader.ui.fragment.community.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ing.offroader.ui.activity.add_post.PostRepository
import com.ing.offroader.ui.fragment.community.model.CommunityUiState
import com.ing.offroader.ui.fragment.community.model.PostDTO
import kotlinx.coroutines.launch

private const val TAG = "태그 : CommunityViewModel"
class CommunityViewModel: ViewModel() {

    private val _communityUiState : MutableLiveData<CommunityUiState?> = MutableLiveData()
    val communityUiState : LiveData<CommunityUiState?> = _communityUiState

    private val postRepository : PostRepository = PostRepository()

    val postItems : LiveData<ArrayList<PostDTO?>?> = postRepository.setPostItems

    init {
        setPosts()
    }
    fun updateUiState(postItems: ArrayList<PostDTO?>?) {
        _communityUiState.value = communityUiState.value?.copy(
            postItems = postItems
        )

    }
    fun setPosts() {

        Log.d(TAG, "setPosts: ")
        postRepository.setPost()

    }



}
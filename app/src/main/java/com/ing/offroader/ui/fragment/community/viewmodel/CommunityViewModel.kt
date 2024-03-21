package com.ing.offroader.ui.fragment.community.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ing.offroader.ui.activity.add_post.PostRepository
import com.ing.offroader.ui.fragment.community.model.CommunityUiState
import com.ing.offroader.ui.fragment.community.model.PostDTO

private const val TAG = "태그 : CommunityViewModel"
class CommunityViewModel(postRepository: PostRepository): ViewModel() {

    private val _communityUiState : MutableLiveData<CommunityUiState?> = MutableLiveData()
    val communityUiState : LiveData<CommunityUiState?> = _communityUiState


    val postItems : LiveData<ArrayList<PostDTO?>?> = postRepository.setPostItems

    init {
//        setPosts()
    }
    fun updateUiState(postItems: ArrayList<PostDTO?>?) {
        _communityUiState.value = communityUiState.value?.copy(
            postItems = postItems
        )

    }
//    fun setPosts() {
//
//        Log.d(TAG, "setPosts: ")
//        postRepository.setPost()
//
//    }



}
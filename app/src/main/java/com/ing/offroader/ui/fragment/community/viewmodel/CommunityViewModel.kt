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

    init {
        setPosts()
    }
    fun updateUiState(postItems: ArrayList<PostDTO?>?) {
        _communityUiState.value = communityUiState.value?.copy(
            postItems = postItems
        )

    }
    fun setPosts() {
        var postItems: ArrayList<PostDTO?>? = null

        Log.d(TAG, "setPosts: ")
        viewModelScope.launch {
            Log.d(TAG, "viewModelScope Launched")
            val postDocumentSnapshot = postRepository.setPost()
            if (postDocumentSnapshot.isNullOrEmpty()) {
                Log.e(TAG, "setPosts: 파이어베이스에서 포스트를 받아오지 못함.", )
            } else {
                postItems = arrayListOf()
                postDocumentSnapshot.forEach {
                    val post = it.toObject(PostDTO::class.java)
                    postItems!!.add(post)
                }
                Log.d(TAG, "setPosts: postItems : $postItems")
            }
            if(postItems != null) {
//                updateUiState
                _communityUiState.value = CommunityUiState(postItems = postItems)
                Log.d(TAG, "setPosts: ${communityUiState.value}")
            }

        }

    }



}
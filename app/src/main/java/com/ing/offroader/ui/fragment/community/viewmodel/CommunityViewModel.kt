package com.ing.offroader.ui.fragment.community.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ing.offroader.ui.activity.add_post.PostRepository
import com.ing.offroader.ui.fragment.community.model.PostDTO

private const val TAG = "태그 : CommunityViewModel"
class CommunityViewModel(private val postRepository: PostRepository): ViewModel() {
    fun deletePost(item: PostDTO?) {
        postRepository.deletePost(item)
    }

    val postItems : LiveData<ArrayList<PostDTO?>?> = postRepository.setPostItems

}
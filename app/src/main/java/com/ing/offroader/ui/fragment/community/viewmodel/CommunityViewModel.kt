package com.ing.offroader.ui.fragment.community.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ing.offroader.ui.activity.add_post.PostRepository
import com.ing.offroader.ui.fragment.community.model.PostDTO

private const val TAG = "태그 : CommunityViewModel"
class CommunityViewModel(postRepository: PostRepository): ViewModel() {

    val postItems : LiveData<ArrayList<PostDTO?>?> = postRepository.setPostItems

}
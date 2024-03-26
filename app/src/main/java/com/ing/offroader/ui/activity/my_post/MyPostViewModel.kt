package com.ing.offroader.ui.activity.my_post

import androidx.lifecycle.ViewModel
import com.ing.offroader.data.model.addpost.PostModel
import com.ing.offroader.ui.activity.add_post.PostRepository

private const val TAG = "태그 : MyPostViewModel"
class MyPostViewModel(private val postRepository: PostRepository): ViewModel() {
    fun deletePost(item: PostModel?) {
        postRepository.deletePost(item?.postId.toString(), rootPage = "MyPostActivity")
    }

}
package com.ing.offroader.ui.activity.add_post

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AddPostViewModel : ViewModel() {

    private val _addPostUiState: MutableLiveData<AddPostUiState?> = MutableLiveData()
    val addPostUiState: LiveData<AddPostUiState?> = _addPostUiState

    private val postRepository: PostRepository = PostRepository()

    init {
        _addPostUiState.value = AddPostUiState(
            title = null,
            content = null,
            image = null,
            errorMessage = "",
            cycle = true
        )
    }

    fun titleChangedListener(title: String) {
        _addPostUiState.value = addPostUiState.value?.copy(
            title = title
        )
    }

    fun contentChangedListener(content: String) {
        _addPostUiState.value = addPostUiState.value?.copy(
            content = content
        )
    }

    fun setOnCompleteButton() {
        if (addPostUiState.value?.title.isNullOrBlank()) {
            // TODO : 토스트 메세지 띄워주기
            _addPostUiState.value = addPostUiState.value?.copy(
                errorMessage = "제목을 입력해 주셔야 합니다."
            )

        } else if (addPostUiState.value?.image == null) {
            // TODO : 토스트 메세지 띄워주기
            _addPostUiState.value = addPostUiState.value?.copy(
                errorMessage = "이미지를 선택해 주세요."
            )
        } else {
            // TODO : 디비에 저장하기 & 포스팅 액티비티 종료시키기
            viewModelScope.launch {
                var successfullyAdded = postRepository.addPost(
                    addPostUiState.value?.title!!,
                    addPostUiState.value?.content,
                    addPostUiState.value?.image
                )
            }


            _addPostUiState.value = addPostUiState.value?.copy(
                errorMessage = null,
                cycle = false
            )
        }
    }

    //Firebase에 전달하기 위해 Act에서 img를 전달받고 addPostUiState 추가
    fun addImageUri(img : ByteArray) {
        _addPostUiState.value = addPostUiState.value?.copy(
            image = img
        )
    }
}

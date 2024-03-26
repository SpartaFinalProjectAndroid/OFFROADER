package com.ing.offroader.ui.fragment.mydetail.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ing.offroader.data.model.addpost.PostModel
import com.ing.offroader.data.model.userInfo.UserData
import com.ing.offroader.data.repository.SanListRepository
import com.ing.offroader.data.repository.UserRepository
import com.ing.offroader.ui.activity.add_post.PostRepository
import com.ing.offroader.ui.fragment.community.model.PostDTO
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

private const val TAG = "태그 : MyDetailViewModel"

class MyDetailViewModel(
    sanListRepository: SanListRepository,
    private val postRepository: PostRepository
) : ViewModel() {
    private val repo: SanListRepository = sanListRepository

//    val myDetailDTO : LiveData<MyDetailDTO> = repo.myInfo
    private val user = FirebaseAuth.getInstance().currentUser


    // ----------------------------- 업적 레벨 관련 기능들 --------------------------------------


    private val _userData: MutableLiveData<UserData> = MutableLiveData()
    val userData: LiveData<UserData> = _userData


    private val userRepository: UserRepository = UserRepository()

    private val userInfo : LiveData<UserData?>? = postRepository.userInfo

    // 내 게시물 가져오기 :
    val myPostItems : LiveData<ArrayList<PostModel?>?> = postRepository.myPostItems

    private val _myPostLists: MutableLiveData<ArrayList<PostModel?>?> = MutableLiveData()
    val myPostLists: LiveData<ArrayList<PostModel?>?> = _myPostLists

//    init {
//        setMyPostLists()
//    }

//    fun setMyPostLists() {
//        val myPostArray: ArrayList<PostModel?> = arrayListOf()
//        postRepository.myPostItems.value?.forEach {
//            val post = PostModel(
//                userName = user?.displayName,
//                userProfileImage = user?.photoUrl,
//                contents = it?.contents.toString(),
//                images = it?.images,
//                like = it?.like.toString().toInt(),
//                postId = it?.post_id.toString(),
//                san = it?.san.toString(),
//                title = it?.title.toString(),
//                uid = it?.uid.toString(),
//                upload_date = it?.upload_date.toString().toLong()
//            )
//            myPostArray.add(post)
//        }
//        _myPostLists.value = myPostArray
//    }


    fun setRepository() {
        Log.d(TAG, "setRepository: ")
        postRepository.setMyPost()
    }


    // UID를 받아와서 UserRepository의 getUserData 함수를 호출 하여
    // 파이어스토에서 해당 UID에 맞는 데이터를 가져와서 _userData LiveData에 저장한다.
    fun getUserData(userUID: String) {
        viewModelScope.launch {
            val timeCheck = measureTimeMillis {
                _userData.value = userRepository.getUserData(userUID = userUID)
                Log.d("민용뷰모델", "name: ${userData.value}")
            }

            Log.d("민용타임", "time: $timeCheck")
        }
    }

    // -------------------------------- 좋아요 관련 기능들 --------------------------------------

    private val _sanLikedList: MutableLiveData<MutableList<String>> = MutableLiveData()
    val sanLikedList: LiveData<MutableList<String>> = _sanLikedList

    private var sanLikedCopyList: MutableList<String> = mutableListOf()

    fun loadSanLikedList(data: MutableList<String>) {
        sanLikedCopyList = data
        _sanLikedList.value = sanLikedCopyList
    }

    fun getLikedSanName() {
//        repo.getLikedItemFromRepository(sanLikedCopyList)
    }
}
package com.ing.offroader.ui.activity.add_post

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ing.offroader.ui.fragment.community.model.PostDTO
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class PostRepository {
    private val TAG = "태그 : AddPostRepository"

    private var user = FirebaseAuth.getInstance().currentUser
    private val db = FirebaseFirestore.getInstance()

    // setPostItems
    private val _setPostItems: MutableLiveData<ArrayList<PostDTO?>?> = MutableLiveData()
    val setPostItems: LiveData<ArrayList<PostDTO?>?> = _setPostItems

    /** 사진 전송 관련 */
    private val specifiedStorage = Firebase.storage("gs://offroader-event.appspot.com")

    init {
        setPost()
    }

    fun setPost() {
        Log.d(TAG, "setPost: ")

        try {
            var postItems: ArrayList<PostDTO?> = arrayListOf()
            db.collection("Community").orderBy("upload_date", Query.Direction.DESCENDING).get().addOnSuccessListener { it ->
                it.documents.forEach {
                    val post = it.toObject(PostDTO::class.java)
                    postItems.add(post)
                }
                Log.d(
                    TAG,
                    "setPosts: postItems : $postItems"
                )
                //                updateUiState

                val items = postItems
                _setPostItems.value = items
                Log.d(TAG, "setPosts: ${setPostItems.value}")
            }
        }catch (e: Exception) {
            Log.e(TAG, "FireStore Error: $e")
        }

    }


    fun addPost(title: String, content: String?, image: ByteArray?) {
        Log.d(TAG, "addPost uid: ${user?.uid}")


        val postId = UUID.randomUUID().toString()

        // 이미지를 저장할 위치를 지정
        val storageRef =
            specifiedStorage.reference.child("Offroader_res/post_image/${postId}.jpg")

        //putBytes 함수를 사용해 전달받은 이미지를 firebase에 업로드
        val uploadTask = storageRef.putBytes(image!!)
        //성공,실패 여부 확인 로그
        uploadTask.addOnFailureListener {
            Log.d(TAG, "사진 업로드 성공여부: 실패")
        }.addOnSuccessListener { taskSnapshot ->
            Log.d(TAG, "사진 업로드 성공여부: 성공")

        }.addOnCompleteListener {
            setPost()
        }

        val date = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        val dateToLong = date.format(formatter).toLong()

        Log.d(TAG, "addPost: $dateToLong")

        val newPost = hashMapOf(
            "uid" to user!!.uid,
            "title" to title,
            "contents" to content,
            "like" to 0,
            "post_id" to postId,
            "san" to null,
            "upload_date" to dateToLong
        )

        try {
            db.collection("Community").document(postId).set(newPost).addOnSuccessListener {
                // 온 석세스가 되었을 때 디비에서 값을 다시 받아와서 옵져빙 해줄 수 있도록 해주기
                Log.d(TAG, "addPost: 게시물 디비에 저장하기 success 이제 setPost 함수 실행")

            }.addOnFailureListener {
                Log.d(TAG, "addPost: 게시물 올리기 Fail !")
            }.addOnCompleteListener {
                Log.d(TAG, "addPost: 온콤플릿 리스너는 업로드 실행이 끝날때까지 기다렸다가 실행되는건감?")
                // 아니네 전에 실행되네...

            }

        } catch (e: Exception) {
            Log.e(TAG, "FireStore Error: $e")
        }
    }

}

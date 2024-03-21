package com.ing.offroader.ui.activity.add_post

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ing.offroader.ui.fragment.community.model.CommunityUiState
import com.ing.offroader.ui.fragment.community.model.PostDTO
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class PostRepository {
    private val TAG = "태그 : AddPostRepository"

    private var user = FirebaseAuth.getInstance().currentUser
    private val db = FirebaseFirestore.getInstance()

    // setPostItems
    private val _setPostItems : MutableLiveData<ArrayList<PostDTO?>?> = MutableLiveData()
    val setPostItems : LiveData<ArrayList<PostDTO?>?> = _setPostItems

    /** 사진 전송 관련 */
    private val specifiedStorage = Firebase.storage("gs://offroader-event.appspot.com")

    fun setPost() {
        Log.d(TAG, "setPost: ")
        var postItems: ArrayList<PostDTO?>? = null
        try {
            postItems = arrayListOf()
            db.collection("Community").orderBy("upload_date").get().addOnSuccessListener {
                it.documents.forEach {
                    val post = it.toObject(PostDTO::class.java)
                    postItems!!.add(post)
                }
                Log.d(
                    TAG,
                    "setPosts: postItems : $postItems"
                )
                if (postItems != null) {
//                updateUiState

                    _setPostItems.value = postItems
                    Log.d(TAG, "setPosts: ${setPostItems.value}")
                } else {
                    Log.d(TAG, "setPosts: post아이템이 널이라서 .. 추가가 안됨.")
                }


            }
        } catch (e: Exception) {
            Log.e(TAG, "FireStore Error: $e")
            null
        }

    }


    suspend fun addPost(title: String, content: String?, image: ByteArray?): Boolean {
        Log.d(TAG, "addPost uid: ${user?.uid}")


        val postId = UUID.randomUUID().toString()

        // 이미지를 저장할 위치를 지정
        val storageRef =
            specifiedStorage.reference.child("Offroader_res/post_image/${postId}.jpg")

        //putBytes 함수를 사용해 전달받은 이미지를 firebase에 업로드
        val uploadTask = storageRef.putBytes(image!!)
        //성공,실패 여부 확인 로그
        uploadTask.addOnFailureListener {
            Log.d(TAG, "테스트: 실패")
        }.addOnSuccessListener { taskSnapshot ->
            Log.d(TAG, "테스트: 성공")
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

        return try {
            db.collection("Community").document(postId).set(newPost).await()
            true

        } catch (e: Exception) {
            Log.e(TAG, "FireStore Error: $e")
            false
        }
    }


//    suspend fun setPost(): MutableList<DocumentSnapshot>? {
//        Log.d(TAG, "setPost: ")
//        return try {
//            db.collection("Community").orderBy("upload_date").get().await().documents
//
//        } catch (e: Exception) {
//            Log.e(TAG, "FireStore Error: $e")
//            null
//        }
//    }
//
//
//    suspend fun addPost(title: String, content: String?, image: ByteArray?) : Boolean {
//        Log.d(TAG, "addPost uid: ${user?.uid}")
//
//
//        val postId = UUID.randomUUID().toString()
//
//        // 이미지를 저장할 위치를 지정
//        val storageRef =
//            specifiedStorage.reference.child("Offroader_res/post_image/${postId}.jpg")
//
//        //putBytes 함수를 사용해 전달받은 이미지를 firebase에 업로드
//        val uploadTask = storageRef.putBytes(image!!)
//        //성공,실패 여부 확인 로그
//        uploadTask.addOnFailureListener {
//            Log.d(TAG, "테스트: 실패")
//        }.addOnSuccessListener { taskSnapshot ->
//            Log.d(TAG, "테스트: 성공")
//        }
//
//        val date = LocalDateTime.now()
//        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
//        val dateToLong = date.format(formatter).toLong()
//
//        Log.d(TAG, "addPost: $dateToLong")
//
//        val newPost = hashMapOf(
//            "uid" to user!!.uid,
//            "title" to title,
//            "contents" to content,
//            "like" to 0,
//            "post_id" to postId,
//            "san" to null,
//            "upload_date" to dateToLong
//        )
//
//        return try {
//            db.collection("Community").document(postId).set(newPost).await()
//            true
//
//        } catch (e: Exception) {
//            Log.e(TAG, "FireStore Error: $e")
//            false
//        }
//    }
}

package com.ing.offroader.ui.activity.add_post

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ing.offroader.ui.fragment.community.model.PostDTO
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class PostRepository {
    private val TAG = "태그 : AddPostRepository"

    private var user = FirebaseAuth.getInstance().currentUser
    private val db = FirebaseFirestore.getInstance()

    /** 사진 전송 관련 */
    private val specifiedStorage = Firebase.storage("gs://offroader-event.appspot.com")

    suspend fun setPost() {
        Log.d(TAG, "setPost: ")
        try {
            val postArray: ArrayList<PostDTO?> = arrayListOf()
            val testtt = db.collection("Community").orderBy("upload_date").get().await()
            Log.d(TAG, "setPost: $testtt")
            testtt.documents.forEach {
                val post = it.toObject(PostDTO::class.java)
                postArray.add(post)
                Log.d(TAG, "setPost: $postArray")
            }

        } catch (e: Exception) {
            Log.e(TAG, "FireStore Error: $e")
        }
    }

    suspend fun addPost(title: String, content: String?, image: ByteArray?) {

        try {
            Log.d(TAG, "addPost uid: ${user?.uid}")


            val postId = UUID.randomUUID().toString()

            // 이미지를 저장할 위치를 지정
            val storageRef = specifiedStorage.reference.child("Offroader_res/post_image/${postId}.jpg")

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

            db.collection("Community").document(postId).set(newPost).await()

        } catch (e: Exception) {
            Log.e(TAG, "FireStore Error: $e")
        }
    }


}
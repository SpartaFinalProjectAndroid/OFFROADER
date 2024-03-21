package com.ing.offroader.ui.activity.add_post

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.ing.offroader.ui.fragment.community.model.PostDTO
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class PostRepository {
    private val TAG = "태그 : AddPostRepository"

    private var user = FirebaseAuth.getInstance().currentUser
    private val db = FirebaseFirestore.getInstance()

    suspend fun setPost(): MutableList<DocumentSnapshot>? {
        Log.d(TAG, "setPost: ")
        return try {
            db.collection("Community").orderBy("upload_date").get().await().documents
//            return try {
//                var postArray: ArrayList<PostDTO>? = null
//                db.collection("Community").orderBy("upload_date").get().await().documents.forEach {
//                    postArray = arrayListOf()
//                    val post = it.toObject(PostDTO::class.java)
//                    if (post != null) {
//                        postArray!!.add(post)
//                    } else {
//                        Log.e(TAG, "setPost: 디비에서 아무런 데이터도 넘어오지 않음 !!")
//                    }
//                    Log.d(TAG, "setPost: <포스트어레이> $postArray")
//
//                }
//                return postArray




//            val testtt = db.collection("Community").orderBy("upload_date").get().await()
//            Log.d(TAG, "setPost: db에서 가져오기 <쿼리스냅샷> $testtt")
//            testtt.documents.forEach {
//                val post = it.toObject(PostDTO::class.java)
//                if (post != null) {
//                    postArray.add(post)
//                } else {
//                    Log.e(TAG, "setPost: 디비에서 아무런 데이터도 넘어오지 않음 !!")
//                }
//                Log.d(TAG, "setPost: <포스트어레이> $postArray")
//            }
//            postArray

        } catch (e: Exception) {
            Log.e(TAG, "FireStore Error: $e")
            null
        }

    }


    suspend fun addPost(title: String, content: String?, image: String?) {

        try {
            Log.d(TAG, "addPost uid: ${user?.uid}")


            val postId = UUID.randomUUID().toString()
            val date = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
            val dateToLong = date.format(formatter).toLong()

            Log.d(TAG, "addPost: $dateToLong")

            val newPost = hashMapOf(
                "uid" to user!!.uid,
                "title" to title,
                "contents" to content,
                "images" to image,
                "like" to null,
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


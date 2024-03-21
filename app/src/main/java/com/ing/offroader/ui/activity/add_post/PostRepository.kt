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


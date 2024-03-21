package com.ing.offroader.ui.activity.add_post

import android.util.Log
import androidx.core.text.isDigitsOnly
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class PostRepository {
    private val TAG = "태그 : AddPostRepository"

    private var user = FirebaseAuth.getInstance().currentUser
    private val db = FirebaseFirestore.getInstance()

    suspend fun setPost() {
        try {
            val testtt = db.collection("Community").orderBy("upload_date").get().await()
            
            testtt.documents.forEach{
                Log.d(TAG, "setPost: ${it}")
            }

        } catch (e: Exception) {
            Log.e(TAG, "FireStore Error: $e")
        }
    }

    suspend fun addPost(title: String, content: String?, image: String?) {

        try {
            Log.d(TAG, "addPost uid: ${user?.uid}")
            val postId = UUID.randomUUID().toString()

            val newPost = hashMapOf(
                "uid" to user!!.uid,
                "title" to title,
                "contents" to content,
                "images" to image,
                "like" to null,
                "post_id" to postId,
                "san" to null,
                "upload_date" to LocalDateTime.now()
            )

            db.collection("Community").document(postId).set(newPost).await()

        } catch (e: Exception) {
            Log.e(TAG, "FireStore Error: $e")
        }


    }
}
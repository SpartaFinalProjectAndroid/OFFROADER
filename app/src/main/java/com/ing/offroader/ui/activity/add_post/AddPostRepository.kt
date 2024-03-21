package com.ing.offroader.ui.activity.add_post

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.util.UUID

class AddPostRepository {
    private val TAG = "태그 : AddPostRepository"

    private var user = FirebaseAuth.getInstance().currentUser
    private val db = FirebaseFirestore.getInstance()

    suspend fun addPost(title: String, content: String?, image: String?) {
        try {
            Log.d(TAG, "addPost uid: ${user?.uid}")
            val postId = UUID.randomUUID().toString()

            val newPost = hashMapOf(
                "title" to title,
                "contents" to content,
                "images" to image,
                "like" to null,
                "post_id" to postId,
                "san" to null,
                "upload_date" to LocalDate.now().toString()
            )

            db.collection("User").document(user!!.uid).collection("Community").document(postId)
                .set(newPost).await()

        } catch (e: Exception) {
            Log.e(TAG, "FireStore Error: $e")
        }


    }
}
package com.ing.offroader.data.repository

import android.util.Log
import androidx.room.util.copy
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.ing.offroader.data.model.userInfo.Achievements
import com.ing.offroader.data.model.userInfo.Attendance
import com.ing.offroader.data.model.userInfo.Post
import com.ing.offroader.data.model.userInfo.SanID
import com.ing.offroader.data.model.userInfo.UserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait
import kotlin.system.measureTimeMillis


class UserRepository {

    private val TAG = "민용 레포지토리"

    suspend fun getUserData(userUID: String) : UserData {

        return try {
            val userData = FirebaseFirestore.getInstance().collection("User").document(userUID)

            val userAchievements = userData.collection("Achievements")
            val sanID = userAchievements.document("san_id").get().await().toObject(SanID::class.java)
            val attendance = userAchievements.document("attendance").get().await().toObject(Attendance::class.java)

            val userCommunity = userData.collection("Community")
            val post = userCommunity.document("post").get().await().toObject(Post::class.java)

            val userDataClass: UserData? = userData.get().await().toObject(UserData::class.java)
            userDataClass?.achievements = Achievements(sanID, attendance)
            userDataClass?.community = post

            if (userDataClass == null) return UserData()
            userDataClass

        } catch (e: Exception) {
            Log.e(TAG, "FireStore Error: $e")
            return UserData()
        }
    }
}
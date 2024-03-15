package com.ing.offroader.data.repository

import android.util.Log
import androidx.room.util.copy
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
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

    data class UserData(
        var userName : String ?= null,
        var userEmail : String ?= null,
        var userAge : String ?= null,
        var achievements : Achievements ?= null,
        var community : Post ?= null,
    )

    data class Achievements(
        var sanId: SanID ?= null,
        var attendance: Attendance ?= null,
    )

    data class SanID(
        var climb : Int?,
        var distance : Int?,
        var badgeTotal : Map<*, *>?
    )

    data class Attendance(
        var attendanceRecord : MutableList<*>?,
    )

    data class Post(
        var title : String?,
        var postId : String?,
        var san : String?,
        var uploadDate : String?,
        var contents : String?,
        var images : MutableList<*>?,
        var like : MutableList<*>?
    )


    suspend fun getUserData(userUID: String) : UserData {

        val userDataClass = UserData()

        val userData = FirebaseFirestore.getInstance().collection("User").document(userUID)

        val userAchievements = userData.collection("Achievements")
        val sanID = userAchievements.document("san_id").get()
        val attendance = userAchievements.document("attendance ").get()

        val userCommunity = userData.collection("Community")
        val post = userCommunity.document("post").get()


        userData.get().addOnSuccessListener {

            userDataClass.userName = it.get("user_name").toString()
            userDataClass.userEmail = it.get("user_email").toString()
            userDataClass.userAge = it.get("user_age").toString()

            //Log.d(TAG, "name: $testName")
        }.addOnFailureListener {
            //Log.d(TAG, "getUserData: Fail")
        }.await()




        sanID.addOnSuccessListener {
            //it.get("climb").toString().toInt()
            userDataClass.achievements?.sanId?.climb = it.get("climb").toString().toInt()
            it.get("distance").toString().toInt()
            it.get("badge_total") as Map<*, *>


        }.addOnFailureListener {

        }.await()


        attendance.addOnSuccessListener {
            //val test2 = Attendance(it.get("attendance_record_2024") as MutableList<*>)
            //attendanceCopy = test2.copy()
            //Log.d(TAG, "getUserData: " + userDataClass.achievements?.attendance?.attendanceRecord)
        }
        post.addOnSuccessListener {

            val test3 = Post(
                it.get("title").toString(),
                it.get("post_id").toString(),
                it.get("san").toString(),
                it.get("upload_date").toString(),
                it.get("contents").toString(),
                it.get("images") as MutableList<*>,
                it.get("like") as MutableList<*>,
            )


            //Log.d(TAG, "getUserData: " + userDataClass.community)
        }.await()

        Thread.sleep(1000)
        Log.d(TAG, "name: " + userDataClass.achievements?.sanId?.climb)

        return userDataClass
    }
}
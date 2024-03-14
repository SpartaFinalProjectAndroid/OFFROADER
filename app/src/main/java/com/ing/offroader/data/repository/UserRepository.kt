package com.ing.offroader.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore


class UserRepository {

    private val TAG = "민용레포지토리"

    data class UserDataTest(
        var userID: String ?= null,
        var userLevel: Int ?= null,
        var userName: String ?= null,
        var achievements: Achievements = Achievements()
    )

    data class Achievements(
        var attendance: Attendance ?= null,
        var san: AchieveSanList ?= null
    )

    data class Attendance(
        var total: Int,
    )

    data class AchieveSanList(
        var sanList: MutableList<SanMap>
    )

    data class SanMap(
        var sanMap: Map<String, Int>
    )

    fun getUserData(userID: String) : UserDataTest {

        var userTest  = UserDataTest()

        var test = FirebaseFirestore.getInstance().collection("User").document(userID)

        test.get().addOnSuccessListener {

            userTest.userID = it.id
            userTest.userLevel = it.data?.get("level").toString().toInt()
            userTest.userName = it.data?.get("name").toString()

            test.collection("achievements").document("attendance").get()
                .addOnSuccessListener { it2 ->
                    userTest.achievements.attendance =
                        Attendance(it2.data?.get("total").toString().toInt())
                    //userTest.achievements?.san?.sanList = SanMap(it2.data.get(""))
                    Log.d(TAG, "getUserData: " + userTest.achievements.attendance)
                }

            test.collection("achievements").document("san").get()
                .addOnSuccessListener { it3 ->
                    //userTest.achievements.attendance = Attendance(it2.data?.get("total").toString().toInt())
                    //userTest.achievements?.san?.sanList = SanMap(it2.data.get(""))

                    Log.d(TAG, "getUserData: " + it3.data)

                    // climb, distance
                }
        }
        return userTest
    }

}
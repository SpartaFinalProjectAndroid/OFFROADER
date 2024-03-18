package com.ing.offroader.ui.activity.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlin.math.log

private const val TAG = "태그 : AuthRepository"


class AuthRepository {
    

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val rootRef: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val usersRef: CollectionReference = rootRef.collection("user")

    // Sign in using google
    fun firebaseSignInWithGoogle(googleAuthCredential: AuthCredential): MutableLiveData<ResponseState<LoginUser>> {
        val authenticatedUserMutableLiveData: MutableLiveData<ResponseState<LoginUser>> =
            MutableLiveData()
        Log.d(TAG, "firebaseSignInWithGoogle: repository start")
        firebaseAuth.signInWithCredential(googleAuthCredential).addOnCompleteListener { authTask ->
            if (authTask.isSuccessful) {
                Log.d(TAG, "firebaseSignInWithGoogle: task is 성공적")
                var isNewUser = authTask.result?.additionalUserInfo?.isNewUser
                val firebaseUser: FirebaseUser? = firebaseAuth.currentUser

                if (firebaseUser != null) {
                    Log.d(TAG, "firebaseSignInWithGoogle: FirebaseUser is not null")
                    val uid = firebaseUser.uid
                    val name = firebaseUser.displayName
                    val email = firebaseUser.email
                    val user = LoginUser(uid = uid, name = name, email = email)
                    user.isNew = isNewUser
                    authenticatedUserMutableLiveData.value = ResponseState.Success(user)

                }


            } else {
                Log.d(TAG, "firebaseSignInWithGoogle: UNSUCCESSFUL")

                authenticatedUserMutableLiveData.value = authTask.exception?.message?.let {
                    ResponseState.Error(it)
                }

            }


        }
        return authenticatedUserMutableLiveData
    }

}
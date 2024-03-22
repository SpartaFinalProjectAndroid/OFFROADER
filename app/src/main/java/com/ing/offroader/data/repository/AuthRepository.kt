package com.ing.offroader.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.ing.offroader.ui.activity.login.LoginUser
import com.ing.offroader.ui.activity.login.ResponseState

private const val TAG = "태그 : AuthRepository"

class AuthRepository {


    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val rootRef: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val usersRef: CollectionReference = rootRef.collection("user")

    private val db = FirebaseFirestore.getInstance()

    private val _authenticatedUserMutableLiveData: MutableLiveData<ResponseState<LoginUser>> = MutableLiveData()
    val authenticatedUserMutableLiveData: LiveData<ResponseState<LoginUser>> = _authenticatedUserMutableLiveData


    // Sign in using google
    fun firebaseSignInWithGoogle(googleAuthCredential: AuthCredential): MutableLiveData<ResponseState<LoginUser>> {


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
                    _authenticatedUserMutableLiveData.value = ResponseState.Success(user)

                    Log.d(
                        TAG,
                        "firebaseSignInWithGoogle: ${authenticatedUserMutableLiveData.value}"
                    )

                    val userDatabaseStructure = hashMapOf(
                        "user_age" to 0,
                        "user_email" to email,
                        "user_name" to "은이",
                    )

                    db.collection("User").document(uid).set(userDatabaseStructure)
                }


            } else {
                Log.d(TAG, "firebaseSignInWithGoogle: UNSUCCESSFUL")

                _authenticatedUserMutableLiveData.value = authTask.exception?.message?.let {
                    ResponseState.Error(it)
                }

            }


        }
        return _authenticatedUserMutableLiveData
    }

}
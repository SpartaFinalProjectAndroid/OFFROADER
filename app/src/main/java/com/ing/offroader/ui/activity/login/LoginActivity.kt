package com.ing.offroader.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.ing.offroader.R
import com.ing.offroader.databinding.ActivityLoginBinding
import com.ing.offroader.ui.fragment.community.MyApplication

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory((this.application as MyApplication).authRepository)
    }

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate: start")
        initGoogleSignInClient()
        signInUsingGoolge()

    }

    private fun initGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        Log.d(TAG, "initGoogleSignInClient: ")
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signInUsingGoolge() {
        val signInGoogleIntent = this.googleSignInClient.signInIntent
        startActivityForResult(signInGoogleIntent, RC_SIGN_IN)
        Log.d(TAG, "signInUsingGoolge: send Intent")
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: start")

        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult: request code is RC_SIGN_IN")
            if (data != null) {
                Log.d(TAG, "onActivityResult: DATA is NOT NULL")
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "onActivityResult: Successfully got account $account")
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    Log.d(
                        TAG,
                        "onActivityResult: account is not null so it is sent to getGoogleAuthCredential"
                    )
                    getGoogleAuthCredential(account)


                } catch (e: ApiException) {
                    Log.e(TAG, "onActivityResult: $e")
                }
            }

        }
    }

    private fun getGoogleAuthCredential(account: GoogleSignInAccount) {
//        binding.progressBar.visible()
        Log.d(TAG, "getGoogleAuthCredential: start")
        val googleTokeId = account.idToken
        Log.d(TAG, "getGoogleAuthCredential: $googleTokeId")
        val credential = GoogleAuthProvider.getCredential(googleTokeId, null)
        Log.d(TAG, "getGoogleAuthCredential: $credential")
        signInWithGoogleAuthCredential(credential)
    }

    private fun signInWithGoogleAuthCredential(googleAuthCredential: AuthCredential) {

        Log.d(TAG, "signInWithGoogleAuthCredential: Before sending it to viewmode")
        loginViewModel.signInWithGoogle(googleAuthCredential)
        loginViewModel.authenticateUserLiveData.observe(this) { authenticatedUser ->
            Log.d(TAG, "signInWithGoogleAuthCredential: 옵져빙 오예 $authenticatedUser")
            when (authenticatedUser) {
                is ResponseState.Error -> {
                    Log.d(TAG, "signInWithGoogleAuthCredential: ResponseState is Error")
                    authenticatedUser.message?.let {
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    }
                }

                is ResponseState.Loading -> {
                    Log.d(TAG, "signInWithGoogleAuthCredential: ResponseState is Loading")
                }

                is ResponseState.Success -> {
                    Log.d(TAG, "signInWithGoogleAuthCredential: ResponseState is Success")

                }

            }
            finish()
        }

    }

    companion object {
        private const val TAG = "태그 : LoginActivity"
        private const val RC_SIGN_IN = 9001
    }

}
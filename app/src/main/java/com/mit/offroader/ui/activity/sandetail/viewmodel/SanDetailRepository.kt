package com.mit.offroader.ui.activity.sandetail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.mit.offroader.ui.activity.sandetail.SanDetailUiState

private const val TAG = "SanDetailRepository"
class SanDetailRepository {
    private val firestore = FirebaseFirestore.getInstance()

    private var _recItems: MutableLiveData<ArrayList<SanDetailUiState>> = MutableLiveData()
    val recItems: LiveData<ArrayList<SanDetailUiState>> get() = _recItems

    init {
        Log.d(TAG, "init Repository")
        initSanData()
    }

    private fun initSanData() {
        firestore.collection("sanlist")
            .get()
            .addOnSuccessListener { documents ->
                val sanInfo: ArrayList<SanDetailUiState> = arrayListOf()

                documents.forEach { document ->
                    val sanlist = SanDetailUiState(
                        document.getString("name") ?: "none",
                        document.getString("address") ?: "none",
                        document.getLong("difficulty") ?: 0,
                        document.getDouble("height") ?: 0.0,
                        document.getLong("time_uphill") ?: 0,
                        document.getLong("time_downhill") ?: 0,
                        document.getString("summary") ?: "none",
                        document.getString("recommend") ?: "none",
                        document.getBoolean("isLiked") ?: false
                    )
                    sanInfo.add(sanlist)
                }

                _recItems.value = sanInfo
                Log.d(TAG, "${recItems.value?.size}")

            }
            .addOnFailureListener { exception ->
                Log.d("fireTest", "Firebase Error", exception)
            }
    }

}

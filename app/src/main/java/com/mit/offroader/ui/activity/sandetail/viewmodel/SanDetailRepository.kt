package com.mit.offroader.ui.activity.sandetail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.mit.offroader.ui.activity.sandetail.SanDetailDTO

private const val TAG = "SanDetailRepository"
class SanDetailRepository {
    private val firestore = FirebaseFirestore.getInstance()

    private var _info: MutableLiveData<SanDetailDTO> = MutableLiveData()
    val info: LiveData<SanDetailDTO> get() = _info


    private fun initSanData(sanName: String?) {
        firestore.collection("sanlist")
            .get()
            .addOnSuccessListener { documents ->
                val sanInfo: ArrayList<SanDetailDTO> = arrayListOf()

                documents.forEach { document ->
                    val sanList = SanDetailDTO(
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
                    if (sanList.mountain == sanName) {
                        _info.value = sanList
                        Log.d(TAG, "initSanData: $sanList -> ${info.value}")
                    }
                }

//                _info.value = sanInfo
//                Log.d(TAG, "${info.value?.size}")

            }
            .addOnFailureListener { exception ->
                Log.d("fireTest", "Firebase Error", exception)
            }
    }

    fun getSelectedItemFromRepository(sanName: String?) {
        initSanData(sanName)
        Log.d(TAG, "getSelectedItemFromRepository: $sanName")
    }

}

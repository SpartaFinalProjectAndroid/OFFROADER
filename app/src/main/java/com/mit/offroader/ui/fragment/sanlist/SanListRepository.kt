package com.mit.offroader.ui.fragment.sanlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class SanListRepository {

    companion object {
        private const val TAG = "NameRepository"
    }

    // db 인스턴스 생성
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private var _sanListDTO: MutableLiveData<List<SanDTO>> = MutableLiveData<List<SanDTO>>()
    val sanListDTO: LiveData<List<SanDTO>> get() = _sanListDTO


    init {
        initPush()
    }


    private fun initPush() {
        val sanArrayList: ArrayList<SanDTO> = arrayListOf()
        db.collection("sanlist").get().addOnSuccessListener { documents ->
            for (document in documents) {
                sanArrayList.add(
                    SanDTO(
                        name = document.getString("name") ?: "오류",
                        address = document.getString("address") ?: "오류",
                        difficulty = document.getLong("difficulty") ?: 0,
                        height = document.getLong("height") ?: 0,
                        timeUphill = document.getLong("time_uphill") ?: 0,
                        timeDownhill = document.getLong("time_downhill") ?: 0,
                        summary = document.getString("summary") ?: "오류",
                        recommendation = document.getString("recommend") ?: "오류"
                    )
                )

            }
            _sanListDTO.value = sanArrayList
        }

    }

}
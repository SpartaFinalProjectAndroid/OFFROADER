package com.mit.offroader.ui.fragment.sanlist.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore

class SanListRepository {

    companion object {
        private const val TAG = "SanListRepository"
    }

    // db 인스턴스 생성
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private var _sanListDTO: MutableLiveData<List<SanDTO>> =
        MutableLiveData<List<SanDTO>>()
    val sanListDTO: LiveData<List<SanDTO>> get() = _sanListDTO


    init {
        Log.d(TAG,"init 함수")
        initPush()
    }

    /**
     * Database field 참고용 :
     * //data class SanDTO(
     * //    val name: String?,
     * //    val address: String?,
     * //    val difficulty: Long?,
     * //    val height: Long?,
     * //    val timeUphill: Long?,
     * //    val timeDownhill: Long?,
     * //    val summary: String?,
     * //    val recommendation: String?
     * //)
     */

    private fun initPush() {

        Log.d(TAG,"initPush 함수 여기에서 데이터베이스에 있는 값 접근")
        val sanArrayList: ArrayList<SanDTO> = arrayListOf()
        db.collection("sanlist").get().addOnSuccessListener { documents ->
            for (document in documents) {
                sanArrayList.add(
                    SanDTO(
                        sanImage = null,
                        sanName = document.getString("name") ?: "오류",
                        sanDifficulty = document.getLong("difficulty") ?: 0,
                        sanHeight = document.getLong("height") ?: 0,
                        sanTimeTotal = (document.getLong("time_uphill")
                            ?: 0) + (document.getLong("time_downhill") ?: 0),
                        sanSelected = false
                    )
                )

            }
            sanArrayList[0].sanSelected = true

            Log.d(TAG, "값 다 가져옴 $sanArrayList")
            _sanListDTO.value = sanArrayList
        }

    }

}
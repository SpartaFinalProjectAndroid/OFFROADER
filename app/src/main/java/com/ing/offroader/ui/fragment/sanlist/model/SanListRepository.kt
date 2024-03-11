package com.ing.offroader.ui.fragment.sanlist.model

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

    fun editSelectedItem(selectedItem: SanDTO) {
        val position = sanListDTO.value?.indexOf(selectedItem)
        if (position != null) {
            initPush(position)
        } else {
            Log.d(TAG,"선택된 아이템이 인덱스가 널임 ? ")
        }
    }


    init {
        Log.d(TAG, "init 함수")
        initPush(0)
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

     suspend fun editsanDTO(index: Int) {
         initPush(index)
     }

     fun initPush(index: Int) {

        Log.d(TAG, "initPush 함수 여기에서 데이터베이스에 있는 값 접근")
        val sanArrayList: ArrayList<SanDTO> = arrayListOf()

        db.collection("sanlist").get().addOnSuccessListener { documents ->


            for (i in documents.documents.indices) {
                val sanInfo = documents.documents[i]

                sanArrayList.add(
                    SanDTO(
                        sanImage = sanInfo["images"] as ArrayList<String>,
                        sanName = sanInfo["name"] as String,
                        sanDifficulty = sanInfo["difficulty"] as Long?,
                        sanHeight = sanInfo.getLong("height") ?: 0,
                        sanTimeTotal = (sanInfo["time_uphill"] as Long) + (sanInfo["time_downhill"] as Long),
                        sanSelected = false
                    )
                )

            }

            Log.d(
                TAG,
                "DocumentSnapshot Data: ${documents.documents}"
            ) // HashMap 타입으로 값을 받아옴. 이 그대로 저장해줘도 될듯?
//            for (document in documents) {
//                sanArrayList.add(
//                    SanDTO(
//                        sanImage = document["images"] as ArrayList<String>?,
//                        sanName = document["name"] as String?,
//                        sanDifficulty = document["difficulty"] as Long?,
//                        sanHeight = document["height"] as Double?,
//                        sanTimeTotal = (document["time_uphill"] as Long) + (document["time_downhill"] as Long),
//                        sanSelected = false
//                    )
//                )
//
//            }
            sanArrayList[index].sanSelected = true

            Log.d(TAG, "값 다 가져옴 $sanArrayList")
            _sanListDTO.value = sanArrayList
        }

    }

}
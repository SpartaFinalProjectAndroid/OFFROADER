package com.ing.offroader.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.ing.offroader.data.model.sanInfo.AllSanDTO
import com.ing.offroader.ui.activity.sandetail.SanDetailDTO
import com.ing.offroader.ui.fragment.home.HomeUiState
import com.ing.offroader.ui.fragment.sanlist.model.SanDTO
import kotlinx.coroutines.tasks.await

class SanListRepository {
    companion object {
        private const val TAG = "SanListRepository"
    }

    // db 인스턴스 생성
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // 산 리스트 변수
    private val _sanListDTO: MutableLiveData<List<SanDTO>> =
        MutableLiveData<List<SanDTO>>()
    val sanListDTO: LiveData<List<SanDTO>> = _sanListDTO

    // HomeFragment 변수
    private val _recItems: MutableLiveData<ArrayList<HomeUiState>> = MutableLiveData()
    val recItems: LiveData<ArrayList<HomeUiState>> = _recItems

    // San Detail Activity

    private val _detailInfo: MutableLiveData<SanDetailDTO> = MutableLiveData()
    val detailInfo: LiveData<SanDetailDTO> = _detailInfo


    // 초기 실행
    init {
        Log.d(TAG, "init 함수")
        initPush(0)
        initRecommendedSan()
    }

    fun editSelectedItem(selectedItem: SanDTO) {
        val position = sanListDTO.value?.indexOf(selectedItem)
        if (position != null) {
            initPush(position)
        } else {
            Log.d(TAG, "선택된 아이템이 인덱스가 널임 ? ")
        }
    }


//    suspend fun editsanDTO(index: Int) {
//        initPush(index)
//    }

    fun getSelectedItemFromRepository(sanName: String?) {
        if (sanName.isNullOrBlank()) {
            Log.d(TAG, "getSelectedItemFromRepository: null sanName")
        } else {
            setSanDetail(sanName)
        }
        Log.d(TAG, "getSelectedItemFromRepository: $sanName")
    }

    fun initPush(index: Int) {

        Log.d(TAG, "initPush 함수 여기에서 데이터베이스에 있는 값 접근")

        db.collection("sanTest").get().addOnSuccessListener { documents ->

            // 산 리스트 DTO를 가져와주는 함수
            setSanListData(index, documents)
            // 홈 화면에 추천 산을 가져와주는 함수

        }

    }




    suspend fun loadAllSanList() {
        val sanItems: ArrayList<AllSanDTO?> = arrayListOf()

        val testtt = FirebaseFirestore.getInstance().collection("AllSanList")
//        Log.d(TAG, "loadAllSanList: $testtt")


        // 수정된 코드

        testtt.get().await().documents.forEach {
            val testCode : AllSanDTO? = it.toObject(AllSanDTO::class.java)
            sanItems.add(testCode)
        }

        sanItems.forEach {
//            Log.d(TAG, "loadAllSanList: $it")
        }





        /*

            suspend fun getUserData(userUID: String) : UserData {

        try {
            val userData = FirebaseFirestore.getInstance().collection("User").document(userUID)

            val userAchievements = userData.collection("Achievements")
            val sanID = userAchievements.document("san_id").get().await().toObject(SanID::class.java)
            val attendance = userAchievements.document("attendance").get().await().toObject(Attendance::class.java)

            val userCommunity = userData.collection("Community")
            val post = userCommunity.document("post").get().await().toObject(Post::class.java)

            val userDataClass: UserData? = userData.get().await().toObject(UserData::class.java)
            userDataClass?.achievements = Achievements(sanID, attendance)
            userDataClass?.community = post

            //if (userDataClass == null) return UserData()
            //userDataClass

            return userDataClass ?: UserData()

        } catch (e: Exception) {
            Log.e(TAG, "FireStore Error: $e")
            return UserData()
        }
    }

        */


//        db.collection("sanTest").whereEqualTo("isLiked", true).get().addOnSuccessListener { documents ->
//                documents.forEach { document ->
//
//                    val rec = HomeUiState(
//                        document.getString("address") ?: "",
//                        document.getLong("difficulty") ?: 0,
//                        document.getLong("height") ?: 0,
//                        document["images"] as ArrayList<String>,
//                        document.getBoolean("isLiked") ?: false,
//                        document.getString("name") ?: "",
//                        document.getString("recommend") ?: "",
//                        document.getString("summary") ?: "",
//                        document.getLong("time_downhill") ?: 0,
//                    )
//
//                    if (document.getField<Boolean>("isLiked") == true) {
//                        rvItems.add(rec)
//
//                        //데이터 로딩이 완료되면 리사이클러 뷰를 업데이트 한다
//                        //updateRecyclerView()
//                    }
//
//                    /** 데이터 전달 부분임 */
//                    _recItems.value = rvItems
//                    Log.d(TAG, "rv : ${recItems.value?.size}")
//
//
//                }
//
//            }

    }






    fun initRecommendedSan() {
        Log.d(TAG, "initRecommendedSan 실행")

        val rvItems: ArrayList<HomeUiState> = arrayListOf()

        db.collection("AllSanList").whereEqualTo("isliked", true).get()
            .addOnSuccessListener { documents ->
                Log.d(TAG, "OnSuccess 됨 $documents, ${documents.size()}")

                documents.forEach { document ->
                    Log.d(TAG, "For문 돌아간다아")

                    val rec = HomeUiState(
                        document.getString("address") ?: "",
                        document.getLong("difficulty") ?: 0,
                        document.getLong("height") ?: 0,
                        document["images"] as ArrayList<String>,
                        document.getBoolean("isliked") ?: false,
                        document.getString("name") ?: "",
                        document.getString("recommend") ?: "",
                        document.getString("summary") ?: "",
                        document.getLong("time") ?: 0,
                    )
                    rvItems.add(rec)
                    _recItems.value = rvItems
                    Log.d(TAG, "rv : ${recItems.value?.size}")


                }

            }.addOnCanceledListener{
                Log.d(TAG, "CANCELLED")
            }.addOnFailureListener{
                Log.d(TAG, "FAILED")
            }

    }

    fun setSanDetail(sanName: String) {
        db.collection("sanTest").get().addOnSuccessListener { documents ->
            documents?.forEach { document ->
                val sanList = SanDetailDTO(
                    document.getString("name") ?: "none",
                    document.getString("address") ?: "none",
                    document.getLong("difficulty") ?: 0,
                    document.getDouble("height") ?: 0.0,
                    document.getLong("time") ?: 0,
                    document.getString("summary") ?: "none",
                    document.getString("recommend") ?: "none",
                    document["images"] as ArrayList<String>,
                    document.getBoolean("isLiked") ?: false
                )
                if (sanList.mountain == sanName) {
                    _detailInfo.value = sanList
                    //Log.d(TAG, "initSanData: $sanList -> ${detailInfo.value}")
                }
            }
        }
    }


    private fun setSanListData(index: Int, documents: QuerySnapshot?) {
        val sanArrayList: ArrayList<SanDTO> = arrayListOf()

        if (documents != null) {
            for (i in documents.documents.indices) {
                val sanInfo = documents.documents[i]
                //
                sanArrayList.add(
                    SanDTO(
                        sanImage = sanInfo["images"] as ArrayList<String>,
                        sanName = sanInfo["name"] as String,
                        sanDifficulty = sanInfo["difficulty"] as Long?,
                        sanHeight = sanInfo.getLong("height") ?: 0,
                        sanTimeTotal = (sanInfo["time"] as Long),
                        sanSelected = false
                    )
                )

            }
        }

        //Log.d(
          //  TAG,
            //"DocumentSnapshot Data: ${documents?.documents}"
        //) // HashMap 타입으로 값을 받아옴. 이 그대로 저장해줘도 될듯?

        sanArrayList[index].sanSelected = true

        //Log.d(TAG, "값 다 가져옴 $sanArrayList")
        _sanListDTO.value = sanArrayList

    }
}


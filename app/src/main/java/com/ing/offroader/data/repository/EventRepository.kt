package com.ing.offroader.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.ing.offroader.ui.fragment.home.HomeUiData

private const val TAG = "EventRepository"
class EventRepository {
    private val db = FirebaseFirestore.getInstance()
    private val _eventItems : MutableLiveData<ArrayList<HomeUiData.Fourth>> = MutableLiveData()

    val eventItems : LiveData<ArrayList<HomeUiData.Fourth>> = _eventItems
    init {
        Log.d(TAG,"이닛")
        initEventListData()

    }
    private fun initEventListData() {
        db.collection("event") // Firestore의 컬렉션 이름
            .get()
            .addOnSuccessListener { documents ->
                val eItems: ArrayList<HomeUiData.Fourth> = arrayListOf()

                if (documents.isEmpty) {
                    Log.d("fireTest", "데이터 없음, なにが problem 입니까?")
                } else {
                    Log.d("fireTest", "event 컬렉션의 doc 개수 : ${documents.size()}")
                    for (document in documents) {
                        Log.d("fireTest", "doc id : ${document.id}, data: ${document.data}")



                        val event = HomeUiData.Fourth(
                            image = document.getString("img") ?: "유효한 데이터가 아닙니다",
                            title = document.getString("title") ?: "유효한 데이터가 아닙니다",
                            des = document.getString("des") ?: "유효한 데이터가 아닙니다",
                            date = document.getLong("date")
                        )
                        eItems.add(event)
                    }

                }
                //데이터 로딩이 완료되면 리사이클러 뷰를 업데이트 한다
//                updateRecyclerView()
                _eventItems.value = eItems
                Log.d(TAG, "event : ${eventItems.value?.size}")

            }
            .addOnFailureListener { exception ->
                Log.d("fireTest", "파이어베이스 에러 : ", exception)
            }

    }
}
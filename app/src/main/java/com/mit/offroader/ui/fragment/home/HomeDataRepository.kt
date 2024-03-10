package com.mit.offroader.ui.fragment.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.getField

private const val TAG = "리포지토리"

class HomeDataRepository {

    private val db = FirebaseFirestore.getInstance()
    private var _recItems : MutableLiveData<ArrayList<HomeUiState>> = MutableLiveData()
    val recItems : LiveData<ArrayList<HomeUiState>>  get( )= _recItems

    private var _eventItems : MutableLiveData<ArrayList<HomeUiData.Fourth>> = MutableLiveData()

    val eventItems : LiveData<ArrayList<HomeUiData.Fourth>> get( )= _eventItems


    init {
        Log.d(TAG,"이닛")
        initSanListData()
        initEventListData()

    }

    private fun initSanListData() {
        db.collection("sanlist") // Firestore의 컬렉션 이름
            .get()
            .addOnSuccessListener { documents ->
                val rvItems: ArrayList<HomeUiState> = arrayListOf()
                documents.forEach { document ->

                    val rec = HomeUiState(
                        document.getString("address") ?: "",
                        document.getLong("difficulty") ?: 0,
                        document.getLong("height") ?: 0,
                        document["images"] as ArrayList<String>,
                        document.getBoolean("isLiked") ?: false,
                        document.getString("name") ?: "",
                        document.getString("recommend") ?: "",
                        document.getString("summary") ?: "",
                        document.getLong("time_downhill") ?: 0,
                        document.getLong("time_uphill") ?: 0,
                    )
                    if (document.getField<Boolean>("isLiked") == true) {
                        rvItems.add(rec)

                        //데이터 로딩이 완료되면 리사이클러 뷰를 업데이트 한다
//                        updateRecyclerView()
                    }

                }
                _recItems.value = rvItems
                Log.d(TAG, "rv : ${recItems.value?.size}")
            }
            .addOnFailureListener { exception ->
                Log.d("fireTest", "파이어베이스 에러 : ", exception)
            }

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
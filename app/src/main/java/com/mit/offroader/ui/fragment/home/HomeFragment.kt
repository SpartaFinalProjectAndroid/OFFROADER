package com.mit.offroader.ui.fragment.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.mit.offroader.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var myPageAdapter: HomeMultiViewTypeAdapter
    private var uiData: List<HomeUiData> = listOf()
    private lateinit var mcontext: Context
    private var items: ArrayList<HomeUiData.Fourth> = ArrayList()

    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mcontext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        myPageAdapter = HomeMultiViewTypeAdapter(requireContext())

        FirebaseApp.initializeApp(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = FirebaseFirestore.getInstance()

        db.collection("event") // Firestore의 컬렉션 이름
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Log.d("fireTest", "데이터 없음, なにが problem 입니까?")
                } else {
                    Log.d("fireTest", "event 컬렉션의 doc 개수 : ${documents.size()}")
                    for (document in documents) {
                        Log.d("fireTest", "doc id : ${document.id}, data: ${document.data}")

                        val event = HomeUiData.Fourth(
                            image = document.getString("img") ?: "none (?: <-으로 나오는 텍스트)",
                            title = document.getString("title") ?: "유효한 데이터가 아닙니다",
                            des = document.getString("des") ?: "none (?: <-으로 나오는 텍스트)",
                            date = document.getLong("date") ?: 1
                        )
                        items.add(event)
                    }
                }
                //데이터 로딩이 완료되면 리사이클러 뷰를 업데이트 한다
                updateRecyclerView()
            }
            .addOnFailureListener { exception ->
                Log.d("fireTest", "파이어베이스 에러 : ", exception)
            }

        //Fourth를 제외한 나머지를 먼저 로딩, 로딩이 오래걸리면 빈 화면 나오길래 분리함
        uiData = listOf(
            HomeUiData.First,
            HomeUiData.Second,
            HomeUiData.Third,
            HomeUiData.Attribute
        )

        binding.rvHome.adapter = myPageAdapter
        binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
        myPageAdapter.submitList(uiData.toList())
    }

    fun updateRecyclerView() {
        uiData = listOf(
            HomeUiData.First,
            HomeUiData.Second,
            HomeUiData.Third
        ) + items + HomeUiData.Attribute
        myPageAdapter.submitList(uiData.toList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

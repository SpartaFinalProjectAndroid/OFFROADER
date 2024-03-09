package com.mit.offroader.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mit.offroader.databinding.FragmentHomeBinding
import com.mit.offroader.ui.fragment.chatbot.MyApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "홈프래그먼트"

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory((requireActivity().application as MyApplication).homeDataRepository)
    }
    private val myPageAdapter: HomeMultiViewTypeAdapter by lazy {
        HomeMultiViewTypeAdapter(requireContext(), homeViewModel)
    }
    private var uiData: List<HomeUiData> = listOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiData = listOf(
            HomeUiData.First,
            HomeUiData.Third,
            HomeUiData.Attribute
        )

        initView()
//        FirebaseApp.initializeApp(requireContext())

        initObserver()

//        binding.rvHome.adapter = myPageAdapter
//        binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
//        myPageAdapter.submitList(uiData.toList())


        //Fourth를 제외한 나머지를 먼저 로딩, 로딩이 오래걸리면 빈 화면 나오길래 분리함


    }

    private fun initView() {
//        homeViewModel.initialise()
//        initializeValue()

        binding.rvHome.adapter = myPageAdapter
        binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
//        Log.d(TAG, uiData.toString())
        myPageAdapter.submitList(uiData.toList())


    }

    //    private fun initializeValue() {
//        homeViewModel.initialise()
//    }
    private fun initObserver() {

//        homeViewModel.uiState.value?.rvItems?.let {
//            Log.d(TAG,it.toString())
//            }


        homeViewModel.uiState.observe(viewLifecycleOwner) {


            CoroutineScope(Dispatchers.Main)
                .launch {
                    it.rvItems?.let { it1 ->
                        it.eventItems?.let { it2 ->
                            updateRecyclerView(it1, it2) }
                    }
//                    updateRecyclerView(it.rvItems, it.eventItems)
                }
            Log.d(TAG, "rvItems: ${it.rvItems}")
            Log.d(TAG, "eventItems: ${it.eventItems}")

//            myPageAdapter = it.rvItems?.let { it1 -> HomeMultiViewTypeAdapter(requireContext(), it1) }!!
            it.rvItems?.let { it1 -> HomeHoriAdapter(items = it1) }
        }


    }

    private fun updateRecyclerView(
        rvItems: ArrayList<HomeUiState>,
        eventItems: ArrayList<HomeUiData.Fourth>
    ) {
        uiData = listOf(
            HomeUiData.First,
        ) + HomeUiData.Second(rvItems) + HomeUiData.Third + eventItems + HomeUiData.Attribute

        Log.d(TAG, "업데이트 : ${uiData.toList()}")
//        myPageAdapter.submitList(uiData.toList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

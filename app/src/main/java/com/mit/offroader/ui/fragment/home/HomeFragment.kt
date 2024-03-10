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


        initView()
//        FirebaseApp.initializeApp(requireContext())

        initObserver()

//        binding.rvHome.adapter = myPageAdapter
//        binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
//        myPageAdapter.submitList(uiData.toList())


        //Fourth를 제외한 나머지를 먼저 로딩, 로딩이 오래걸리면 빈 화면 나오길래 분리함


    }

    private fun initView() {


        homeViewModel.initialise()

        uiData = listOf(
            HomeUiData.First,
            HomeUiData.Third,
            HomeUiData.Attribute
        )

        binding.rvHome.adapter = myPageAdapter
        binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
//        Log.d(TAG, uiData.toString())
        myPageAdapter.submitList(uiData.toList())


    }

    //    private fun initializeValue() {

    //    }
    private fun initObserver() {


        homeViewModel.eventItems.observe(viewLifecycleOwner) {
            CoroutineScope(Dispatchers.Main).launch {
                updateRecyclerView(it)
            }

        }


    }

    private fun updateRecyclerView(
        eventItems: ArrayList<HomeUiData.Fourth>
    ) {
        uiData = listOf(
            HomeUiData.First,
        ) + HomeUiData.Second + HomeUiData.Third + eventItems + HomeUiData.Attribute

        Log.d(TAG, "업데이트 : ${uiData.toList()}")
        myPageAdapter.submitList(uiData.toList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

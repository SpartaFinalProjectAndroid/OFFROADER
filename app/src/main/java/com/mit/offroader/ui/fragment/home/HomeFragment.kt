package com.mit.offroader.ui.fragment.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mit.offroader.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var myPageAdapter: HomeMultiViewTypeAdapter
    var uiData: List<HomeUiData> = listOf()
    private lateinit var mcontext: Context

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val oriImg = binding.ivOrigin
//        val blurImg = binding.ivBlur

//        Blurry.with(context)
//            .radius(10)
//            .sampling(8)
//            .capture(oriImg)
//            .into(blurImg)

        //여기!!


// 가정: 뷰 바인딩이 이미 설정되어 있고, 'binding'이라는 변수명으로 사용 가능
//        val blurView = binding.fkBlurView
//        activity?.let { blurView.setBlur(it, blurView, 20) }




        uiData = listOf(
            HomeUiData.First,
            HomeUiData.Second
        )

        binding.rvHome.adapter = myPageAdapter
        binding.rvHome.layoutManager = LinearLayoutManager(requireContext())

        myPageAdapter.submitList(uiData.toList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.ing.offroader.ui.fragment.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ing.offroader.databinding.FragmentHomeBinding
import com.ing.offroader.ui.activity.login.LoginActivity
import com.ing.offroader.ui.activity.main.MainActivity
import com.ing.offroader.ui.fragment.chatbot.MyApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "홈프래그먼트"

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            (requireActivity().application as MyApplication).sanListRepository,
            (requireActivity().application as MyApplication).eventRepository
        )
    }
    private lateinit var myPageAdapter: HomeMultiViewTypeAdapter
    private var uiData: List<HomeUiData> = listOf()

    private var auth: FirebaseAuth? = null
    private val user = FirebaseAuth.getInstance().currentUser

    private var startTime : Long? = null
    private var endTime : Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        // 프래그먼트에 있는 onCreateView 함수에는 페이지 인플레이팅만 들어갑닏다!
        // onViewCreated에 작성해주세요!
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myPageAdapter = HomeMultiViewTypeAdapter(requireContext(), homeViewModel, null)
        binding.rvHome.adapter = myPageAdapter

        // 시작 시간 기록 (나노초 단위로 더 정밀한 측정 가능)
        startTime = System.nanoTime()
        initView()
        initObserver()

        initListener()
    }

    private fun initView() {
        if (user != null) {
            binding.tvUsername.text = user.email ?: "츄파춥스님, 안녕하세요!"
            Log.d(TAG, "onViewCreated: ${user.uid}")
            Log.d(TAG, "onViewCreated: ${user.email}")
        }
        myPageAdapter = HomeMultiViewTypeAdapter(requireContext(), homeViewModel, arrayListOf<HomeUiState>())
        binding.rvHome.adapter = myPageAdapter
        binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initListener() = with(binding) {
        tvUsername.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
        if (user != null) {
            tvSignOut.visibility = View.VISIBLE
        }
        tvSignOut.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }
        btnAttendance.setOnClickListener {
            homeViewModel.takeAttendance()
        }
    }

    private fun initObserver() {
        Log.d(TAG, "initObserver")

        uiData = listOf(
            HomeUiData.First,
            HomeUiData.Third,
            HomeUiData.Attribute
        )
        myPageAdapter.submitList(uiData.toList())

        homeViewModel.recItems.observe(viewLifecycleOwner) {
        Log.d(TAG, "recItems 옵져빙")
            myPageAdapter = HomeMultiViewTypeAdapter(requireContext(), homeViewModel, it)
            binding.rvHome.adapter = myPageAdapter
            binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
            myPageAdapter.submitList(uiData.toList())
        }

        homeViewModel.eventItems.observe(viewLifecycleOwner) {
        Log.d(TAG, "eventItems 옵져빙")
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

//        Log.d(TAG, "업데이트 : ${uiData.toList()}")
        myPageAdapter.submitList(uiData.toList())

        // 종료 시간 기록 및 계산
        endTime = System.nanoTime()
        val duration = endTime!! - startTime!!

        // 결과 로그 출력
        Log.d(TAG, "시간: ${startTime}ns, ${endTime}ns")
        Log.d(TAG, "프래그먼트로 데이터 전송에 걸린 시간: ${duration}ns")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

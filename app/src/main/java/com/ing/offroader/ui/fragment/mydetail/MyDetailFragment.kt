package com.ing.offroader.ui.fragment.mydetail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.ing.offroader.databinding.FragmentMyDetailBinding
import com.ing.offroader.ui.activity.achievement.AchievementActivity
import com.ing.offroader.ui.activity.login.LoginActivity
import com.ing.offroader.ui.activity.main.MainViewModel
import com.ing.offroader.ui.activity.sandetail.MyLikedSan
import com.ing.offroader.ui.activity.sandetail.SanDetailActivity
import com.ing.offroader.ui.fragment.community.MyApplication
import com.ing.offroader.ui.fragment.mydetail.viewmodel.MyDetailViewModel
import com.ing.offroader.ui.fragment.mydetail.viewmodel.MyDetailViewModelFactory

class MyDetailFragment : Fragment() {

    companion object {
        private const val TAG = "태그 : MyDetailFragment"
    }

    private var _binding: FragmentMyDetailBinding? = null
    private val binding get() = _binding!!

    private val likedSanViewModel by activityViewModels<MainViewModel>()

    private val myDetailViewModel: MyDetailViewModel by viewModels {
        return@viewModels MyDetailViewModelFactory(
            (requireActivity().application as MyApplication).sanListRepository
        )
    }

    // 사용자 정보 가져오기
    private var user = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyDetailBinding.inflate(inflater, container, false)

        myDetailViewModel.getUserData("user_test") // 파이어스토에 해당 유저 UID에 맞는 데이터 가져오기


        return binding.root

        /** 프래그먼트에는 onCreateView랑 onViewCreated 가 둘다 있는데 onCreateView에서는 바인딩해주는
         *  작업만 해주고 모든 초기화및 함수 로직은 onViewCreated에서 해줘야한다고 튜터님께서 말씀해주셨어요
         *  꼬일수도 있다고 하셨던 것 같습니다!
         **/

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터 받아오기
        val myLikedSan = likedSanViewModel.sanLikedList.value
        Log.d(TAG, "${myLikedSan}")
        if (myLikedSan != null) {
            initLikedRecyclerView(myLikedSan)
        }

        initBlur()
//        initObserver()

        // Lv Dialog
        setLvDialog()

        // 업적창으로 이동
        goToAchieveActivity()

    }

    private fun initObserver() {
//        myDetailViewModel.myDetailDTO.observe(viewLifecycleOwner) {
//
//        }
    }


    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
        initView()
    }

    private fun initView() {
        Log.d(TAG, "initView: ")
        setUpUserDetail()
        setUpListeners()

    }

    private fun setUpUserDetail() {
        user = FirebaseAuth.getInstance().currentUser
        Log.d(TAG, "setUpUserDetail: ${user?.uid}")
        when (user?.uid) {
            null -> setNoLoggedInUser()
            else -> setUserInformation()
        }
    }

    private fun setUserInformation() = with(binding) {
        tvLogin.visibility = View.INVISIBLE
        tvId.visibility = View.INVISIBLE
        tvName.visibility = View.VISIBLE
        tvName.text = user?.displayName
        tvNameNim.visibility = View.VISIBLE
        // 아직 구현이 안된 부분이라 숨겨둘 예정
        // 회원 가입 시 로그인 정부 추가 하면 구현할 VISIBLE로 바꿔주고 적절한 값을 추가해주면 되지 않울까욤.
        tvProfilInfo.visibility = View.INVISIBLE
        clAddress.visibility = View.INVISIBLE
        Glide.with(requireActivity()).load(user?.photoUrl).into(ivProfil)


    }

    private fun setNoLoggedInUser() = with(binding) {
        tvLogin.visibility = View.VISIBLE
        tvLogin.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
        tvId.visibility = View.VISIBLE
        tvName.visibility = View.INVISIBLE
        tvNameNim.visibility = View.INVISIBLE
        tvProfilInfo.visibility = View.INVISIBLE
    }

    private fun setUpListeners() = with(binding) {

    }

    // 로그인 상태가 아닐 때, blur처리
    private fun initBlur() {
        val blur = binding.blur
        with(blur) {
            setupWith(binding.clMyInfo)
                .setBlurEnabled(true)
                .setBlurRadius(15f)
        }
    }

    // lv 부분에 있는 아이콘 누르면 Dialog 생성
    private fun setLvDialog() {
        binding.ivLvInfo.setOnClickListener {
            val dialog = LvDialogFragment()
            dialog.show(childFragmentManager, "LvDialog")
        }
    }

    // 업적 버튼을 누르면 업적 창으로 이동
    private fun goToAchieveActivity() {
        binding.ivAchieveInfo.setOnClickListener {
            val intent = Intent(requireActivity(), AchievementActivity::class.java)

            startActivity(intent)
        }
    }

    // 톱니바퀴 누르면 setting
    private fun goToSettingFragment() {
        binding.ivSetting.setOnClickListener { }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    private fun initLikedRecyclerView(sanlist: MutableList<MyLikedSan>) {
        binding.rvRecode.adapter = MyBookmarkAdapter(sanlist)
        binding.rvRecode.layoutManager = GridLayoutManager(context, 4)

        setOnClickSan(sanlist)
    }

    private fun setOnClickSan(san: MutableList<MyLikedSan>) {
        MyBookmarkAdapter(san).sanClick = object : MyBookmarkAdapter.SanClick {
            override fun onClick(item: MyLikedSan) {
                val intent = Intent(requireActivity(), SanDetailActivity::class.java)
                val sanName = MyLikedSan(mountain = )

                if (sanName == null) {
                    intent.putExtra("name", "계룡산")
                } else {
                    intent.putExtra("name", sanName)
                }

                startActivity(intent)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}
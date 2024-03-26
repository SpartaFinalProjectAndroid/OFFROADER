package com.ing.offroader.ui.fragment.mydetail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ing.offroader.data.model.addpost.PostModel
import com.ing.offroader.databinding.FragmentMyDetailBinding
import com.ing.offroader.ui.activity.achievement.AchievementActivity
import com.ing.offroader.ui.activity.login.LoginActivity
import com.ing.offroader.ui.activity.main.MainActivity
import com.ing.offroader.ui.activity.main.MainViewModel
import com.ing.offroader.ui.activity.my_post.MyPostActivity
import com.ing.offroader.ui.activity.sandetail.MyLikedSan
import com.ing.offroader.ui.fragment.community.MyApplication
import com.ing.offroader.ui.fragment.community.model.PostDTO
import com.ing.offroader.ui.fragment.community.viewmodel.CommunityViewModel
import com.ing.offroader.ui.fragment.community.viewmodel.CommunityViewModelFactory
import com.ing.offroader.ui.fragment.mydetail.viewmodel.MyDetailViewModel
import com.ing.offroader.ui.fragment.mydetail.viewmodel.MyDetailViewModelFactory
import com.ing.offroader.ui.fragment.sanlist.adapter.SanListAdapter
import okhttp3.internal.notify
import okhttp3.internal.notifyAll

class MyDetailFragment : Fragment() {

    companion object {
        private const val TAG = "태그 : MyDetailFragment"
    }

    private var _binding: FragmentMyDetailBinding? = null
    private val binding get() = _binding!!

    private val likedSanViewModel by activityViewModels<MainViewModel>()

    private val myBookmarkAdapter: MyBookmarkAdapter by lazy { MyBookmarkAdapter() }


    //    private val myDetailViewModel by viewModels<MyDetailViewModel>()
    private val communityViewModel: CommunityViewModel by viewModels {
        CommunityViewModelFactory((requireActivity().application as MyApplication).postRepository)
    }

    private val myDetailViewModel: MyDetailViewModel by viewModels {
        return@viewModels MyDetailViewModelFactory(
            (requireActivity().application as MyApplication).sanListRepository,
            (requireActivity().application as MyApplication).postRepository
        )
    }


    // 사용자 정보 가져오기
    private var user = FirebaseAuth.getInstance().currentUser

    private var myPosts: ArrayList<PostModel?>? = null

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyDetailBinding.inflate(inflater, container, false)

        return binding.root

        /** 프래그먼트에는 onCreateView랑 onViewCreated 가 둘다 있는데 onCreateView에서는 바인딩해주는
         *  작업만 해주고 모든 초기화및 함수 로직은 onViewCreated에서 해줘야한다고 튜터님께서 말씀해주셨어요
         *  꼬일수도 있다고 하셨던 것 같습니다!
         *
         **/

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myDetailViewModel.getUserData("user_test") // 파이어스토에 해당 유저 UID에 맞는 데이터 가져오기
        initObserver()

        // 좋아요 한 산 데이터 받아오기
        initPreferenceData()
    }

    private fun initObserver() {


        myDetailViewModel.myPostLists.observe(viewLifecycleOwner) {
            Log.d(TAG, "initObserver: ${it?.size}")
            if (it != null) {
                Log.d(TAG, "initObserver: postItem 업데이트 ${it}")
                myPosts = it
                setUpUserDetail()

            } else {
                Log.d(TAG, "initObserver: 옵져빙된 값이 널이라서 업데이트가 안됨.")
            }
        }
    }


    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
        myDetailViewModel.setRepository()
        initView()
    }

    private fun initView() {
        Log.d(TAG, "initView: ")
        setUpUserDetail()
        setUpListeners()
        initPreferenceData()
    }

    private fun initPreferenceData() {
        val myLikedSan = likedSanViewModel.sanLikedList.value
        Log.d(TAG, "${myLikedSan}")
        if (myLikedSan != null) {
            initLikedRecyclerView(myLikedSan)
        }
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
        tvLogout.visibility = View.VISIBLE
        tvLogout.text = "로그아웃"
        tvId.visibility = View.INVISIBLE
        tvName.visibility = View.VISIBLE
        tvName.text = user?.displayName
        tvNameNim.visibility = View.VISIBLE
        // 아직 구현이 안된 부분이라 숨겨둘 예정
        // 회원 가입 시 로그인 정부 추가 하면 구현할 VISIBLE로 바꿔주고 적절한 값을 추가해주면 되지 않울까욤.
        tvProfileInfo.visibility = View.INVISIBLE
        clAddress.visibility = View.INVISIBLE
        Glide.with(requireActivity()).load(user?.photoUrl).into(ivProfil)
        setMyPosts()

        tvLogout.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra("MY_POSTS",myPosts)
            startActivity(intent)
        }

        binding.blur.visibility = View.GONE

        tvLogin.visibility = View.GONE

    }

    private fun setMyPosts() = with (binding) {
        clMyPost.isClickable = true

        if (myPosts.isNullOrEmpty()) {
            Log.d(TAG, "setMyPosts: ${myPosts?.size}")
            myDetailViewModel.setRepository()
            myPosts = myDetailViewModel.myPostLists.value
            Log.d(TAG, "setMyPosts: 2 ${myPosts?.size}")
            tvMyPostCount.text = ""

        } else {
//            tvMyPostCount.text = myPosts?.size.toString()
            tvMyPostCount.text = ""
        }    }

    private fun setNoLoggedInUser() = with(binding) {
        tvLogin.visibility = View.VISIBLE

        tvId.visibility = View.VISIBLE
        tvName.visibility = View.INVISIBLE
        tvNameNim.visibility = View.INVISIBLE
        tvProfileInfo.visibility = View.INVISIBLE
        clMyPost.isClickable = false
        tvMyPostCount.text = "-"

        blurMyInfo()

    }

    private fun setUpListeners() = with(binding) {
        clMyPost.setOnClickListener {
            user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                Toast.makeText(activity, "로그인 후 확인 가능합니다.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(requireActivity(), MyPostActivity::class.java)
                startActivity(intent)
            }
        }
        tvLogin.setOnClickListener {
            user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                startActivity(intent)
            } else {
                Firebase.auth.signOut()
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }
        }
        //내 정보 필드 - 레벨
        clLevel.setOnClickListener {
            val dialog = LvDialogFragment()
            dialog.show(childFragmentManager, "LvDialog")
        }
        //내 정보 필드 - 업적
        clAchievement.setOnClickListener {
            val intent = Intent(requireActivity(), AchievementActivity::class.java)
            startActivity(intent)
        }
        ivNotify.setOnClickListener {
            Toast.makeText(activity, "곧 구현될 예정입니다 :)", Toast.LENGTH_SHORT).show()
        }
        ivSetting.setOnClickListener {
            Toast.makeText(activity, "곧 구현될 예정입니다 :)", Toast.LENGTH_SHORT).show()
        }
        //등산 기록
        clRecord.setOnClickListener {
            Toast.makeText(activity, "준비중입니다 :)", Toast.LENGTH_SHORT).show()
        }
    }

    // 로그인 상태가 아닐 때, blur처리
    private fun blurMyInfo() {
        with(binding.blur) {
            setupWith(binding.clDetailRoot)
                .setBlurEnabled(true)
                .setBlurRadius(4f)
        }
    }

    //톱니바퀴 누르면 setting
    private fun goToSettingFragment() {
        binding.ivSetting.setOnClickListener { }
    }


    private fun initLikedRecyclerView(sanlist: MutableList<MyLikedSan>) {
        Log.d(TAG, "initLikedRecyclerView: $sanlist.")
        binding.rvRecode.adapter = myBookmarkAdapter
        binding.rvRecode.layoutManager = GridLayoutManager(context, 4)
        binding.rvRecode.itemAnimator = null
        myBookmarkAdapter.submitList(sanlist)

        setOnClickSan(sanlist)
    }

    private fun setOnClickSan(san: MutableList<MyLikedSan>) {
//
        MyBookmarkAdapter().sanClick = object : MyBookmarkAdapter.SanClick {
            override fun onClick(item: MyLikedSan) {
                Toast.makeText(activity, "곧 구현될 예정입니다 :)", Toast.LENGTH_SHORT).show()
//                val intent = Intent(requireActivity(), SanDetailActivity::class.java)
//
//                if (sanName == null) {
//                    intent.putExtra("name", "계룡산")
//                } else {
//                    intent.putExtra("name", sanName)
//                }
//
//                startActivity(intent)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


}
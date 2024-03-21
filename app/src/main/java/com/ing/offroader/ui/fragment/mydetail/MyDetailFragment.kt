package com.ing.offroader.ui.fragment.mydetail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.ing.offroader.databinding.FragmentMyDetailBinding
import com.ing.offroader.ui.activity.achievement.AchievementActivity
import com.ing.offroader.ui.fragment.chatbot.MyApplication
import com.ing.offroader.ui.fragment.mydetail.viewmodel.MyDetailViewModel
import com.ing.offroader.ui.fragment.mydetail.viewmodel.MyDetailViewModelFactory

class MyDetailFragment : Fragment() {

    companion object {
        fun newInstance() = MyDetailFragment()
    }

    private var _binding: FragmentMyDetailBinding? = null
    private val binding get() = _binding!!

    private val mItems = mutableListOf<MyDetailDTO>()

    private val myDetailViewModel: MyDetailViewModel by viewModels {
        return@viewModels MyDetailViewModelFactory(
            (requireActivity().application as MyApplication).sanListRepository
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyDetailBinding.inflate(inflater, container, false)

        myDetailViewModel.getUserData("user_test") // 파이어스토에 해당 유저 UID에 맞는 데이터 가져오기

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()

        initBlur()

        // Lv Dialog
        setLvDialog()

        // 업적창으로 이동
        goToAchieveActivity()

    }

    private fun initObserver() {
        myDetailViewModel.myDetailDTO.observe(viewLifecycleOwner) {
//            binding.rvRecode.adapter = MyBookmarkAdapter(mItems)
            binding.rvRecode.layoutManager = GridLayoutManager(context, 4)
        }
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
        binding.ivSetting.setOnClickListener {  }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    private fun initLikedRecyclerView() {
//        myBookmarkAdapter.onBookmarkClickedInMyLikedListener = listOf()
    }

}
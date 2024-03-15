package com.ing.offroader.ui.fragment.mydetail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ing.offroader.databinding.FragmentMyDetailBinding
import com.ing.offroader.ui.activity.achievement.AchievementActivity

class MyDetailFragment : Fragment() {

    companion object {
        fun newInstance() = MyDetailFragment()
    }

    private var _binding: FragmentMyDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var myBookmarkAdapter: MyBookmarkAdapter

    private val myDetailViewModel by viewModels<MyDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyDetailBinding.inflate(inflater, container, false)

        return binding.root

        initLikedRecyclerView()


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBlur()

        // Lv Dialog
        setLvDialog()

        // 업적창으로 이동
        goToAchieveActivity()

    }

    // 로그인 상태가 아닐 때, blur처리
    private fun initBlur() {
        val blur = binding.blur
        with(blur) {
            setupWith(this)
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    private fun initLikedRecyclerView() {
        myBookmarkAdapter.onBookmarkClickedInMyLikedListener = listOf()
    }

}
package com.ing.offroader.ui.fragment.community

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ing.offroader.R
import com.ing.offroader.data.model.addpost.EditPostDTO
import com.ing.offroader.databinding.FragmentCommunityBinding
import com.ing.offroader.ui.activity.add_post.AddPostActivity
import com.ing.offroader.ui.fragment.community.adapter.CommunityAdapter
import com.ing.offroader.ui.fragment.community.model.PostDTO
import com.ing.offroader.ui.fragment.community.viewmodel.CommunityViewModel
import com.ing.offroader.ui.fragment.community.viewmodel.CommunityViewModelFactory

private const val TAG = "태그 : CommunityFragment"

class CommunityFragment : Fragment() {

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!
    private val communityViewModel: CommunityViewModel by viewModels {
        CommunityViewModelFactory((requireActivity().application as MyApplication).postRepository)
    }
    private val communityAdapter: CommunityAdapter by lazy {
        CommunityAdapter(communityViewModel)
    }

    private var user = FirebaseAuth.getInstance().currentUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        initView()
        initObserver()
    }

    private fun initObserver() {
        Log.d(TAG, "initObserver: 옵져빙 시작!")
        communityViewModel.postItems.observe(viewLifecycleOwner) {
            Log.d(TAG, "initObserver: 포스트아이템즈 옵져빙 됨.")
            if (it != null) {
                Log.d(TAG, "initObserver: postItem 업데이트 ${it}")
                setItemView(it)
//                scrollToTop(it)
            } else {
                Log.d(TAG, "initObserver: 옵져빙된 값이 널이라서 업데이트가 안됨.")
            }

        }
    }

    private fun scrollToTop(it: ArrayList<PostDTO?>) {
        if (it.isNullOrEmpty().not()) {
            binding.rvCommunity.smoothScrollToPosition(0)
        }
    }

    private fun setItemView(postItems: ArrayList<PostDTO?>?) {
        Log.d(TAG, "setItemView: 셋 아이템 뷰 여기서 서브밋 함.")
        Log.d(TAG, "setItemView: ${postItems?.size}")
        // 깜빡거림
        binding.rvCommunity.apply {
            itemAnimator = null
        }
        val sortedItems = postItems?.sortedByDescending { it?.upload_date as Comparable<Any> }
        communityAdapter.submitList(sortedItems)
    }

    private fun initView() {
        Log.d(TAG, "initView: bind Adapter")
        // 어댑터를 가장 먼저 바인딩 해줘야만 서브밋이 가능함
        binding.rvCommunity.adapter = communityAdapter
        // 리포지토리에서 우선 커뮤니티 게시글을 가져와야함.
        Log.d(TAG, "initView: ${communityViewModel.postItems.value}")
        setItemView(communityViewModel.postItems.value)
        setAddPostButton()
        setUpAdapter()

    }

    private fun setAddPostButton() {

        Log.d(TAG, "setAddPostButton: $user")
        binding.ivAddPost.setOnClickListener {
            user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                Toast.makeText(requireActivity(), "회원가입을 해야만 포스팅이 가능합니다.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val intent = Intent(requireActivity(), AddPostActivity::class.java)
                startActivity(intent)
            }

        }
    }

    private fun setUpAdapter() {
        communityAdapter.moreClick = object : CommunityAdapter.ItemMoreClick {
            override fun itemMoreClick(item: PostDTO?) {
                when (user?.uid == item?.uid) {
                    true -> setEditDeleteBottomSheetDialog(item)
                    false -> setReportBottomSheetDialog(item)
                }
            }
        }
    }

    private fun setReportBottomSheetDialog(item: PostDTO?) {
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_other, null)
        val bottomSheetDialog = BottomSheetDialog(requireContext())

        bottomSheetDialog.setContentView(bottomSheetView)

        val reportButton: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.cl_report)

        reportButton?.setOnClickListener {
            bottomSheetDialog.dismiss()
            ToastMessage("게시물 삭제")
            // 삭제 다이얼로그 띄우기
            setUpReportDialog(bottomSheetDialog)

        }

        bottomSheetDialog.show()
    }

    private fun setUpReportDialog(bottomSheetDialog: BottomSheetDialog) {

    }

    private fun setEditDeleteBottomSheetDialog(item: PostDTO?) {
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_my, null)
        val bottomSheetDialog = BottomSheetDialog(requireContext())

        bottomSheetDialog.setContentView(bottomSheetView)

        val deleteButton: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.cl_delete)
        val editButton: ConstraintLayout? = bottomSheetDialog.findViewById(R.id.cl_edit)


        deleteButton?.setOnClickListener {
            bottomSheetDialog.dismiss()
            ToastMessage("게시물 삭제")
            // 삭제 다이얼로그 띄우기
            setUpDeleteDialog(bottomSheetDialog, item)

        }
        editButton?.setOnClickListener {
            ToastMessage("게시물 수정")     // 토스트 메세지 띄워주는 함수
            setEditPostView(item)              // 수정 페이지로 넘어가는 코드
            bottomSheetDialog.dismiss()        // 바텀시트 다이얼로그 내려줌.

        }
        bottomSheetDialog.show()
    }

    private fun showEditDeleteBottomSheet(bottomSheetDialog: BottomSheetDialog) {
        // TODO : 다이얼로그 디자인 해주세요ㅜㅜㅠㅠ
        ToastMessage("아직 다이얼로그 부분이 구현되지 않았습니다. 저희 팀원분 중 한 분이 해줄 거라고 했습니다. :)")
    }

    private fun setUpDeleteDialog(bottomSheetDialog: BottomSheetDialog,item: PostDTO?) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("게시물 삭제").setMessage("정말 게시물을 삭제하시겠습니까? (게시물은 영구 삭제됩니다.)")
            .setPositiveButton(
                "확인"
            ) { _, _ ->
                bottomSheetDialog.dismiss()
                ToastMessage("확인")
                communityViewModel.deletePost(item)
            }.setNegativeButton(
                "취소"
            ) { _, _ ->
                ToastMessage("취소")
                bottomSheetDialog.show()

            }
        builder.show()
    }

    private fun setEditPostView(item: PostDTO?) {

        val intent = Intent(activity, AddPostActivity::class.java)
        try {

            intent.putExtra(
                "POST_INFO",
                EditPostDTO(
                    item?.title.toString(),
                    item?.contents.toString(),
                    item?.post_id.toString()
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, "itemMoreClick: ", e)
        }

        startActivity(intent)
    }

    private fun ToastMessage(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT)
            .show()
    }


// 뷰 모델 옵져빙해주는 함수

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
    }


}
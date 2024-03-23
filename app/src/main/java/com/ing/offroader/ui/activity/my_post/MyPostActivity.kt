package com.ing.offroader.ui.activity.my_post

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ing.offroader.R
import com.ing.offroader.data.model.addpost.EditPostDTO
import com.ing.offroader.data.model.userInfo.Post
import com.ing.offroader.databinding.ActivityMyPostBinding
import com.ing.offroader.ui.activity.add_post.AddPostActivity
import com.ing.offroader.ui.fragment.community.MyApplication
import com.ing.offroader.ui.fragment.community.model.PostDTO
import com.ing.offroader.ui.fragment.mydetail.viewmodel.MyDetailViewModel
import com.ing.offroader.ui.fragment.mydetail.viewmodel.MyDetailViewModelFactory

private const val TAG = "태그 : MyPostActivity"

class MyPostActivity : AppCompatActivity() {

    private var _binding: ActivityMyPostBinding? = null
    private val binding get() = _binding!!

    private val myPostViewModel by viewModels<MyPostViewModel> {
        MyPostViewModelFactory((this.application as MyApplication).postRepository)
    }
    private val myPostAdapter: MyPostAdapter by lazy {
        MyPostAdapter(myPostViewModel)
    }
    private val myDetailViewModel: MyDetailViewModel by viewModels {
        return@viewModels MyDetailViewModelFactory(
            (this.application as MyApplication).sanListRepository,
            (this.application as MyApplication).postRepository
        )
    }


    private val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMyPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initObserver() {
        Log.d(TAG, "initObserver: ")
        myDetailViewModel.myPostItems.observe(this) {
            Log.d(TAG, "initObserver: ${it?.size}")
            if (it != null) {
                Log.d(TAG, "initObserver: postItem 업데이트 ${it}")
                setItemView(it)
            } else {
                Log.d(TAG, "initObserver: 옵져빙된 값이 널이라서 업데이트가 안됨.")
            }
        }
    }

    private fun setItemView(postItems: ArrayList<PostDTO?>?) {
        Log.d(TAG, "setItemView: $postItems")
        val sortedItems = postItems?.sortedByDescending { it?.upload_date as Comparable<Any> }
        myPostAdapter.submitList(sortedItems)
    }

    private fun initView() {

        Log.d(TAG, "initView: ")
        binding.rvMyPost.adapter = myPostAdapter
        setItemView(myDetailViewModel.myPostItems.value)
        setAddPostButton()
        setBackButton()
        setUpAdapter()

    }

    private fun setUpAdapter() {
        myPostAdapter.moreClick = object : MyPostAdapter.ItemMoreClick {
            override fun itemMoreClick(user: FirebaseUser?, item: PostDTO?) {

                if (user == null) {

                } else {
                    setBottomSheetDialog(item)


                }
            }

        }
    }

    private fun setBottomSheetDialog(item: PostDTO?) {
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_my, null)
        val bottomSheetDialog = BottomSheetDialog(this@MyPostActivity)

        bottomSheetDialog.setContentView(bottomSheetView)

        val deleteButton: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.cl_delete)
        val editButton: ConstraintLayout? = bottomSheetDialog.findViewById(R.id.cl_edit)

        deleteButton?.setOnClickListener {
            bottomSheetDialog.dismiss()
            ToastMessage("게시물 삭제")
            // 삭제 다이얼로그 띄우기
            setUpDeleteDialog(bottomSheetDialog)

        }
        editButton?.setOnClickListener {
            ToastMessage("게시물 수정")     // 토스트 메세지 띄워주는 함수
            setEditPostView(item)              // 수정 페이지로 넘어가는 코드
            bottomSheetDialog.dismiss()        // 바텀시트 다이얼로그 내려줌.

        }
        bottomSheetDialog.show()
    }

    private fun setUpDeleteDialog(bottomSheetDialog: BottomSheetDialog) {
        val builder = AlertDialog.Builder(this@MyPostActivity)
        builder.setTitle("게시물 삭제").setMessage("정말 게시물을 삭제하시겠습니까? (게시물은 영구 삭제됩니다.)")
            .setPositiveButton(
                "확인"
            ) { _, _ ->
                //TODO
                bottomSheetDialog.dismiss()
                ToastMessage("확인")
            }.setNegativeButton(
                "취소"
            ) { _, _ ->
                //TODO
                ToastMessage("취소")
            }
        builder.show()
    }

    private fun setEditPostView(item: PostDTO?) {

        val intent = Intent(this@MyPostActivity, AddPostActivity::class.java)
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

    private fun setBackButton() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun setAddPostButton() {
        Log.d(TAG, "setAddPostButton: ")
        binding.ivAddPost.setOnClickListener {
            if (user == null) {
                Toast.makeText(this, "회원가입을 해야만 포스팅이 가능합니다.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, AddPostActivity::class.java)
                startActivity(intent)
            }

        }
    }

    private fun ToastMessage(text: String) {
        Toast.makeText(this@MyPostActivity, text, Toast.LENGTH_SHORT)
            .show()
    }

    override fun onResume() {
        Log.d(TAG, "onResume: ")
        super.onResume()
        myDetailViewModel.setRepository()
        initObserver()

    }
}

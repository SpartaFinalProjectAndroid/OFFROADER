package com.ing.offroader.ui.activity.my_post

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.ing.offroader.databinding.ActivityMyPostBinding
import com.ing.offroader.ui.activity.add_post.AddPostActivity
import com.ing.offroader.ui.fragment.community.MyApplication
import com.ing.offroader.ui.fragment.community.model.PostDTO

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

    private val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMyPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initObserver() {
        Log.d(TAG, "initObserver: ")
        myPostViewModel.myPostItems.observe(this) {
            Log.d(TAG, "initObserver: ${it?.size}")
            if (it != null) {
                Log.d(TAG, "initObserver: postItem 업데이트 ${it}")
                setItemView(it)
            } else {
                Log.d(TAG, "initObserver: 옵져빙된 값이 널이라서 업데이트가 안됨.")
            }
        }
    }

    private fun setItemView(postItems: ArrayList<PostDTO?>?){
        Log.d(TAG, "setItemView: 셋 아이템 뷰 여기서 서브밋 함.")
        Log.d(TAG, "setItemView: $postItems")
        val sortedItems = postItems?.sortedByDescending { it?.upload_date as Comparable<Any> }
        myPostAdapter.submitList(sortedItems)
    }

    private fun initView() {

        Log.d(TAG, "initView: ")
        binding.rvMyPost.adapter = myPostAdapter
        setAddPostButton()
        setBackButton()

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
                Toast.makeText(this,"회원가입을 해야만 포스팅이 가능합니다.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, AddPostActivity::class.java)
                startActivity(intent)
            }

        }
    }

    override fun onResume() {
        Log.d(TAG, "onResume: ")
        super.onResume()
        initObserver()
        myPostViewModel.setRepository()
    }
}

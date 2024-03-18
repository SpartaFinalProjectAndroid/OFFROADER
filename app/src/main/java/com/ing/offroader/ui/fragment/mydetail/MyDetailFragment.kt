package com.ing.offroader.ui.fragment.mydetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ing.offroader.databinding.FragmentMyDetailBinding

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

        myDetailViewModel.getUserData("user_test") // 파이어스토에 해당 유저 UID에 맞는 데이터 가져오기

        return binding.root


        initLikedRecyclerView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    private fun initLikedRecyclerView() {
        myBookmarkAdapter.onBookmarkClickedInMyLikedListener = listOf()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}
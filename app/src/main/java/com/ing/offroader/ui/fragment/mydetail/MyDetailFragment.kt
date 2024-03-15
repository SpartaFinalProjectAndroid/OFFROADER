package com.ing.offroader.ui.fragment.mydetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

        return binding.root

        initLikedRecyclerView()


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBlur()
        initDialog()
    }

    private fun initBlur() {
        val blur = binding.blur
        with(blur) {
            setBlurEnabled(true)
            setBlurRadius(15f)
        }
    }

    private fun initDialog() {
        binding.ivLvInfo.setOnClickListener {
            val dialog = LvDialogFragment()
            dialog.show(childFragmentManager, "LvDialog")
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
package com.mit.offroader.ui.fragment.mydetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.mit.offroader.R
import com.mit.offroader.databinding.FragmentChatBotBinding
import com.mit.offroader.databinding.FragmentMyDetailBinding
import com.mit.offroader.ui.activity.sandetail.SanDetailViewModel

class MyDetailFragment : Fragment() {

    companion object {
        fun newInstance() = MyDetailFragment()
    }
    private var _binding: FragmentMyDetailBinding? = null
    private val binding get() = _binding!!

    private val myDetailViewModel by viewModels<MyDetailViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyDetailBinding.inflate(inflater, container, false)

        return binding.root    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}
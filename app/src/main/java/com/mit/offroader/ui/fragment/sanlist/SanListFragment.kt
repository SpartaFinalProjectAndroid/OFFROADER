package com.mit.offroader.ui.fragment.sanlist

import com.mit.offroader.utils.Application
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.mit.offroader.R
import com.mit.offroader.databinding.FragmentChatBotBinding
import com.mit.offroader.databinding.FragmentSanListBinding
import com.mit.offroader.ui.fragment.mydetail.MyDetailViewModel

class SanListFragment : Fragment() {

    companion object {
        fun newInstance() = SanListFragment()
    }
    private var _binding: FragmentSanListBinding? = null
    private val binding get() = _binding!!

    private val sanListViewModel: SanListViewModel by viewModels {
        SanListViewModelFactory((requireActivity().application as Application).sanListRepository)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSanListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
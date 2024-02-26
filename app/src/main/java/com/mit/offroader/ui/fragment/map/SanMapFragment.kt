package com.mit.offroader.ui.fragment.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mit.offroader.databinding.FragmentSanMapBinding


class SanMapFragment : Fragment() {

    companion object {
        fun newInstance() = SanMapFragment()
    }
    private var _binding: FragmentSanMapBinding? = null
    private val binding get() = _binding!!

    private val sanMapViewModel by viewModels<SanMapViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSanMapBinding.inflate(inflater, container, false)

        return binding.root    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
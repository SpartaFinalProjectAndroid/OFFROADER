package com.mit.offroader.ui.fragment.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.mit.offroader.databinding.FragmentMapBinding

class MapFragment : Fragment() {

    companion object {
        fun newInstance() = MapFragment()
    }
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val mapViewModel by viewModels<MapViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        return binding.root    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
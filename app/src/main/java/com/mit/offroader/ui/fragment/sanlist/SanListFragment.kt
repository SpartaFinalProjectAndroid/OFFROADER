package com.mit.offroader.ui.fragment.sanlist

import android.annotation.SuppressLint
import com.mit.offroader.utils.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.mit.offroader.databinding.FragmentSanListBinding
import com.mit.offroader.ui.fragment.sanlist.adapter.SanListAdapter
import com.mit.offroader.ui.fragment.sanlist.model.SanDTO
import com.mit.offroader.ui.fragment.sanlist.viewmodel.SanListViewModel
import com.mit.offroader.ui.fragment.sanlist.viewmodel.SanListViewModelFactory

class SanListFragment : Fragment() {

    companion object {
        fun newInstance() = SanListFragment()
    }

    private var _binding: FragmentSanListBinding? = null
    private val binding get() = _binding!!
    private val sanListAdapter: SanListAdapter by lazy { SanListAdapter(sanListViewModel) }
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
        binding.rvSanList.adapter = sanListAdapter

        initView()
        initObserve()
    }

    private fun initView() {
        setRecyclerViewGridLayout()
    }



    private fun initObserve() {
        sanListViewModel.sanListUiState.observe(viewLifecycleOwner) {
            it?.selectedItem?.let { it1 -> setSelectedSan(it1) }
        }
        sanListViewModel.sanListRepository.observe(viewLifecycleOwner) {
            sanListAdapter.submitList(it)
        }
    }

    @SuppressLint("SetTextI18n") //하드코딩 하지말라는 경고를 타이틀 어노테이션을 통해 무시함.
    private fun setSelectedSan(selectedItem: SanDTO) {
        if (selectedItem != null) {
            Glide.with(this).load(selectedItem.sanImage).into(binding.ivSelectedImage)
        }
        val height = "${selectedItem.sanHeight?.div(1000)},${selectedItem.sanHeight?.rem(1000)}m"
        val timeTaken =
            "${selectedItem.sanTimeTotal?.div(60)}h ${selectedItem.sanTimeTotal?.rem(60)}min"
        val difficulty = when (selectedItem.sanDifficulty?.toInt()) {
            0 -> "하"
            1 -> "중"
            else -> "상"
        }
        val divider = " | "
        binding.tvSanName.text = selectedItem.sanName
        binding.tvSanInfo.text = height + divider + timeTaken + divider + difficulty
    }

    private fun setRecyclerViewGridLayout() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 4)
        binding.rvSanList.layoutManager = gridLayoutManager
    }


}
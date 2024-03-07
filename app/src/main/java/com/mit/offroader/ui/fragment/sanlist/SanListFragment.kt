package com.mit.offroader.ui.fragment.sanlist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.mit.offroader.databinding.FragmentSanListBinding
import com.mit.offroader.ui.activity.sandetail.SanDetailActivity
import com.mit.offroader.ui.fragment.sanlist.adapter.SanListAdapter
import com.mit.offroader.ui.fragment.sanlist.model.SanDTO
import com.mit.offroader.ui.fragment.sanlist.viewmodel.SanListViewModel
import com.mit.offroader.ui.fragment.sanlist.viewmodel.SanListViewModelFactory
import com.mit.offroader.utils.Application

private const val TAG = "SanListFragment"

class SanListFragment : Fragment() {

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

        setInitiallySelectedItem()


    }

    private fun clickListener(sanName: String?) {
        binding.ivSelectedImage.setOnClickListener {
            Log.d(TAG,"Selected Image")
            val intent = Intent(requireActivity(), SanDetailActivity::class.java)
//            intent.putExtra("name",sanName)
            startActivity(intent)

        }
        binding.chipCategory
    }

    private fun setInitiallySelectedItem() {
        sanListViewModel.setInitiallySelectedItem()
    }


    private fun initObserve() {
        sanListViewModel.sanListUiState.observe(viewLifecycleOwner) {
            Log.d(TAG, "sanListUiState OBSERVED ${it?.selectedItem}")
            if (it?.selectedItem == null) {
                sanListViewModel.getSelectedItem(null)
                clickListener(it!!.selectedItem?.sanName)
            } else {
                it.selectedItem?.let { it1 -> setSelectedSan(it1) }
                sanListViewModel.updateSelectedItemOnDTO(it.selectedItem!!)
            }
        }
        sanListViewModel.sanList.observe(viewLifecycleOwner) {
            Log.d(TAG, "sanListRepository OBSERVED")
            sanListAdapter.submitList(it)
        }
    }

    @SuppressLint("SetTextI18n") //하드코딩 하지말라는 경고를 타이틀 어노테이션을 통해 무시함.
    private fun setSelectedSan(selectedItem: SanDTO) {
        Log.d(TAG, "setSelectedSan 화면 구성 $selectedItem")

        Glide.with(this).load(selectedItem.sanImage?.get(0)).into(binding.ivSelectedImage)

        val height = "${selectedItem.sanHeight?.div(1000)},${selectedItem.sanHeight?.rem(1000)}m"
        val timeTaken =
            "${selectedItem.sanTimeTotal?.div(60)}h ${selectedItem.sanTimeTotal?.rem(60)}min"
        val difficulty = when (selectedItem.sanDifficulty?.toInt()) {
            0 -> getString(SanListString.EASY.string)
            1 -> getString(SanListString.INTERMEDIATE.string)
            else -> getString(SanListString.HARD.string)
        }
        val divider = getString(SanListString.DIVIDER.string)
        binding.tvSanName.text = selectedItem.sanName
        binding.tvSanInfo.text = "$height $divider $timeTaken $divider $difficulty"
    }

    private fun setRecyclerViewGridLayout() {
        Log.d(TAG, "setRecyclerViewGridLayout")
        val gridLayoutManager = GridLayoutManager(requireContext(), 4)
        binding.rvSanList.layoutManager = gridLayoutManager
    }


}
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
import com.mit.offroader.ui.fragment.chatbot.MyApplication
import com.mit.offroader.ui.fragment.sanlist.adapter.SanListAdapter
import com.mit.offroader.ui.fragment.sanlist.model.SanDTO
import com.mit.offroader.ui.fragment.sanlist.viewmodel.SanListViewModel
import com.mit.offroader.ui.fragment.sanlist.viewmodel.SanListViewModelFactory

private const val TAG = "SanListFragment"

class SanListFragment : Fragment() {

    private var _binding: FragmentSanListBinding? = null
    private val binding get() = _binding!!
    private val sanListAdapter: SanListAdapter by lazy { SanListAdapter(sanListViewModel) }
    private val sanListViewModel: SanListViewModel by viewModels {
        SanListViewModelFactory((requireActivity().application as MyApplication).sanListRepository)

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


    private fun setInitiallySelectedItem() {
        sanListViewModel.setInitiallySelectedItem()
    }


    private fun initObserve() {

        // sanListUiState 안에 있는 selectedItem 변수는 리사이클러뷰에서 선택된 아이템이다!
        sanListViewModel.sanListUiState.observe(viewLifecycleOwner) {
            Log.d(TAG, "sanListUiState OBSERVED ${it?.selectedItem}")
            if (it?.selectedItem == null) {

                sanListViewModel.getSelectedItem(null)      // 선택된 아이템의 테두리를 추가해주기 위해서 산 디티오의 selectedItem의 값을 확인해주는 함수
                clickListener(binding.tvSanName.text.toString())        // 텍스트 클릭 시 디테일 액티비티로 넘어갈 수 있도록 구현
            } else {
                it.selectedItem?.let { it1 -> setSelectedSan(it1) }
                sanListViewModel.updateSelectedItemOnDTO(it.selectedItem!!)  // 산 디티오 옵져빙 후 업데이트 시켜주기
                clickListener(it.selectedItem?.sanName)
            }
        }
        sanListViewModel.sanList.observe(viewLifecycleOwner) {
            Log.d(TAG, "sanListRepository OBSERVED")
            sanListAdapter.submitList(it)
        }
    }

    @SuppressLint("SetTextI18n") //하드코딩 하지말라는 경고를 타이틀 어노테이션을 통해 무시함.

    // 선택된 산 디테일 정보를 표시해주는 코드
    private fun setSelectedSan(selectedItem: SanDTO) {
        Log.d(TAG, "setSelectedSan 화면 구성 $selectedItem")

        Glide.with(this).load(selectedItem.sanImage?.get(0)).into(binding.ivSelectedImage)

        val height = getHeightToString(selectedItem.sanHeight?.toInt())
        val timeTaken = getTimeTakenToString(selectedItem)
        val difficulty = getDifficultyToString(selectedItem)
        val divider = getString(SanListString.DIVIDER.string)

        binding.tvSanName.text = selectedItem.sanName
        binding.tvSanInfo.text = "$height $divider $timeTaken $divider $difficulty"
    }

    private fun getDifficultyToString(selectedItem: SanDTO): String {
        return when (selectedItem.sanDifficulty?.toInt()) {
            1 -> getString(SanListString.EASY.string)
            2 -> getString(SanListString.INTERMEDIATE.string)
            else -> getString(SanListString.HARD.string)
        }
    }

    private fun getTimeTakenToString(selectedItem: SanDTO): String {
        return if (selectedItem.sanTimeTotal?.rem(60)?.toInt() == 0) {
            "${selectedItem.sanTimeTotal?.div(60)}h"
        } else {
            "${selectedItem.sanTimeTotal?.div(60)}h ${selectedItem.sanTimeTotal?.rem(60)}min"
        }
    }

    private fun getHeightToString(selectedItem: Int?): String {
        return if (selectedItem?.div(1000) == 0) {
            "${selectedItem}m"
        } else {

            if (selectedItem?.rem(1000)?.div(100) == 0) {

                if (selectedItem.rem(100).div(10)== 0) {

                    if (selectedItem.rem(10) == 0) {
                        "${selectedItem.div(1000)},000m"
                    } else {
                        "${selectedItem.div(1000)},00${selectedItem.rem(1000)}m"
                    }

                } else {
                    "${selectedItem.div(1000)},0${selectedItem.rem(1000)}m"
                }

            } else {
                "${selectedItem?.div(1000)},${selectedItem?.rem(1000)}m"
            }

        }
    }

    private fun setRecyclerViewGridLayout() {
        Log.d(TAG, "setRecyclerViewGridLayout")
        val gridLayoutManager = GridLayoutManager(requireContext(), 4)
        binding.rvSanList.layoutManager = gridLayoutManager
    }


    private fun clickListener(sanName: String?) {

        // 디테일 액티비티로 넘어감.
        binding.ivSelectedImage.setOnClickListener {
            Log.d(TAG, "Selected Image $sanName")
            val intent = Intent(requireActivity(), SanDetailActivity::class.java)

            if (sanName == null) {
                intent.putExtra("name", "계룡산")
            } else {
                intent.putExtra("name", sanName)
            }
            startActivity(intent)

        }
        binding.chipCategory
    }
}
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
                clickListener(it!!.selectedItem?.sanName)        // 텍스트 클릭 시 디테일 액티비티로 넘어갈 수 있도록 구현
            } else {
                it.selectedItem?.let { it1 -> setSelectedSan(it1) }
                sanListViewModel.updateSelectedItemOnDTO(it.selectedItem!!)  // 산 디티오 옵져빙 후 업데이트 시켜주기
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


    private fun clickListener(sanName: String?) {

        // 디테일 액티비티로 넘어감.
        binding.tvSanName.setOnClickListener {
            Log.d(TAG,"Selected Image")
            val intent = Intent(requireActivity(), SanDetailActivity::class.java)
            intent.putExtra("name",sanName)
            startActivity(intent)

        }
        binding.chipCategory
    }
}
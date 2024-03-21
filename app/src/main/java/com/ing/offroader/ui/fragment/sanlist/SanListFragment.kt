package com.ing.offroader.ui.fragment.sanlist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.ing.offroader.databinding.FragmentSanListBinding
import com.ing.offroader.ui.activity.sandetail.SanDetailActivity
import com.ing.offroader.ui.fragment.community.MyApplication
import com.ing.offroader.ui.fragment.sanlist.adapter.SanListAdapter
import com.ing.offroader.ui.fragment.sanlist.model.SanDTO
import com.ing.offroader.ui.fragment.sanlist.viewmodel.SanListViewModel
import com.ing.offroader.ui.fragment.sanlist.viewmodel.SanListViewModelFactory

private const val TAG = "SanListFragment"

class SanListFragment : Fragment() {

    private var _binding: FragmentSanListBinding? = null
    private val binding get() = _binding!!
    private val sanListAdapter: SanListAdapter by lazy { SanListAdapter(sanListViewModel) }
    private val sanListViewModel: SanListViewModel by viewModels {
        SanListViewModelFactory((requireActivity().application as MyApplication).sanListRepository)
    }

    private var startTime : Long? = null
    private var endTime : Long? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSanListBinding.inflate(inflater, container, false)

        startTime = System.nanoTime()

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
        // 카테고리 리스트 스피너 처리
        setCategorySpinner()
    }

    private fun setCategorySpinner() = with(binding.spCategory) {
        adapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            listOf(
                "가나다순"
            )
        )

        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }


    }


    private fun setInitiallySelectedItem() {
        sanListViewModel.setInitiallySelectedItem()
    }


    private fun initObserve() {

        // sanListUiState 안에 있는 selectedItem 변수는 리사이클러뷰에서 선택된 아이템이다!
        sanListViewModel.sanListUiState.observe(viewLifecycleOwner) {
//            Log.d(TAG, "sanListUiState OBSERVED ${it?.selectedItem}")
            if (it?.selectedItem == null) {

                // 선택된 아이템의 테두리를 추가해주기 위해서 산 디티오의 selectedItem의 값을 확인해주는 함수
                sanListViewModel.getSelectedItem(null)

                // 텍스트 클릭 시 디테일 액티비티로 넘어갈 수 있도록 구현
                clickListener(binding.tvSanName.text.toString())

            } else {

                it.selectedItem?.let { it1 -> setSelectedSan(it1) }

                // 산 디티오 옵져빙 후 업데이트 시켜주기
                sanListViewModel.updateSelectedItemOnDTO(it.selectedItem!!)
                clickListener(it.selectedItem?.sanName)
            }
        }
        sanListViewModel.sanList.observe(viewLifecycleOwner) {
//            Log.d(TAG, "sanListRepository OBSERVED")
            sanListAdapter.submitList(it)
            endTime = System.nanoTime()
            val duration = endTime!! - startTime!!

            // 결과 로그 출력
            //        Log.d(TAG, "시간: ${startTime}ns, ${endTime}ns")
            Log.d(TAG, "프래그먼트로 데이터 전송에 걸린 시간: ${duration}ns")
        }



    }

    @SuppressLint("SetTextI18n") //하드코딩 하지말라는 경고를 타이틀 어노테이션을 통해 무시함.

    // 선택된 산 디테일 정보를 표시해주는 코드
    private fun setSelectedSan(selectedItem: SanDTO) {
//        Log.d(TAG, "setSelectedSan 화면 구성 $selectedItem")

        Glide.with(this).load(selectedItem.sanImage?.get(0)).into(binding.ivSelectedImage)

        val height = getHeightToString(selectedItem.sanHeight?.toInt())
        val timeTaken = getTimeTakenToString(selectedItem)
        val difficulty = getDifficultyToString(selectedItem)
        val divider = getString(SanListString.DIVIDER.string)

        binding.tvSanName.text = selectedItem.sanName
        binding.tvSanInfo.text = "$height $divider $timeTaken $divider $difficulty"
    }

    // 상중하 문자열로 변환하는 로직
    private fun getDifficultyToString(selectedItem: SanDTO): String {
        return when (selectedItem.sanDifficulty?.toInt()) {
            1 -> getString(SanListString.EASY.string)
            2 -> getString(SanListString.INTERMEDIATE.string)
            else -> getString(SanListString.HARD.string)
        }
    }

    // 걸린 시간 문자열로 변환하는 로직
    private fun getTimeTakenToString(selectedItem: SanDTO): String {
        return if (selectedItem.sanTimeTotal?.rem(60)?.toInt() == 0) {
            "${selectedItem.sanTimeTotal.div(60)}h"
        } else {
            "${selectedItem.sanTimeTotal?.div(60)}h ${selectedItem.sanTimeTotal?.rem(60)}min"
        }
    }

    // 산 높이 문자열로 변환하는 로직
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

    // 리사이클러뷰 그리드 레이아웃으로 설정하는 코드
    private fun setRecyclerViewGridLayout() {
//        Log.d(TAG, "setRecyclerViewGridLayout")
        val gridLayoutManager = GridLayoutManager(requireContext(), 4)
        binding.rvSanList.layoutManager = gridLayoutManager

        // 깜ㅃ
        binding.rvSanList.apply {
            itemAnimator = null
        }
    }


    // 리스너 콜백 함수들 모아놓은 함수
    private fun clickListener(sanName: String?) {

        // 디테일 액티비티로 넘어감.
        binding.ivSelectedImage.setOnClickListener {
//            Log.d(TAG, "Selected Image $sanName")
            val intent = Intent(requireActivity(), SanDetailActivity::class.java)

            if (sanName == null) {
                intent.putExtra("name", "계룡산")
            } else {
                intent.putExtra("name", sanName)
            }
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}
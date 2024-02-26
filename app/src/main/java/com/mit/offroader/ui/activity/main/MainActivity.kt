package com.mit.offroader.ui.activity.main

import android.graphics.drawable.Drawable.ConstantState
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Constants
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.mit.offroader.R
import com.mit.offroader.databinding.ActivityMainBinding
import com.mit.offroader.ui.fragment.chatbot.ChatBotFragment
import com.mit.offroader.ui.fragment.home.HomeFragment
import com.mit.offroader.ui.fragment.map.MapFragment
import com.mit.offroader.ui.fragment.mydetail.MyDetailFragment
import com.mit.offroader.ui.fragment.sanlist.SanListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = binding.navMain

        replaceFragment(HomeFragment())

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            // 아이콘 색상 변경

            // 각 아이템에 따라 적절한 작업 수행
            when (menuItem.itemId) {
                R.id.navigation_1 -> {
                    replaceFragment(HomeFragment())
                    //애니메이션 쓸거면 여기
                    true
                }
                R.id.navigation_2 -> {
                    replaceFragment(SanListFragment())
                    true
                }
                R.id.navigation_3 -> {
                    replaceFragment(MapFragment())
                    true
                }
                R.id.navigation_4 -> {
                    replaceFragment(ChatBotFragment())
                    true
                }
                R.id.navigation_5 -> {
                    replaceFragment(MyDetailFragment())
                    true
                }
                else -> false
            }
        }

        //bottomSheetControl()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_main, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }


    // Bottom Sheet 상태 변화에(드래그 중, 사라졌을 때 등) 따라 기능 수행
//    private fun bottomSheetControl() {
//        val test: BottomSheetBehavior<View> = BottomSheetBehavior.from(binding.bslMain.clBottomSheet)
//        test.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
//                    //binding.bslMain.ablRadio.visibility = View.VISIBLE
//                }
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//               // binding.bslMain.ablRadio.alpha = slideOffset
//            }
//
//        })
//    }
}

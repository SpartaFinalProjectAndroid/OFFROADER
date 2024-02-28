package com.mit.offroader.ui.activity.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.mit.offroader.R
import com.mit.offroader.databinding.ActivityMainBinding
import com.mit.offroader.ui.fragment.chatbot.ChatBotFragment
import com.mit.offroader.ui.fragment.home.HomeFragment
import com.mit.offroader.ui.fragment.map.SanMapFragment
import com.mit.offroader.ui.fragment.mydetail.MyDetailFragment
import com.mit.offroader.ui.fragment.sanlist.SanListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mit.offroader.ui.activity.main.adapters.RadioListAdapter

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
                    replaceFragment(SanMapFragment())
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
        radioSetting()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_main, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    // 각 방송국의 라디오 채널 리스트 초기화
    private fun radioSetting() {
        val test : ArrayList<String> = arrayListOf("test", "test2", "", "","","","","","")
        binding.rvChannelList.adapter = RadioListAdapter(test)

        with(binding) {
            cvFavorites.setOnClickListener {
                val test2 : ArrayList<String> = arrayListOf("test", "test2", "", "")
                rvChannelList.adapter = RadioListAdapter(test2)

            }

            cvKbs.setOnClickListener {
                val test3 : ArrayList<String> = arrayListOf("test", "test2", "", "","","","")
                rvChannelList.adapter = RadioListAdapter(test3)
                ivRadioProfile.setImageResource(R.drawable.ic_kbs_radio)
            }

            cvSbs.setOnClickListener {
                val test4 : ArrayList<String> = arrayListOf("test", "test2")
                rvChannelList.adapter = RadioListAdapter(test4)
                ivRadioProfile.setImageResource(R.drawable.ic_sbs_radio)
            }

            cvMbc.setOnClickListener {
                val test5 : ArrayList<String> = arrayListOf("test", "test2", "", "","")
                rvChannelList.adapter = RadioListAdapter(test5)
                ivRadioProfile.setImageResource(R.drawable.ic_mbc_radio)
            }

        }
    }

}

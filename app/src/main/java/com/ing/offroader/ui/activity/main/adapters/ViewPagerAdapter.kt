package com.ing.offroader.ui.activity.main.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ing.offroader.ui.fragment.community.CommunityFragment
import com.ing.offroader.ui.fragment.home.HomeFragment
import com.ing.offroader.ui.fragment.mydetail.MyDetailFragment
import com.ing.offroader.ui.fragment.sanlist.SanListFragment
import com.naver.maps.map.MapFragment

class ViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> SanListFragment()
            2 -> MapFragment()
            3 -> CommunityFragment()
            else -> MyDetailFragment()
        }
    }
}
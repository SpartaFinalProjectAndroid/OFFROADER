package com.ing.offroader.ui.activity.main.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.bumptech.glide.manager.Lifecycle
import com.ing.offroader.ui.fragment.community.CommunityFragment
import com.ing.offroader.ui.fragment.home.HomeFragment
import com.ing.offroader.ui.fragment.map.SanMapFragment
import com.ing.offroader.ui.fragment.mydetail.MyDetailFragment
import com.ing.offroader.ui.fragment.sanlist.SanListFragment
import com.naver.maps.map.MapFragment

class ViewPagerAdapter(fragment: FragmentActivity)
    : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 5


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> SanListFragment()
            2 -> SanMapFragment()
            3 -> CommunityFragment()
            else -> MyDetailFragment()
        }
    }

    override fun onBindViewHolder(
        holder: FragmentViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }

}
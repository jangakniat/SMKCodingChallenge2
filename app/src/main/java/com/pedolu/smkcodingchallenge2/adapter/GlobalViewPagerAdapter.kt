package com.pedolu.smkcodingchallenge2.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pedolu.smkcodingchallenge2.fragment.globalstatus.GlobalConfirmedFragment
import com.pedolu.smkcodingchallenge2.fragment.globalstatus.GlobalDeathFragment
import com.pedolu.smkcodingchallenge2.fragment.globalstatus.GlobalRecoveredFragment

class GlobalViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val menuSize = 3
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                GlobalConfirmedFragment()
            }
            1 -> {
                GlobalRecoveredFragment()
            }
            2 -> {
                GlobalDeathFragment()
            }
            else -> {
                GlobalConfirmedFragment()
            }
        }
    }

    override fun getItemCount() = menuSize
}
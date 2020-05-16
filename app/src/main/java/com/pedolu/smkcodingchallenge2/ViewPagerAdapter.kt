package com.pedolu.smkcodingchallenge2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val menuSize = 3
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                GlobalFragment()
            }
            1 -> {
                LocalFragment()
            }
            2 -> {
                IndonesiaFragment()
            }
            else -> {
                GlobalFragment()
            }
        }
    }

    override fun getItemCount() = menuSize
}
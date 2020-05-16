package com.pedolu.smkcodingchallenge2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.global -> {
                val fragment = GlobalFragment()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.local -> {
                val fragment = LocalFragment()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.indonesia -> {
                val fragment = IndonesiaFragment()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .replace(R.id.content, fragment, fragment.javaClass.simpleName)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val fragment = GlobalFragment()
        addFragment(fragment)
    }
}

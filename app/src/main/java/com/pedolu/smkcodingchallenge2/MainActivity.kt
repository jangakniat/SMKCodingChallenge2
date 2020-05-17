package com.pedolu.smkcodingchallenge2

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.pedolu.smkcodingchallenge2.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val menuTeks = arrayOf("Global", "Local", "Indonesia", "Status")
    private val menuIcon = arrayOf(
        R.drawable.ic_global, R.drawable.ic_local,
        R.drawable.ic_home, R.drawable.ic_global
    )
    private lateinit var session: UserSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        session = UserSession(this)
        checkLogin()
        val adapter =
            ViewPagerAdapter(this)
        viewPager.adapter = adapter
        TabLayoutMediator(tabsLayout, viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = menuTeks[position]
                tab.icon = ResourcesCompat.getDrawable(
                    resources,
                    menuIcon[position], null
                )
            }).attach()
        viewPager.isUserInputEnabled = false
        tabsLayout.getTabAt(0)!!.icon!!.setColorFilter(
            resources.getColor(R.color.colorAccent),
            PorterDuff.Mode.SRC_IN
        )
        tabsLayout.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.icon!!.setColorFilter(
                    resources.getColor(R.color.colorAccent),
                    PorterDuff.Mode.SRC_IN
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.icon!!.setColorFilter(
                    resources.getColor(R.color.colorWhite),
                    PorterDuff.Mode.SRC_IN
                )
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun checkLogin() {
        if (session.checkLogin()) {
            goToLoginActivity()
        }
    }

    private fun goToLoginActivity() {
        val i = Intent(this, LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }

    fun onLogoutAction(mi: MenuItem?) {
        session.logoutUser()
    }

}

package cn.haohao.dbbook.presentation.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.Menu
import android.view.MenuItem
import cn.haohao.dbbook.R
import cn.haohao.dbbook.presentation.fragment.BaseFragment
import cn.haohao.dbbook.presentation.fragment.BookListFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.backgroundColor

class MainActivity : BaseActivity(){

    val fragments = ArrayList<BaseFragment>()
    val titles = arrayListOf("热门", "新书", "小说", "科幻", "文学", "其他")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initNavView()
        initData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            startActivity(Intent(this, SearchActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (floatingNavView.isOpened) {
            floatingNavView.close()
        } else {
            super.onBackPressed()
        }
    }

    private fun initData() {
        titles.forEach {
            fragments.add(BookListFragment.getInstance(it) as BaseFragment)
        }

        viewPager.adapter = HomePagerAdapter(fragments, titles, supportFragmentManager)
        viewPager.offscreenPageLimit = 5
        //viewPager.currentItem = 2
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE)
    }

    private fun initNavView() {
        floatingNavView.navigationMenuView.background = getDrawable(R.drawable.bg_nav_menu_view)
        floatingNavView.setOnClickListener {
            floatingNavView.open()
        }
        floatingNavView.setNavigationItemSelectedListener { menuItem ->
            floatingNavView.close()
            if (menuItem.itemId == R.id.nav_about) {
                Handler().postDelayed({
                    startActivity(Intent(this, AboutActivity::class.java))
                }, 300)
            }
            true
        }
        floatingNavView.backgroundColor = resources.getColor(R.color.colorAccent)
    }

    class HomePagerAdapter(val framents: List<BaseFragment>, val titles: ArrayList<String>, fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

        override fun getItem(position: Int): Fragment = framents[position]

        override fun getCount(): Int = titles.size

        override fun getPageTitle(position: Int): CharSequence = titles[position]

    }

}

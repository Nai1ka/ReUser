package ru.ndevelop.reuser.ui.faqPage

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.ndevelop.reuser.R




const val NUM_PAGES = 3

class FaqActivity : FragmentActivity(),View.OnClickListener {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabDots:TabLayout
    private lateinit var skipText:TextView
    private lateinit var okText:TextView
    private val fragmentArray = arrayOf(FaqFragment.newInstance(1),FaqFragment.newInstance(2),FaqFragment.newInstance(3))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)
        viewPager = findViewById(R.id.pager)
        tabDots = findViewById(R.id.tabDots)
        skipText = findViewById(R.id.tv_skip_faq)
        okText = findViewById(R.id.tv_ok_faq)
        okText.setOnClickListener(this)
        skipText.setOnClickListener(this)
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter
        TabLayoutMediator(tabDots, viewPager) { tab, _ ->
            viewPager.setCurrentItem(tab.position, true)
        }.attach()
        tabDots.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position ?: 0){
                    NUM_PAGES-1 ->  okText.visibility = View.VISIBLE
                    else ->  okText.visibility = View.GONE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }


    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment{
            return fragmentArray[position]
        }

        override fun onBindViewHolder(
            holder: FragmentViewHolder,
            position: Int,
            payloads: MutableList<Any>
        ) {
            super.onBindViewHolder(holder, position, payloads)

        }

    }

    override fun onBackPressed() {

    }

    override fun onClick(v: View?) {
        when(v){
            skipText,okText ->{
                finish()
            }
        }
    }
}


package com.oksisi213.circularviewpager

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
	companion object {
		val TAG = MainActivity::class.java.simpleName
	}


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		view_pager.adapter = CircularPagerAdapter()
		tab_layout.addTab(tab_layout.newTab().setText("title 00"))
		tab_layout.addTab(tab_layout.newTab().setText("title 01"))
		tab_layout.addTab(tab_layout.newTab().setText("title 02"))
		tab_layout.addTab(tab_layout.newTab().setText("title 03"))
		tab_layout.addTab(tab_layout.newTab().setText("title 04"))


		view_pager.addOnPageChangeListener(pageChangeListener)
		tab_layout.addOnTabSelectedListener(onTabSelectedListener)


	}


	class CircularPagerAdapter : PagerAdapter() {


		lateinit var context: Context

		val views: ArrayList<View> by lazy {
			val list = ArrayList<View>()

			for (i in 0 until 5) {
				list.add(CheckBox(context).apply {
					text = "$i"
				})
			}

			list
		}

		init {

		}

		override fun isViewFromObject(view: View, obj: Any): Boolean = view == obj as View

		override fun getCount(): Int = 100
		override fun instantiateItem(container: ViewGroup, position: Int): Any {
			context = container.context
			return views[position]
		}

		override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
			super.destroyItem(container, position, view)
			container.removeView(view as View)
		}

	}

	val onTabSelectedListener: TabLayout.OnTabSelectedListener = object : TabLayout.OnTabSelectedListener {
		override fun onTabReselected(tab: TabLayout.Tab?) {
		}

		override fun onTabUnselected(tab: TabLayout.Tab?) {
		}

		override fun onTabSelected(tab: TabLayout.Tab?) {
			view_pager.removeOnPageChangeListener(pageChangeListener)
			view_pager.setCurrentItem(tab!!.position, true)
			view_pager.addOnPageChangeListener(pageChangeListener)
		}

	}

	val pageChangeListener: ViewPager.SimpleOnPageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
		override fun onPageSelected(position: Int) {
			super.onPageSelected(position)
			tab_layout.removeOnTabSelectedListener(onTabSelectedListener)
			tab_layout.getTabAt(position % 5)?.select()
			tab_layout.addOnTabSelectedListener(onTabSelectedListener)

		}
	}

}

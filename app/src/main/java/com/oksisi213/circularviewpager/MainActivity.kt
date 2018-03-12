package com.oksisi213.circularviewpager

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
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
		view_pager.currentItem = 5000
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

		override fun isViewFromObject(view: View, obj: Any): Boolean {
			return view == obj as View
		}

		override fun getCount(): Int = Integer.MAX_VALUE
		override fun instantiateItem(container: ViewGroup, position: Int): Any {
			Log.e(TAG, "instantiateItem=$position")
			context = container.context


			val fakePosition = position % 5

			if (container.indexOfChild(views[fakePosition]) != -1) {
				container.removeView(views[fakePosition])
			}
			container.addView(views[fakePosition])

			return views[fakePosition]
		}

		override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
			Log.e(TAG, "destroyItem=$position")
//			container.removeView(view as View)
		}

	}

	val onTabSelectedListener: TabLayout.OnTabSelectedListener = object : TabLayout.OnTabSelectedListener {
		override fun onTabReselected(tab: TabLayout.Tab?) {
		}

		override fun onTabUnselected(tab: TabLayout.Tab?) {
		}

		override fun onTabSelected(tab: TabLayout.Tab?) {
			Log.e(TAG, "onTabSelected = ${tab!!.position} page =${view_pager.currentItem}")
			view_pager.removeOnPageChangeListener(pageChangeListener)
			tab_layout.removeOnTabSelectedListener(this)

			val pagerPosition = view_pager.currentItem
			val tabPosition = tab!!.position
			val fakePagerPosition = view_pager.currentItem % 5

			if (tabPosition % 5 == fakePagerPosition) {
				Log.e(TAG, "do nothing")
				return
			}

			val positionDiff = Math.abs(fakePagerPosition - tabPosition)
			Log.e(TAG, "positionDiff = $positionDiff")


			if (fakePagerPosition > tabPosition) {
				for (i in 0 until positionDiff) {
					Log.e(TAG, "decrease")
					view_pager.currentItem--
				}
			} else if (tabPosition > fakePagerPosition) {
				for (i in 0 until positionDiff) {
					Log.e(TAG, "increase")
					view_pager.currentItem++
				}
			}

			tab_layout.addOnTabSelectedListener(this)
			view_pager.addOnPageChangeListener(pageChangeListener)
		}

	}

	val pageChangeListener: ViewPager.SimpleOnPageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
		override fun onPageSelected(position: Int) {
			super.onPageSelected(position)
			Log.e(TAG, "onPageSelected = $position")
			tab_layout.removeOnTabSelectedListener(onTabSelectedListener)
			tab_layout.getTabAt(position % 5)?.select()
			tab_layout.addOnTabSelectedListener(onTabSelectedListener)
		}
	}

}

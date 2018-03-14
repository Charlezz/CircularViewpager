package com.oksisi213.circularviewpager

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class SecondActivity : AppCompatActivity() {

	companion object {
		val TAG = SecondActivity::class.java.simpleName
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_second)

		view_pager.adapter = FragmentCircularPagerAdapter(supportFragmentManager)
		tab_layout.addTab(tab_layout.newTab().setText("title 00"))
		tab_layout.addTab(tab_layout.newTab().setText("title 01"))
		tab_layout.addTab(tab_layout.newTab().setText("title 02"))
		tab_layout.addTab(tab_layout.newTab().setText("title 03"))
		tab_layout.addTab(tab_layout.newTab().setText("title 04"))

		view_pager.addOnPageChangeListener(pageChangeListener)
		tab_layout.addOnTabSelectedListener(onTabSelectedListener)
		view_pager.currentItem = 5000


	}


	class FragmentCircularPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

		val fm: FragmentManager = fm


		val items = ArrayList<MyFragment>().apply {
			for (i in 0 until 5) {
				add(MyFragment.newInstance("$i"))
			}
		}

		override fun getItem(position: Int): Fragment = items[position % 5]


		override fun getCount(): Int = Integer.MAX_VALUE

		override fun instantiateItem(container: ViewGroup, position: Int): Any {
			val fakePosition = position % 5
			if (!items[fakePosition].isDetached) {
				fm.beginTransaction().detach(items[fakePosition])
			}
			return super.instantiateItem(container, position % 5)
		}

		override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
			super.destroyItem(container, position, obj)
		}


	}

	class MyFragment : Fragment() {
		companion object {
			fun newInstance(arg: String): MyFragment {
				var bundle = Bundle()
				bundle.putString("arg", arg)
				var f = MyFragment()
				f.arguments = bundle
				return f
			}
		}

		var data: String? = null

		override fun onCreate(savedInstanceState: Bundle?) {
			super.onCreate(savedInstanceState)
			data = arguments?.getString("arg")
		}

		override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
			val tv = TextView(container?.context)
			tv.text = data
			return tv
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

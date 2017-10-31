package com.wiatec.blive.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by patrick on 24/05/2017.
 * create time : 3:58 PM
 */
class FragmentAdapter(fragmentManager:FragmentManager, private val fragmentList: List<Fragment>)
    : FragmentPagerAdapter(fragmentManager) {

    private val titles: Array<String> = arrayOf("Live", "Record")

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = fragmentList.size

    override fun getPageTitle(position: Int): CharSequence = titles[position]
}
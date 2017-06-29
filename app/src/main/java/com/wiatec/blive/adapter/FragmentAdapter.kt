package com.wiatec.blive.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by patrick on 24/05/2017.
 * create time : 3:58 PM
 */
class FragmentAdapter(fragmentManager:FragmentManager , val fragmentList: List<Fragment>)
    : FragmentPagerAdapter(fragmentManager) {


    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }
}
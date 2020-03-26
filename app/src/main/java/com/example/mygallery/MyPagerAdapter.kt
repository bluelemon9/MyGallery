package com.example.mygallery

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MyPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm!!) {
    private val items = ArrayList<Fragment>()   // 뷰페이저가 표시할 프래그먼트 목록

    // position 위치의 프래그먼트
    override fun getItem(position: Int): Fragment {
        return items[position]
    }

    // 아이템 개수
    override fun getCount(): Int {
        return items.size
    }

    // 아이템 갱신
    fun updateFragment(items : List<Fragment>){
        this.items.addAll(items)
    }
}
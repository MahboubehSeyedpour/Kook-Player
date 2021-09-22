package com.example.kookplayer.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    val arrayList = ArrayList<Fragment>()

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun createFragment(position: Int): Fragment {
        // return fragment that corresponds to this 'position'
        return arrayList[position]
    }

    fun addFragment(fragment : Fragment){
        arrayList.add(fragment)
    }
}
package com.example.cassette.views.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.cassette.R
import com.example.cassette.adapter.ViewPagerFragmentAdapter
import com.example.cassette.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import github.com.st235.lib_expandablebottombar.ExpandableBottomBar


class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initTabs(view)
    }

    private fun initTabs(fragment: View) {

        val tabNames = resources.getStringArray(R.array.tabNames)

        val adapter = ViewPagerFragmentAdapter(requireActivity().supportFragmentManager, lifecycle)
        adapter.addFragment(LibraryFragment())
//        adapter.addFragment(RecentlyAdded())
        adapter.addFragment(PlaylistFragment())
        adapter.addFragment(FavoriteFragment())
        binding.viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.viewpager.adapter = adapter


        val bottomBar: ExpandableBottomBar =
            fragment.findViewById(R.id.expandable_toolbar_component)
        val menu = bottomBar.menu


        var selectedTab: TabLayout.Tab
        var selectedPosition: Int

        binding.toolbar.tabLayoutHome.visibility = View.GONE

        TabLayoutMediator(binding.toolbar.tabLayoutHome, binding.viewpager)
        { tab, position ->
            tab.text = tabNames[position]
            selectedTab = tab
            selectedPosition = position
        }.attach()


        binding.toolbar.expandableToolbar.expandableToolbarComponent.onItemSelectedListener =
            { view, menuItem, b ->
                when (menuItem.text) {
                    "songs" -> {
//                        Toast.makeText( baseContext, "Songs clicked", Toast.LENGTH_SHORT ).show()

                        binding.viewpager.setCurrentItem(0, true)
                    }

                    "Playlists" -> {

                        binding.viewpager.setCurrentItem(1, true)
                    }
                    "Favorite" -> {

                        binding.viewpager.setCurrentItem(2, true)
//                        binding.includeToolbar.tabLayoutHome.selectTab( binding.includeToolbar.tabLayoutHome.getTabAt( 3 ) )
                    }
                }
            }

        binding.toolbar.tabLayoutHome.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

                when (tab?.text) {
                    "Library" -> binding.toolbar.expandableToolbar.expandableToolbarComponent.menu.select(
                        R.id.songs
                    )
                    "Playlists" -> binding.toolbar.expandableToolbar.expandableToolbarComponent.menu.select(
                        R.id.playlist
                    )
                    "Favorite" -> binding.toolbar.expandableToolbar.expandableToolbarComponent.menu.select(
                        R.id.favorites
                    )
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "Library" -> binding.toolbar.expandableToolbar.expandableToolbarComponent.menu.select(
                        R.id.songs
                    )
                    "Playlists" -> binding.toolbar.expandableToolbar.expandableToolbarComponent.menu.select(
                        R.id.playlist
                    )
                    "Favorite" -> binding.toolbar.expandableToolbar.expandableToolbarComponent.menu.select(
                        R.id.favorites
                    )
                }

            }
        })
    }
}
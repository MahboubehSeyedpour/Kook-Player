 package com.example.cassette.views

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.cassette.R
import com.example.cassette.adapter.HomePageAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.component_tab.*

 class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar)

//        TODO( "implement hideStatusBar() function");


        val tabs = listOf(
            "library",
            "recently added",
            "playlists"
        )

        val res: Resources = resources
        val tabList = res.getStringArray(R.array.tabNames)

        val adapter = HomePageAdapter(tabList.asList())
        fragment_container.adapter = adapter

        TabLayoutMediator(homePageTabLayout, fragment_container)
        {
            tab, position -> tab.text = tabList[position]
        }.attach()

    }
}
package com.example.cassette.views

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LifecycleOwner
import com.example.cassette.R
import com.example.cassette.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.component_tab.*


class MainActivity : AppCompatActivity(), LifecycleOwner {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
//        setSupportActionBar(toolbar)


//        TODO( "implement hideStatusBar() function");

        val res: Resources = resources
        val tabList = res.getStringArray(R.array.tabNames)

        val adapter = ViewPagerAdapter(tabList.asList())
        viewpager_home.adapter = adapter


        TabLayoutMediator(tablayout_home, viewpager_home)
        { tab, position ->
            tab.text = tabList[position]
        }.attach()

        tablayout_home.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                Toast.makeText(
                    applicationContext,
                    "tab reselected: ${tab?.text}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Toast.makeText(
                    applicationContext,
                    "tab unselected: ${tab?.text}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                Toast.makeText(applicationContext, "tab selected: ${tab?.text}", Toast.LENGTH_SHORT)
                    .show()
            }
        })

//        val viewModel =
//            ViewModelProvider(this).get(com.example.cassette.datamodels.Songs::class.java)
//        viewModel.getMutableLiveData().observe(this, songListUpdateObserver)


        //temp: a button to check if the absolute path is correct
//        button.setOnClickListener {
//
//            File(FilePathUtlis.MUSIC_CANONICAL_PATH).walk().forEach {
//                if (it.isDirectory) {
//                    Toast.makeText(applicationContext, FileUtils.listfiles(it).toString(), Toast.LENGTH_SHORT).show()
//                }
//                if (it.isFile) {
//                    Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_SHORT).show()
//                }
////                Toast.makeText(applicationContext, FileUtils.listfiles(System.getProperty(FilePathUtlis.MUSIC_CANONICAL_PATH)), Toast.LENGTH_SHORT).show()
//            }
//        }

    }

//    val songListUpdateObserver: Observer<ArrayList<Song>> =
//        object : Observer<ArrayList<Song>> {
//            override fun onChanged(songArrayList: ArrayList<Song>?) {
//                val recyclerViewAdapter =
//                    RecyclerViewAdapter(this@MainActivity, songArrayList!!, R.layout.song_rv_item)
//                recyclerview.setLayoutManager(LinearLayoutManager(applicationContext))
//                recyclerview.setAdapter(recyclerViewAdapter)
//            }
//        }
}
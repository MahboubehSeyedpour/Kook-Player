 package com.example.cassette.views

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.widget.ViewPager2
import com.example.cassette.R
import com.example.cassette.adapter.ViewPagerFragmentAdapter
import com.example.cassette.utlis.MusicUtils
import com.example.cassette.views.Fragments.Favorite
import com.example.cassette.views.Fragments.Library
import com.example.cassette.views.Fragments.Playlist
import com.example.cassette.views.Fragments.RecentlyAdded
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.base.*
import kotlinx.android.synthetic.main.player_panel.*


 class MainActivity : AppCompatActivity(), LifecycleOwner{

    val PERMISSION_REQUEST = 111
     lateinit var mediaPlayer : MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mediaPlayer = MediaPlayer.create(this, R.raw.nafas)


//        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
//        setSupportActionBar(toolbar)


//        TODO( "implement hideStatusBar() function");

        val res: Resources = resources
        val tabList = res.getStringArray(R.array.tabNames)

//        val adapter = ViewPagerAdapter(tabList.asList())
//        viewpager_home.adapter = adapter


        val adapter = ViewPagerFragmentAdapter(supportFragmentManager, lifecycle)
        adapter.addFragment(Library())
        adapter.addFragment(RecentlyAdded())
        adapter.addFragment(Playlist())
        adapter.addFragment(Favorite())
        viewpager_home.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewpager_home.adapter = adapter


        TabLayoutMediator(tabLayout_home, viewpager_home)
        { tab, position ->
            tab.text = tabList[position]
        }.attach()

        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST
                )
            }
        } else {
            doStuff()
        }

        // bottomsheet manager
        val playerPanel = PlayerPanel ()
        playerPanel.setup(bottomSheet, baseContext)


        play_btn.setOnClickListener {

            if(!MusicUtils.mediaPlayer.isPlaying)
            {
                MusicUtils.mediaPlayer.start()
                play_btn.setImageResource(R.mipmap.ic_play_track_pic_foreground)
            }
            else
            {
                MusicUtils.mediaPlayer.pause()
                play_btn.setImageResource(R.mipmap.ic_pause_track_pic_foreground)
            }

        }




        ///////////////////////////////////////////////////////
//        tablayout_home.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//                Toast.makeText(
//                    applicationContext,
//                    "tab reselected: ${tab?.text}",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//                Toast.makeText(
//                    applicationContext,
//                    "tab unselected: ${tab?.text}",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                Toast.makeText(applicationContext, "tab selected: ${tab?.text}", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        })

//        val viewModel =
//            ViewModelProvider(this).get(com.example.cassette.datamodels.Songs::class.java)
//        viewModel.getMutableLiveData().observe(this, songListUpdateObserver)
//////////////////////////////////////

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_REQUEST -> if(grantResults.size>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show()
                    doStuff()
                }
                else{
                    Toast.makeText(this, "no permission granted", Toast.LENGTH_SHORT).show()
                    finish()
                }
                return
            }
        }

    }

    fun doStuff()
    {
        
    }
 }
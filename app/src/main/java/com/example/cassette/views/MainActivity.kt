package com.example.cassette.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.widget.ViewPager2
import com.example.cassette.R
import com.example.cassette.adapter.ViewPagerFragmentAdapter
import com.example.cassette.utlis.TimeUtils
import com.example.cassette.views.Fragments.*
import com.frolo.waveformseekbar.WaveformSeekBar
import com.google.android.material.tabs.TabLayoutMediator
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.base.*
import kotlinx.android.synthetic.main.component_toolbar.*
import kotlinx.android.synthetic.main.player_panel.*
import kotlinx.android.synthetic.main.player_remote.*
import kotlin.random.Random


class MainActivity : AppCompatActivity(), LifecycleOwner {

    val PERMISSIONS_REQUEST_CODE = 1

    val PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    var likeState: Boolean = false


    lateinit var currentMode: PlayerRemote.playerMode


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor", "WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PlayerRemote.setupRemote(applicationContext, music_album_image, music_title_tv)


        currentMode = PlayerRemote.playerMode.NORMAL


        seekBar.visibility = View.GONE


        waveform_seek_bar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener,
            WaveformSeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                waveform_seek_bar.setProgressInPercentage(0.25F)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onProgressInPercentageChanged(
                seekBar: WaveformSeekBar?,
                percent: Float,
                fromUser: Boolean
            ) {
                if (PlayerRemote.mediaPlayer.isPlaying) {
                    music_min.text = TimeUtils.milliSecToDuration(
                        (percent * TimeUtils.getDurationOfCurrentMusic().toLong()).toLong()
                    )
                }
            }

            override fun onStartTrackingTouch(seekBar: WaveformSeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: WaveformSeekBar?) {
                Toast.makeText(
                    baseContext,
                    "Tracked: percent=" + waveform_seek_bar.getProgressPercent(),
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
        waveform_seek_bar.setWaveform(createWaveform(), true)


        like_iv.setOnClickListener {
            likeState = when (likeState) {
                true -> {
                    like_iv.setImageResource(R.drawable.ic_heart)
                    false
                }
                false -> {
                    like_iv.setImageResource(R.drawable.ic_filled_heart)
                    true
                }
            }
        }


//        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
//        setSupportActionBar(toolbar)


//        TODO( "implement hideStatusBar() function");


//        val adapter = ViewPagerAdapter(tabList.asList())
//        viewpager_home.adapter = adapter

        val res: Resources = resources
        val tabNames = res.getStringArray(R.array.tabNames)

        val adapter = ViewPagerFragmentAdapter(supportFragmentManager, lifecycle)
        adapter.addFragment(Library())
        adapter.addFragment(RecentlyAdded())
        adapter.addFragment(Playlist())
        adapter.addFragment(Favorite())
        viewpager_home.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewpager_home.adapter = adapter


        TabLayoutMediator(tabLayout_home, viewpager_home)
        { tab, position ->
            tab.text = tabNames[position]
        }.attach()

        if (!hasPermissions(this, *PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_REQUEST_CODE)
        }


        // bottomsheet manager
//        val playerPanel = PlayerPanel_bottomSheet()
//        playerPanel.setup(this, bottomSheet, baseContext)

        val playerPanel = SlidingUpPanelLayout(baseContext)


        playerPanel.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {
            }

            override fun onPanelStateChanged(
                panel: View?,
                previousState: SlidingUpPanelLayout.PanelState?,
                newState: SlidingUpPanelLayout.PanelState?
            ) {
                when (newState?.name) {
                    "Collapsed" -> {
                        val i = 0
                        //action when collapsed
                    }
                    "Expanded" -> {
                        val i = 0
                        //action when expanded
                    }
                }
            }

        })


//        val mHandler = Handler()
//        runOnUiThread(object : Runnable {
//            override fun run() {
//                if (PlayerRemote.mediaPlayer != null) {
//                    val mCurrentPosition = PlayerRemote.mediaPlayer.currentPosition / 1000
//                    seekBar.setProgress(mCurrentPosition)
//                    seekBar.max = PlayerRemote.mediaPlayer.duration / 1000
//                }
//                mHandler.postDelayed(this, 1000)
//            }
//        })


        val mHandler1 = Handler()
        runOnUiThread(object : Runnable {
            override fun run() {
                if (PlayerRemote.mediaPlayer != null) {
                    val mCurrentPosition = PlayerRemote.mediaPlayer.currentPosition / 1000
                    if (mCurrentPosition > 0) {
                        waveform_seek_bar.setProgressInPercentage((mCurrentPosition / 100.0).toFloat())
                    }
                }
                mHandler1.postDelayed(this, 1000)
            }
        })

        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {

                music_min.text =
                    TimeUtils.milliSecToDuration((seekBar.max - progress).toLong())
//                textView.setText(progress.toString() + "/" + seekBar.max)
//                PlayerRemote.mediaPlayer.seekTo(progress * 1000)

                if (seekBar.max - progress <= 0) {
                    PlayerRemote.playNextMusic(currentMode)
                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}

        })


        next_btn.setOnClickListener() {
            PlayerRemote.playNextMusic(currentMode)
        }

        prev_btn.setOnClickListener {
            PlayerRemote.playPrevMusic(currentMode)
        }

        play_btn.setOnClickListener {

            if (!PlayerRemote.mediaPlayer.isPlaying) {
                PlayerRemote.player.resumePlaying()
            } else {
                PlayerRemote.player.pauseMusic()
                play_btn.setImageResource(R.drawable.ic_pause)
            }
            updateUI()
        }


        sort_iv.setOnClickListener {

            val bottomSheetDialog = Custom_BottomSheetDialogFragment.newInstance()
            bottomSheetDialog?.setStyle(
                R.style.AppBottomSheetDialogTheme,
                R.style.AppBottomSheetDialogTheme
            )
            bottomSheetDialog?.show(supportFragmentManager, "btmsheet")

        }

        shuffle_btn.setOnClickListener {
            when (currentMode) {
                PlayerRemote.playerMode.NORMAL -> {
                    currentMode = PlayerRemote.playerMode.SHUFFLE
                }

                PlayerRemote.playerMode.SHUFFLE -> {
                    currentMode = PlayerRemote.playerMode.NORMAL
                }
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


    private fun createWaveform(): IntArray? {
        val random = Random(System.currentTimeMillis())
        val length: Int = 50 + random.nextInt(50)
        val values = IntArray(length)
        var maxValue = 0
        for (i in 0 until length) {
            val newValue: Int = 5 + random.nextInt(50)
            if (newValue > maxValue) {
                maxValue = newValue
            }
            values[i] = newValue
        }
        return values
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(this, "permissions granted", Toast.LENGTH_SHORT).show()

//                    Library.notifyDataSetChanges()

                } else {
                    Toast.makeText(this, "no permissions granted", Toast.LENGTH_SHORT).show()
                    finish()
                }
                return
            }
        }
    }

    private fun updateUI() {
        music_max.text =
            TimeUtils.milliSecToDuration(TimeUtils.getDurationOfCurrentMusic().toLong())
    }
}
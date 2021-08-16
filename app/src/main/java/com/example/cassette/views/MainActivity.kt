package com.example.cassette.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.widget.ViewPager2
import com.example.cassette.R
import com.example.cassette.adapter.ViewPagerFragmentAdapter
import com.example.cassette.databinding.ActivityMainBinding
import com.example.cassette.views.Fragments.*
import com.google.android.material.tabs.TabLayoutMediator
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.base.*


class MainActivity : AppCompatActivity(), LifecycleOwner {


    companion object {
        var permissionsGranted: Boolean = false
    }

    private val PERMISSIONS_REQUEST_CODE = 1

    private val PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    lateinit var binding: ActivityMainBinding
    lateinit var playerPanelFragment: PlayerPanelFragment

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor", "WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //use viewBinding to bind the layout to the activity
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkForPermissions()
        initTabs()
        initBottomSheet()

//        TODO( "implement hideStatusBar() function");

        binding.slidingLayout.addPanelSlideListener(object :
            SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {
//                Toast.makeText(
//                    baseContext,
//                    "onPanelSlide",
//                    Toast.LENGTH_SHORT
//                ).show()
            }

            override fun onPanelStateChanged(
                panel: View?,
                previousState: SlidingUpPanelLayout.PanelState?,
                newState: SlidingUpPanelLayout.PanelState?
            ) {
                when (binding.slidingLayout.panelState) {
                    SlidingUpPanelLayout.PanelState.EXPANDED -> {
                        Toast.makeText(
                            baseContext,
                            "expanded",
                            Toast.LENGTH_SHORT
                        ).show()
                        playerPanelFragment.setVisibiliyForPlayerPanelHeaderOnExpanded(View.VISIBLE)
                        playerPanelFragment.setVisibiliyForPlayerPanelHeaderOnCollapsed(View.GONE)
                    }
                    SlidingUpPanelLayout.PanelState.COLLAPSED -> {
                        Toast.makeText(
                            baseContext,
                            "collapsed",
                            Toast.LENGTH_SHORT
                        ).show()
                        playerPanelFragment.setVisibiliyForPlayerPanelHeaderOnExpanded(View.GONE)
                        playerPanelFragment.setVisibiliyForPlayerPanelHeaderOnCollapsed(View.VISIBLE)
                    }
                }
            }
        })


        binding.includeBase.includeToolbar.sortIv.setOnClickListener {

            val bottomSheetDialog = Custom_BottomSheetDialogFragment.newInstance()
            bottomSheetDialog?.setStyle(
                R.style.AppBottomSheetDialogTheme,
                R.style.AppBottomSheetDialogTheme
            )
            bottomSheetDialog?.show(supportFragmentManager, "btmsheet")
        }

/*
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

        val viewModel =
            ViewModelProvider(this).get(com.example.cassette.datamodels.Songs::class.java)
        viewModel.getMutableLiveData().observe(this, songListUpdateObserver)
 */

/*
        //temp: a button to check if the absolute path is correct
        button.setOnClickListener {

            File(FilePathUtlis.MUSIC_CANONICAL_PATH).walk().forEach {
                if (it.isDirectory) {
                    Toast.makeText(applicationContext, FileUtils.listfiles(it).toString(), Toast.LENGTH_SHORT).show()
                }
                if (it.isFile) {
                    Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_SHORT).show()
                }
                Toast.makeText(applicationContext, FileUtils.listfiles(System.getProperty(FilePathUtlis.MUSIC_CANONICAL_PATH)), Toast.LENGTH_SHORT).show()
            }
        }

         */

    }

    private fun checkForPermissions() {
        if (!hasPermissions(this, *PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_REQUEST_CODE)
        }
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    permissionsGranted = true

                } else {
                    permissionsGranted = false
                    finish()
                }
                initTabs()
                initBottomSheet()
                return
            }
        }
    }

    private fun initTabs() {

//        val adapter = ViewPagerAdapter(tabList.asList())
//        viewpager_home.adapter = adapter


        val tabNames = resources.getStringArray(R.array.tabNames)

        val adapter = ViewPagerFragmentAdapter(supportFragmentManager, lifecycle)
        adapter.addFragment(Library(application))
        adapter.addFragment(RecentlyAdded())
        adapter.addFragment(Playlist())
        adapter.addFragment(Favorite())
        viewpager_home.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewpager_home.adapter = adapter


        TabLayoutMediator(binding.includeBase.includeToolbar.tabLayoutHome, viewpager_home)
        { tab, position ->
            tab.text = tabNames[position]
        }.attach()
    }


    private fun initBottomSheet() {
        playerPanelFragment = PlayerPanelFragment()
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.add(
            binding.bottomSheetContainer.id,
            playerPanelFragment,
            "bottom sheet container"
        )
            .commit()
    }
}
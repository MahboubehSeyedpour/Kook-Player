package com.example.cassette.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.example.cassette.databinding.ActivityMainBinding
import com.example.cassette.player.Enums
import com.example.cassette.providers.PermissionProvider
import com.example.cassette.utlis.SharedPrefUtils
import com.example.cassette.views.Fragments.MainFragment
import com.example.cassette.views.Fragments.PlayerPanelFragment
import com.sothree.slidinguppanel.SlidingUpPanelLayout


class MainActivity : AppCompatActivity(), LifecycleOwner {

    companion object {
        var permissionsGranted: Boolean = false
        lateinit var playerPanelFragment: PlayerPanelFragment
        lateinit var activity: MainActivity
        lateinit var sharedPreferences: SharedPreferences
    }

    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    lateinit var binding: ActivityMainBinding


    override fun onStop() {
        super.onStop()
        saveSettings()
    }

    fun saveSettings() {

        SharedPrefUtils.saveState()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor", "WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        sharedPreferences = getPreferences(Context.MODE_PRIVATE) ?: return



        activity = this

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkForPermissions()

        initMainFragment()

        initBottomSheet()


//        TODO( "implement hideStatusBar() function");

        binding.slidingLayout.addPanelSlideListener(object :
            SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {
            }

            override fun onPanelStateChanged(
                panel: View?,
                previousState: SlidingUpPanelLayout.PanelState?,
                newState: SlidingUpPanelLayout.PanelState?
            ) {
                when (binding.slidingLayout.panelState) {
                    SlidingUpPanelLayout.PanelState.EXPANDED -> {
                        playerPanelFragment.updatePanelBasedOnState(Enums.PanelState.EXPANDED)

                    }
                    SlidingUpPanelLayout.PanelState.COLLAPSED -> {
                        playerPanelFragment.updatePanelBasedOnState(Enums.PanelState.COLLAPSED)

                    }
                }
            }
        })


//        binding.includeToolbar.sortIv.setOnClickListener {
//
//            val bottomSheetDialog = Custom_BottomSheetDialogFragment.newInstance()
//            bottomSheetDialog?.setStyle(
//                R.style.AppBottomSheetDialogTheme,
//                R.style.AppBottomSheetDialogTheme
//            )
//            bottomSheetDialog?.show(supportFragmentManager, "btmsheet")
//        }
//

    }

    fun checkForPermissions() {
        val permissionProvider = PermissionProvider()
        permissionProvider.askForPermission(this, permissions)

        if (permissionProvider.permissionIsGranted) {
//            initTabs()
            initBottomSheet()

        }

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

    private fun initMainFragment() {
        val mainFragment = MainFragment()
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.add(
            binding.fragmentBaseContainer.id,
            mainFragment,
            "bottom sheet container"
        )
            .commit()
    }

}
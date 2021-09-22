package com.example.kookplayer.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.example.kookplayer.databinding.ActivityMainBinding
import com.example.kookplayer.manager.Coordinator
import com.example.kookplayer.player.Enums
import com.example.kookplayer.providers.PermissionProvider
import com.example.kookplayer.repositories.appdatabase.roomdb.MyDatabaseUtils
import com.example.kookplayer.services.NotificationPlayerService
import com.example.kookplayer.utlis.ScreenSizeUtils.getScreenHeight
import com.example.kookplayer.views.Fragments.MainFragment
import com.example.kookplayer.views.Fragments.PlayerPanelFragment
import com.sothree.slidinguppanel.SlidingUpPanelLayout


class MainActivity : AppCompatActivity(), LifecycleOwner {

    companion object {
        var permissionsGranted: Boolean = false
        lateinit var playerPanelFragment: PlayerPanelFragment
        lateinit var activity: MainActivity

        //        lateinit var sharedPreferences: SharedPreferences

    }



    var prefs: SharedPreferences? = null


    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )


    lateinit var binding: ActivityMainBinding
    lateinit var phoneStateListener: PhoneStateListener


    override fun onStop() {
        super.onStop()
//        saveSettings()

        val mgr = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        mgr?.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)

    }


    override fun onDestroy() {
        super.onDestroy()

        NotificationPlayerService.stopNotification(baseContext)

        Coordinator.mediaPlayerAgent.stop()
    }

//    fun saveSettings() {
//        SharedPrefUtils.saveState()
//    }

//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(true)
//        }
//        return super.onKeyDown(keyCode, event)
//    }

    override fun onBackPressed() {
        onStop()
        moveTaskToBack(true)
    }


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        activity = this

        MyDatabaseUtils.createDatabse()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("AppName", MODE_PRIVATE)

        checkForPermissions()

        initMainFragment()

        initBottomSheet()

        binding.slidingLayout.panelHeight = 0

        binding.slidingLayout.requestLayout()


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

        phoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, incomingNumber: String) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    //Incoming call: Pause music
                    if (Coordinator.isPlaying())
                        Coordinator.pause()
                } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                    //Not in call: Play music
                    if (Coordinator.currentPlayingSong != null) {
//                        Coordinator.currentPlayingSong!!.data?.let { Coordinator.play(it) }
                    }
                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    //A call is dialing, active or on hold
                    if (Coordinator.isPlaying())
                        Coordinator.pause()
                }
                super.onCallStateChanged(state, incomingNumber)
            }
        }
        val mgr = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        mgr?.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)


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

//    override fun onResume() {
//        super.onResume()
//
//        if (prefs?.getBoolean("firstrun", true)!!) {
//
//
//            binding.slidingLayout.panelHeight = 0
//
//
//            prefs!!.edit().putBoolean("firstrun", false).commit();
//        }
//    }

    fun updateVisibility() {
        binding.slidingLayout.panelHeight = getScreenHeight() * 1 / 10
    }


    fun checkForPermissions() {
        val permissionProvider = PermissionProvider()
        permissionProvider.askForPermission(this, permissions)

        if (permissionProvider.permissionIsGranted) {
//            initBottomSheet()
        }

    }


    fun replace() {
        playerPanelFragment = PlayerPanelFragment()
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
//        transaction.addToBackStack("playerPanel")
        transaction.replace(
            binding.bottomSheetContainer.id,
            playerPanelFragment,
            "bottom sheet container"
        )
            .commit()


        val mainFragment = MainFragment()
//        transaction.addToBackStack(null)
        transaction.replace(
            binding.fragmentBaseContainer.id,
            mainFragment,
            "bottom sheet container"
        )
            .commit()
    }


    fun initBottomSheet() {

        playerPanelFragment = PlayerPanelFragment()
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
//        transaction.addToBackStack("playerPanel")
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
//        transaction.addToBackStack(null)
        transaction.add(
            binding.fragmentBaseContainer.id,
            mainFragment,
            "bottom sheet container"
        )
            .commit()
    }

}
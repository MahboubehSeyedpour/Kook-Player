package com.example.cassette.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.view.KeyEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.example.cassette.databinding.ActivityMainBinding
import com.example.cassette.manager.Coordinator
import com.example.cassette.player.Enums
import com.example.cassette.providers.PermissionProvider
import com.example.cassette.services.NotificationPlayerService
import com.example.cassette.utlis.SharedPrefUtils
import com.example.cassette.views.Fragments.LibraryFragment
import com.example.cassette.views.Fragments.MainFragment
import com.example.cassette.views.Fragments.PlayerPanelFragment
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import java.util.*
import kotlin.concurrent.schedule
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity(), LifecycleOwner {

    companion object {
        var permissionsGranted: Boolean = false
        lateinit var playerPanelFragment: PlayerPanelFragment
        lateinit var activity: MainActivity
//        lateinit var sharedPreferences: SharedPreferences
    }

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

    fun saveSettings() {
        SharedPrefUtils.saveState()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {


            /*
            * finish() kills all processes related to an activity such as foreground services and etc.
            * moveTaskToBack(true) minimizes the activity not kill
            * */
            moveTaskToBack(true)

        }
        return super.onKeyDown(keyCode, event)
    }


    override fun onResume() {
        super.onResume()


    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor", "WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

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
                val display = windowManager.defaultDisplay
                val size = Point()
                display.getSize(size)
                val width: Int = size.x
                val height: Int = size.y

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
                        Coordinator.currentPlayingSong!!.data?.let { Coordinator.play(it) }
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


    fun checkForPermissions() {
        val permissionProvider = PermissionProvider()
        permissionProvider.askForPermission(this, permissions)

        if (permissionProvider.permissionIsGranted) {
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
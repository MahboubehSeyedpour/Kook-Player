package com.example.cassette.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior

class PlayerPanel_bottomSheet {

    lateinit var bottomSheetBehaviour: BottomSheetBehavior<View>
    lateinit var baseActivity: MainActivity
    var statusBar_color: Int = Color.BLUE

    //    lateinit var bottomSheetnew: View
    lateinit var context: Context

    fun setup(activity: MainActivity, bottomSheet: View, context: Context) {
        bottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehaviour.setPeekHeight(140)
        bottomSheetBehaviour.isHideable = false

        bottomSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehaviour.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED


        bottomSheetBehaviour.addBottomSheetCallback(bottomsheetCallback)

        this.context = context
        this.baseActivity = activity

    }

    val bottomsheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
        }

        @SuppressLint("ResourceAsColor")
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                BottomSheetBehavior.STATE_EXPANDED -> {
                    onPanelExpanded()
                }

                BottomSheetBehavior.STATE_COLLAPSED -> {
                    onPanelCollapsed()
                }
                BottomSheetBehavior.STATE_SETTLING -> {

                }
            }
        }

        fun onPanelExpanded() {
            Toast.makeText(
                context,
                "state is EXPANDED",
                Toast.LENGTH_SHORT
            ).show()
//            bottomSheet.visibility = View.GONE
//            bottomSheetnew.visibility = View.VISIBLE
            bottomSheetBehaviour.setPeekHeight(Int.MAX_VALUE)
//            bottomSheetBehaviour.isHideable = true
//            bottomSheetBehaviour = BottomSheetBehavior.from(bottomSheetnew)

            if(Build.VERSION.SDK_INT>=21){
                baseActivity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                val window : Window = baseActivity.getWindow()
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                statusBar_color = window.statusBarColor
                window.statusBarColor = Color.TRANSPARENT
                baseActivity.supportActionBar?.hide()
            }
        }

        fun onPanelCollapsed() {
            Toast.makeText(
                context,
                "state is Collapsed",
                Toast.LENGTH_SHORT
            ).show()
            bottomSheetBehaviour.setPeekHeight(140)

            if(Build.VERSION.SDK_INT>=21){
                baseActivity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                val window : Window = baseActivity.getWindow()
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = statusBar_color
                baseActivity.supportActionBar?.show()
            }
        }
    }
}
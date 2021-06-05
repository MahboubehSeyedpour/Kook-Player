package com.example.cassette.views

import android.R
import android.annotation.SuppressLint
import android.app.StatusBarManager
import android.content.Context
import android.media.MediaPlayer
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*

class PlayerPanel {

    lateinit var bottomSheetBehaviour: BottomSheetBehavior<View>
//    lateinit var bottomSheetnew: View
    lateinit var context: Context

    fun setup(bottomSheet: View, context: Context) {
        bottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehaviour.setPeekHeight(140)
        bottomSheetBehaviour.isHideable = false

//        bottomSheetnew.visibility = View.GONE

        bottomSheetBehaviour.addBottomSheetCallback(bottomsheetCallback)

        this.context = context
//        this.bottomSheetnew = bottomSheetnew

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
        }

        fun onPanelCollapsed() {
            Toast.makeText(
                context,
                "state is Collapsed",
                Toast.LENGTH_SHORT
            ).show()
            bottomSheetBehaviour.setPeekHeight(140)
        }
    }
}
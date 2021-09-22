package com.example.kookplayer.providers

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.kookplayer.views.MainActivity

class PermissionProvider: ActivityCompat.OnRequestPermissionsResultCallback {

    var permissionIsGranted : Boolean = false
    private val PERMISSIONS_REQUEST_CODE = 1
    lateinit var activity: Activity

    fun askForPermission(activity: Activity, permissions: Array<out String>)
    {
        this.activity = activity
        if (!hasPermissions(this.activity, *permissions)) {
            ActivityCompat.requestPermissions(activity, permissions ,PERMISSIONS_REQUEST_CODE)
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
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    MainActivity.permissionsGranted = true

                } else {
                    MainActivity.permissionsGranted = false
                    activity.finish()
                }
                permissionIsGranted = true
                return
            }
        }
    }
}
package com.example.cassette.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager




class NotificationBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        p0?.sendBroadcast(Intent("Songs")
            .putExtra("actionname", p1?.action))
    }
}
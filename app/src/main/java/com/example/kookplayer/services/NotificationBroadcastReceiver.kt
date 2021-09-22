package com.example.kookplayer.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class NotificationBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        p0?.sendBroadcast(Intent("Songs")
            .putExtra("actionname", p1?.action))
    }
}
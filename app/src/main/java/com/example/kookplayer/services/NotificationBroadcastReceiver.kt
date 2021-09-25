package com.example.kookplayer.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.kookplayer.R


class NotificationBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        p0?.sendBroadcast(Intent("Songs")
            .putExtra(p0.getString(R.string.extra_key), p1?.action))
    }
}
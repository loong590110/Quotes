package com.nineodes.quotes

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.IBinder

class QuotesService : Service() {
    private val receiver = ScreenOffBroadcastReceiver()

    override fun onCreate() {
        IntentFilter(Intent.ACTION_SCREEN_OFF).apply {
            addAction("expand")
            registerReceiver(
                receiver, this
            )
        }
        startForeground(1, receiver.notifyDataChanged(this, null))
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.apply {
            val quotes = getStringExtra("quotes")
            val quotesCn = getStringExtra("quotes_cn")
            receiver.notifyDataChanged(
                this@QuotesService,
                Bundle().apply {
                    putString("quotes", quotes)
                    putString("quotes_cn", quotesCn)
                })
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
    }
}
package com.nineodes.quotes

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.text.TextUtils
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ScreenOffBroadcastReceiver : BroadcastReceiver() {

    private var mediaSessionCompat: MediaSessionCompat? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.apply {
            if (Intent.ACTION_SCREEN_OFF == action) {
                //notifyDataChanged(context, null)
            }
        }
    }

    fun notifyDataChanged(context: Context?, bundle: Bundle?): Notification? {
        context?.apply {
            val quotes = bundle?.getString("quotes")
            val quotesCN = bundle?.getString("quotes_cn")
            val notificationId = 1
            val notification = NotificationCompat.Builder(this, packageName)
                .setChannelId(packageName)
                .setSmallIcon(R.drawable.ic_stat_warning)
                .setSubText(getText(R.string.default_quotes_cn))
                .apply {
                    fun RemoteViews.fill(): RemoteViews {
                        if (!TextUtils.isEmpty(quotes)) {
                            setTextViewText(R.id.txt_title, quotes)
                            if (!TextUtils.isEmpty(quotesCN)) {
                                setTextViewText(R.id.txt_subtitle, quotesCN)
                                setViewVisibility(R.id.txt_subtitle, View.VISIBLE)
                            } else {
                                setViewVisibility(R.id.txt_subtitle, View.GONE)
                            }
                        }
                        return this
                    }
                    setCustomContentView(
                        RemoteViews(
                            packageName,
                            R.layout.quotes_notification
                        ).fill()
                    )
                    setCustomBigContentView(
                        RemoteViews(
                            packageName,
                            R.layout.quotes_notification_large
                        ).fill()
                    )
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.images
                    ).let {
                        setLargeIcon(it)
                    }
                    mediaSessionCompat =
                        mediaSessionCompat ?: MediaSessionCompat(context, packageName)
                    val style =
                        androidx.media.app.NotificationCompat.DecoratedMediaCustomViewStyle()
                    style.setMediaSession(mediaSessionCompat?.sessionToken)
                    setStyle(style)
                }
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setNotificationSilent()
                .setOnlyAlertOnce(true)
                .setOngoing(false)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setShowWhen(false)
                .build()
            NotificationManagerCompat.from(this).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel(
                        NotificationChannel(
                            packageName,
                            "Quotes",
                            NotificationManager.IMPORTANCE_DEFAULT
                        ).apply {
                            lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
                        }
                    )
                }
            }.notify(notificationId, notification)
            return notification
        }
        return null
    }
}
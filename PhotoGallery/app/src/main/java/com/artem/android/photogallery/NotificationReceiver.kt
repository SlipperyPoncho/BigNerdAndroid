package com.artem.android.photogallery

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat

private const val TAG = "NotificationReceiver"

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            Log.i(TAG, "received result: $resultCode")
            if (resultCode != Activity.RESULT_OK) {
            // Активити переднего плана отменила возврат трансляции.
            }
            val requestCode = intent.getIntExtra(PollWorker.REQUEST_CODE, 0)
            val notification: Notification? = intent.getParcelableExtra(PollWorker.NOTIFICATION)
            if (context?.let {
                    ActivityCompat.checkSelfPermission(it, Manifest.permission.POST_NOTIFICATIONS)
                } != PackageManager.PERMISSION_GRANTED
            ) { return }
            if (notification != null) {
                context.let { NotificationManagerCompat.from(it) }
                    .notify(requestCode, notification)
            }
        }
    }
}
package com.example.mainverte.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mainverte.R

object WorkerUtils
{
    private const val TAG = "WorkerUtils"
    fun makeStatusNotification(idChannel : String,message: String, description: String, context: Context) {

        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val name = "Notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(idChannel, name, importance)
            channel.description = description

            // Add the channel
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)
        }

        // Create the notification
        val builder = NotificationCompat.Builder(context, idChannel)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("MainVerte")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))

        // Show the notification
        NotificationManagerCompat.from(context).notify(idChannel.toInt(), builder.build())
    }


    /**
     * Method for sleeping for a fixed amount of time to emulate slower work
     */
    fun sleep() {
        try {
            Thread.sleep(3000, 0)
        } catch (e: InterruptedException) {
            Log.e(TAG, e.message.toString())
        }

    }
}
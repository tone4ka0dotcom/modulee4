package ru.samsung.itacademy.mdev.getusdrate

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigDecimal


class RateCheckService : Service() {
    val handler = Handler(Looper.getMainLooper())
    var rateCheckAttempt = 0
    lateinit var startRate: BigDecimal
    lateinit var targetRate: BigDecimal
    val rateCheckInteractor = RateCheckInteractor()

    val rateCheckRunnable: Runnable = Runnable {
        rateCheckAttempt++
        Log.d(TAG, "rateCheckAttempt = $rateCheckAttempt")
        if (rateCheckAttempt > RATE_CHECK_ATTEMPTS_MAX) {
            Log.v(TAG, "чек")
            sendNotification("Max attempts count reached, stopping service")
            stopSelf()
            Log.d(TAG, "Max attempts count reached, stopping service")
            return@Runnable
        }
        requestAndCheckRate()
    }

    private fun requestAndCheckRate() {

        GlobalScope.launch(Dispatchers.Main) {
            val rate = rateCheckInteractor.requestRate()
            Log.d(TAG, "new rate = $rate")
            val rateBigDecimal = BigDecimal(rate)
            if ((startRate >= targetRate && rateBigDecimal <= targetRate) ||
                (startRate < targetRate && rateBigDecimal >= targetRate)
            ) {
                sendNotification("Current USD rate is $rate")
                stopSelf()
            } else {
                handler.postDelayed(rateCheckRunnable, RATE_CHECK_INTERVAL)
            }
        }
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startRate = BigDecimal(intent?.getStringExtra(ARG_START_RATE))
        targetRate = BigDecimal(intent?.getStringExtra(ARG_TARGET_RATE))
        Log.d(TAG, "onStartCommand startRate = $startRate targetRate = $targetRate")
        handler.post(rateCheckRunnable)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(rateCheckRunnable)
    }


    fun sendNotification(rate: String) {
        createNotificationChannel()
        val notificationBuilder = NotificationCompat.Builder(this,"My_Channel_ID")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(rate)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)){
            notify(1, notificationBuilder.build())
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "My Channel"
            val channelDescription = "Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("My_Channel_ID",name,importance)
            channel.apply {
                description = channelDescription
            }
            // Регистрируем канал в системе
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }






    companion object {
        const val TAG = "RateCheckService"
        const val NOTIFICATION_CHANNEL_ID = "usd_rate"
        const val RATE_CHECK_INTERVAL = 500L
        const val RATE_CHECK_ATTEMPTS_MAX = 2

        const val ARG_START_RATE = "ARG_START_RATE"
        const val ARG_TARGET_RATE = "ARG_TARGET_RATE"

        fun startService(context: Context, startRate: String, targetRate: String) {
            context.startService(Intent(context, RateCheckService::class.java).apply {
                putExtra(ARG_START_RATE, startRate)
                putExtra(ARG_TARGET_RATE, targetRate)
            })
        }

        fun stopService(context: Context) {
            context.stopService(Intent(context, RateCheckService::class.java))
        }
    }



}
package ru.samsung.itacademy.mdev.getusdrate

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.math.BigDecimal


class RateCheckService : Service() {
    val handler = Handler(Looper.getMainLooper())
    var rateCheckAttempt = 0
    lateinit var startRate: BigDecimal
    lateinit var targetRate: BigDecimal
    val rateCheckInteractor = RateCheckInteractor()
    lateinit var currentRate: String
    val rateCheckRunnable: Runnable = Runnable {
        // Write your code here. Check number of attempts and stop service if needed
        if(rateCheckAttempt < RATE_CHECK_ATTEMPTS_MAX){
            rateCheckAttempt++
            requestAndCheckRate()
        }
        else {
            Toast.makeText(this, "Max attempts count reached, stopping service", Toast.LENGTH_SHORT).show()
            return@Runnable
        }
    }

    private fun requestAndCheckRate(){
        // Write your code here
        GlobalScope.launch(Dispatchers.Main) {
            currentRate = rateCheckInteractor.requestRate()
            if(startRate < targetRate && targetRate.toString() <= currentRate){
                Toast.makeText(this@RateCheckService, "Rate = $currentRate", Toast.LENGTH_SHORT).show()
                return@launch
            }
            else if(targetRate.toString() in currentRate..startRate.toString()){
                Toast.makeText(this@RateCheckService, "Rate = $currentRate", Toast.LENGTH_SHORT).show()
                return@launch
            }
            Log.v(TAG, "Запрос")
            handler.postDelayed(rateCheckRunnable, RATE_CHECK_INTERVAL)
        }
    }
    inner class IncomingHandler : Handler() {
        override fun handleMessage(msg: Message) {
            val data = msg.data
            startRate = BigDecimal(data.getString("startRate"))
            targetRate = BigDecimal(data.getString("targetRate"))
            handler.post(rateCheckRunnable)
        }
    }
    private val myMessenger = Messenger(IncomingHandler())

    override fun onBind(intent: Intent): IBinder? {
        Log.d(TAG, "MyService onBind")
        return myMessenger.binder
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(rateCheckRunnable)
    }

    companion object {
        const val TAG = "RateCheckService"
        const val NOTIFICATION_CHANNEL_ID = "usd_rate"
        const val RATE_CHECK_INTERVAL = 5000L
        const val RATE_CHECK_ATTEMPTS_MAX = 2

        const val ARG_START_RATE = "ARG_START_RATE"
        const val ARG_TARGET_RATE = "ARG_TARGET_RATE"


    }



}
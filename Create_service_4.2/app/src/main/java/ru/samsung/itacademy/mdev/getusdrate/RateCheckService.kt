package ru.samsung.itacademy.mdev.getusdrate

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import okhttp3.internal.wait
import java.lang.Runnable
import java.math.BigDecimal


class RateCheckService : Service() {
    val handler = Handler(Looper.getMainLooper())
    var rateCheckAttempt = 0
    lateinit var currentRate: String
    lateinit var startRate: BigDecimal
    lateinit var targetRate: BigDecimal
    val rateCheckInteractor = RateCheckInteractor()
    val rateCheckRunnable: Runnable = Runnable {
        // Write your code here. Check number of attempts and stop service if needed
        if(rateCheckAttempt < RATE_CHECK_ATTEMPTS_MAX){
            rateCheckAttempt++
            requestAndCheckRate()
        }
        else {
            Toast.makeText(this, "Max attempts count reached, stopping service", Toast.LENGTH_SHORT).show()
            Companion.stopService(this)
        }
    }

    private fun requestAndCheckRate(){
        // Write your code here
        GlobalScope.launch(Dispatchers.Main) {
            currentRate = rateCheckInteractor.requestRate()
            if(startRate < targetRate && targetRate.toString() <= currentRate){
                Toast.makeText(this@RateCheckService, "Rate = $currentRate", Toast.LENGTH_SHORT).show()
                Companion.stopService(this@RateCheckService)
            }
            else if(targetRate.toString() in currentRate..startRate.toString()){
                Toast.makeText(this@RateCheckService, "Rate = $currentRate", Toast.LENGTH_SHORT).show()
                Companion.stopService(this@RateCheckService)
            }
            Log.v(TAG, "request")
            handler.postDelayed(rateCheckRunnable, RATE_CHECK_INTERVAL)
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


    companion object {
        const val TAG = "RateCheckService"
        const val RATE_CHECK_INTERVAL = 5000L
        const val RATE_CHECK_ATTEMPTS_MAX = 100

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
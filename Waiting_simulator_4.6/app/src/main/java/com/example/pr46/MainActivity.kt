package com.example.pr46

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

var counter = 0
lateinit var textPlace: TextView

class TimeTick: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        counter++
        textPlace.text = "время созерцания: $counter мин."
    }
}

class BatteryLow: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        textPlace.text = "накормите Ждуна, силы на исходе!"
    }
}

class MainActivity : AppCompatActivity() {
    private val receiverTimeTick = TimeTick()
    private val receiverBatteryLow = BatteryLow()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textPlace = findViewById(R.id.print)
        registerReceiver(receiverTimeTick, IntentFilter(Intent.ACTION_TIME_TICK))
        registerReceiver(receiverBatteryLow, IntentFilter(Intent.ACTION_BATTERY_LOW))
    }
    override fun onDestroy() {
        unregisterReceiver(receiverTimeTick)
        unregisterReceiver(receiverBatteryLow)
        super.onDestroy()
    }
    fun cancel_wait(view: View){
        unregisterReceiver(receiverTimeTick)
        unregisterReceiver(receiverBatteryLow)
        Toast.makeText(this, R.string.toast_text, Toast.LENGTH_SHORT).show()
    }
}
package com.example.pr47students

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Student1: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var resCode = resultCode
        val res = getResultExtras(true)
        var str = res.getString("text")
        str += "\n Student1"
        res.putString("text",str)
        setResult(++resCode, resultData, res)
    }
}
class Student2: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var resCode = resultCode
        val res = getResultExtras(true)
        var str = res.getString("text")
        str += "\n Student2"
        res.putString("text",str)
        setResult(++resCode, resultData, res)
    }
}
class Student3: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var resCode = resultCode
        val res = getResultExtras(true)
        var str = res.getString("text")
        str += "\n Student3"
        res.putString("text",str)
        setResult(++resCode, resultData, res)
    }
}
class Student4: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var resCode = resultCode
        val res = getResultExtras(true)
        var str = res.getString("text")
        str += "\n Student4"
        res.putString("text",str)
        setResult(++resCode, resultData, res)
    }
}
class Student5: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var resCode = resultCode
        val res = getResultExtras(true)
        var str = res.getString("text")
        str += "\n Student5"
        res.putString("text",str)
        setResult(++resCode, resultData, res)
    }
}
class Student6: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var resCode = resultCode
        val res = getResultExtras(true)
        var str = res.getString("text")
        str += "\n Student6"
        res.putString("text",str)
        setResult(++resCode, resultData, res)
        abortBroadcast()
    }
}
class Student7: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var resCode = resultCode
        val res = getResultExtras(true)
        var str = res.getString("text")
        str += "\n Student7"
        res.putString("text",str)
        setResult(++resCode, resultData, res)
    }
}
class Student8: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var resCode = resultCode
        val res = getResultExtras(true)
        var str = res.getString("text")
        str += "\n Student8"
        res.putString("text",str)
        setResult(++resCode, resultData, res)
    }
}
class Student9: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var resCode = resultCode
        val res = getResultExtras(true)
        var str = res.getString("text")
        str += "\n Student9"
        res.putString("text",str)
        setResult(++resCode, resultData, res)
    }
}
class Student10: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var resCode = resultCode
        val res = getResultExtras(true)
        var str = res.getString("text")
        str += "\n Student10"
        res.putString("text",str)
        setResult(++resCode, resultData, res)
    }
}

class MainActivity : AppCompatActivity() {
    val s1 = Student1()
    val s2 = Student2()
    val s3 = Student3()
    val s4 = Student4()
    val s5 = Student5()
    val s6 = Student6()
    val s7 = Student7()
    val s8 = Student8()
    val s9 = Student9()
    val s10 = Student10()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filter = IntentFilter("com.example.pr47lector.Letter")
        registerReceiver(s1, filter.apply { priority = 1000 })
        registerReceiver(s4, filter.apply { priority = 700 })
        registerReceiver(s8, filter.apply { priority = 300 })
        registerReceiver(s10, filter.apply { priority = 100 })
    }
    override fun onDestroy(){
        unregisterReceiver(s1)
        unregisterReceiver(s3)
        unregisterReceiver(s4)
        unregisterReceiver(s6)
        unregisterReceiver(s8)
        unregisterReceiver(s10)
        super.onDestroy()
    }
}
package com.example.pr47lector

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class Receiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var res = getResultExtras(true)
        var str = res.getString("text")
        Toast.makeText(context, str, Toast.LENGTH_LONG).show()
    }
}


class MainActivity : AppCompatActivity() {
    private lateinit var originalLetter: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        originalLetter = getString(R.string.broadcast)
    }
    fun checkStudents(view: View){
        val intent = Intent("com.example.pr47lector.Letter")
        intent.putExtra("text",originalLetter)
        sendOrderedBroadcast(intent,null, Receiver(), null, 0, "Lecture",
            Bundle().apply { putString("text", originalLetter) })
    }
}
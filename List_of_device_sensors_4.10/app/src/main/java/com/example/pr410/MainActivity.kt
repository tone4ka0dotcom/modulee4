package com.example.pr410

import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.size
import android.widget.AdapterView

import android.widget.Toast




class MainActivity : AppCompatActivity() {
    lateinit var sm: SensorManager
    lateinit var list: TextView
    lateinit var spinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sm = getSystemService(SENSOR_SERVICE) as SensorManager
        list = findViewById(R.id.list_sensor)
        spinner = findViewById(R.id.spinner)
        val sensorList = sm.getSensorList(Sensor.TYPE_ALL)
        val location = arrayOf(1, 4, 8, 9, 10, 11, 15, 16, 17, 18, 19, 20, 29, 30, 35, 36)
        val environment = arrayOf(2, 5, 6, 12, 13, 14)
        val human = arrayOf(21,31,34)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View?, selectedItemPosition: Int, selectedId: Long
            ) {
                var text = ""
                when(selectedItemPosition){
                    0 -> for (i in sensorList.filter { it.type in environment }.map { it.name }){text += "$i\n"}
                    1 -> for (i in sensorList.filter { it.type in location }.map { it.name }){text += "$i\n"}
                    2 -> for (i in sensorList.filter { it.type in human }.map { it.name }){text += "$i\n"}
                }
                list.text = text
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}
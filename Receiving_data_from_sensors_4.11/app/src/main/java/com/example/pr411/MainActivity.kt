package com.example.pr411

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.pr411.databinding.ActivityMainBinding
import android.hardware.SensorEventListener
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup


class MainActivity : AppCompatActivity() {
    lateinit var sm: SensorManager
    var lSensor: Sensor? = null
    var rSensor:Sensor? = null
    var aSensor:Sensor? = null
    var vSensor:Sensor? = null
    private lateinit var dataSen:ActivityMainBinding
    lateinit var radioGroup: RadioGroup
    lateinit var dataSensor: String

    private val sensorEventListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            var x = 0f
            var y = 0f
            var z = 0f
            var s = 0f
            when (event.sensor.type) {
                lSensor!!.type -> {
                    x = event.values[0]
                    dataSensor = "Освещенность: $x"
                    dataSen.sensText = dataSensor
                }
                rSensor!!.type -> {
                    x = event.values[0]
                    y = event.values[1]
                    z = event.values[2]
                    s = event.values[3]
                    dataSensor = "Проекция вектора по осям:" +
                            "\n OX: $x" +
                            "\n OY: $y" +
                            "\n OZ: $z" +
                            "\n скалярная мера угла поворота: $s"
                    dataSen.sensText = dataSensor
                }
                aSensor!!.type -> {
                    x = event.values[0]
                    y = event.values[1]
                    z = event.values[2]
                    dataSensor = "Динамическое ускоение по осям:" +
                            "\n OX: $x" +
                            "\n OY: $y" +
                            "\n OZ: $z"
                    dataSen.sensText = dataSensor
                }
            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataSen = DataBindingUtil.setContentView(this, R.layout.activity_main)
        sm = getSystemService(SENSOR_SERVICE) as SensorManager
        radioGroup = findViewById(R.id.radioGroup)
        lSensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT)
        rSensor = sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if(lSensor != null){
            sm.registerListener(sensorEventListener,lSensor,0)
            vSensor = lSensor
        }
        else{
            Toast.makeText(this, R.string.sensorAbsentL, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onResume() {
        super.onResume()
        if(vSensor!=null) sm.registerListener(sensorEventListener,vSensor,0)
        radioGroup.setOnCheckedChangeListener { _, i ->
            when(i){
                R.id.l -> {
                    sm.unregisterListener(sensorEventListener)
                    if(lSensor != null){
                        sm.registerListener(sensorEventListener,lSensor,0)
                    }
                    else{
                        Toast.makeText(this, R.string.sensorAbsentL, Toast.LENGTH_SHORT).show()
                    }
                    vSensor = lSensor
                }
                R.id.r -> {
                    sm.unregisterListener(sensorEventListener)
                    if(rSensor != null){
                        sm.registerListener(sensorEventListener,rSensor,0)
                    }
                    else{
                        Toast.makeText(this, R.string.sensorAbsentR, Toast.LENGTH_SHORT).show()
                    }
                    vSensor = rSensor
                }
                R.id.a -> {
                    sm.unregisterListener(sensorEventListener)
                    if(aSensor != null){
                        sm.registerListener(sensorEventListener,aSensor,0)
                    }
                    else{
                        Toast.makeText(this, R.string.sensorAbsentA, Toast.LENGTH_SHORT).show()
                    }
                    vSensor = aSensor
                }
            }
        }
    }


    override fun onPause() {
        super.onPause()
        sm.unregisterListener(sensorEventListener)
    }
}
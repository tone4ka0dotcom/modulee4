package com.example.pr44

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    lateinit var textLocation: TextView
    lateinit var textAdress: TextView
    lateinit var getLocation: Button
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        textLocation = findViewById(R.id.textLocation)
        textAdress = findViewById(R.id.textAdress)
        getLocation = findViewById(R.id.getLocation)
        getLocation.setOnClickListener {
            getLocation()
        }
    }

    private fun getLocation(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 1)
        }
        val lastLocation = fusedLocationProviderClient.lastLocation
        lastLocation.addOnSuccessListener {
            if(it != null){
                textLocation.text = "Latitude: ${it.latitude}\nLongitude: ${it.longitude}"
                textAdress.text = getAddress(it.latitude, it.longitude)
            }
            else{
                Toast.makeText(this, "Sorry Can't Get Location", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getAddress(lat: Double, lng: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val list = geocoder.getFromLocation(lat, lng, 1)
        return list[0].getAddressLine(0)
    }
}
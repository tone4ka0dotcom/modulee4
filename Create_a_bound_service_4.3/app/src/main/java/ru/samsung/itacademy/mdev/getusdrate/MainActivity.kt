package ru.samsung.itacademy.mdev.getusdrate

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import android.R.attr.name




class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    lateinit var textRate: TextView
    lateinit var textTargetRate: EditText
    lateinit var rootView: View
    lateinit var rateCheckService: RateCheckService
    var myService: Messenger? = null
    var isBound: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewModel()
        initView()
        val intent = Intent(applicationContext, RateCheckService::class.java)
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE)
    }

    private val myConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {
            Log.v("RateCheckService", "onServiceConnected")
            myService = Messenger(service)
            isBound = true
        }

        override fun onServiceDisconnected(
            className: ComponentName) {
            Log.v("RateCheckService", "onServiceDisconnected")
            myService = null
            isBound = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.usdRate.observe(this, {
            textRate.text = "$it RUB"
        })
        viewModel.onCreate()
    }
    private fun sendMessage(startRate: String, targetRate: String){
        if (!isBound) return
        val msg = Message.obtain()
        val bundle = Bundle()
        bundle.putString("startRate", startRate)
        bundle.putString("targetRate", targetRate)
        msg.data = bundle
        try {
            myService?.send(msg)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }
    fun initView() {
        textRate = findViewById(R.id.textUsdRubRate)
        textTargetRate = findViewById(R.id.textTargetRate)
        rootView = findViewById(R.id.rootView)

        findViewById<Button>(R.id.btnRefresh).setOnClickListener {
            viewModel.onRefreshClicked()
        }
        findViewById<Button>(R.id.btnSubscribeToRate).setOnClickListener {
            val targetRate = textTargetRate.text.toString()
            val startRate = viewModel.usdRate.value
            if (targetRate.isNotEmpty() && startRate?.isNotEmpty() == true) {
                sendMessage(startRate, targetRate)
            } else if (targetRate.isEmpty()) {
                Snackbar.make(rootView, R.string.target_rate_empty, Snackbar.LENGTH_SHORT).show()
            } else if (startRate.isNullOrEmpty()) {
                Snackbar.make(rootView, R.string.current_rate_empty, Snackbar.LENGTH_SHORT).show()
            }
        }

    }
}
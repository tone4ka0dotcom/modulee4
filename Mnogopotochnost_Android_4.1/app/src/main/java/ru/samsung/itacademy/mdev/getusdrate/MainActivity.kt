package ru.samsung.itacademy.mdev.getusdrate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    lateinit var textRate: TextView
    lateinit var rootView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewModel()
        initView()
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

    fun initView() {
        textRate = findViewById(R.id.textUsdRubRate)
        rootView = findViewById(R.id.rootView)
        findViewById<Button>(R.id.btnRefresh).setOnClickListener {
            viewModel.onRefreshClicked()
        }
    }
}
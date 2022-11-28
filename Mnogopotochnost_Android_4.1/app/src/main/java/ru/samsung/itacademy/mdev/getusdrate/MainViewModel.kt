package ru.samsung.itacademy.mdev.getusdrate

import android.location.Location
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import java.lang.Runnable
import java.util.concurrent.Flow
import kotlin.concurrent.thread

class MainViewModel : ViewModel() {
    val usdRate = MutableLiveData<String>()
    val rateCheckInteractor = RateCheckInteractor()
    fun onCreate() {
        refreshRate()
    }

    fun onRefreshClicked() {
        refreshRate()
    }

    private fun refreshRate(){
        //Write your code here
        GlobalScope.launch(Dispatchers.Main) {
            usdRate.value = rateCheckInteractor.requestRate()
        }
    }

    companion object {
        const val TAG = "MainViewModel"
        const val USD_RATE_URL = "https://www.freeforexapi.com/api/live?pairs=USDRUB"
    }
}
package com.example.smartshop.ui.setting

import androidx.lifecycle.ViewModel
import androidx.work.*
import com.example.smartshop.constant.DATA_NAME
import com.example.smartshop.constant.ID
import com.example.smartshop.worker.SmartShopWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor() : ViewModel() {

    var time = "3"
    lateinit var workManager: WorkManager
    private lateinit var periodicWork: PeriodicWorkRequest
    val data = Data.Builder().putString(DATA_NAME, time)

    fun setTime() {
        periodicWork = PeriodicWorkRequestBuilder<SmartShopWorker>(time.toLong(), TimeUnit.HOURS)
            .setInputData(data.build())
            .build()

        setWorkManager()
    }

    fun start() {
        periodicWork = PeriodicWorkRequestBuilder<SmartShopWorker>(3, TimeUnit.HOURS)
            .setInputData(data.build())
            .build()

        setWorkManager()
    }

    fun stop() {
        workManager.cancelUniqueWork(ID)
    }

    private fun setWorkManager() {
        workManager.enqueueUniquePeriodicWork(
            ID,
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWork
        )
    }
}
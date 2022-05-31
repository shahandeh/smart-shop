package com.example.smartshop.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

object DataStoreObject {
    val Context.dataStore by preferencesDataStore(name = "Data Store")
}
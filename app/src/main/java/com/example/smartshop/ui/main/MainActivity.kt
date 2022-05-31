package com.example.smartshop.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.smartshop.R
import com.example.smartshop.constant.SET_THEME
import com.example.smartshop.constant.SET_USER_ID
import com.example.smartshop.data.CurrentUser.email
import com.example.smartshop.data.CurrentUser.first_name
import com.example.smartshop.data.CurrentUser.last_name
import com.example.smartshop.data.CurrentUser.user_id
import com.example.smartshop.data.CurrentUser.user_name
import com.example.smartshop.data.DataStoreObject.dataStore
import com.example.smartshop.safeapi.ResultWrapper
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var currentTheme = MODE_NIGHT_NO
    private val mainViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition {
            var condition = false
            lifecycleScope.launchWhenCreated {
                readUserFromDataStore()
                condition = retrieveUser()
                condition = setUser()
            }
            return@setKeepOnScreenCondition condition
        }
        super.onCreate(savedInstanceState)
        setTheme()
        setContentView(R.layout.activity_main)

        val bnv = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment

        val navController = navHostFragment.navController

        bnv.setupWithNavController(navController)

    }

    private fun setTheme() {
        lifecycleScope.launch {
            val temp = readTheme()
            if (temp != null) currentTheme = temp
            AppCompatDelegate.setDefaultNightMode(currentTheme)
        }
    }

    private suspend fun saveTheme() {
        val dataStoreKey = intPreferencesKey(SET_THEME)
        dataStore.edit { setting ->
            setting[dataStoreKey] = currentTheme
        }
    }

    private suspend fun readTheme(): Int? {
        val dataStoreKey = intPreferencesKey(SET_THEME)
        return dataStore.data.first()[dataStoreKey]
    }

    private suspend fun readUserFromDataStore() {
        val dataStoreKey = intPreferencesKey(SET_USER_ID)
        user_id = dataStore.data.first()[dataStoreKey] ?: 0
    }

    private suspend fun retrieveUser(): Boolean{
        return if (user_id != 0) {
            mainViewModel.retrieveUser(user_id.toString())
            false
        } else true
    }

    private suspend fun setUser(): Boolean{
        var returnResult = false
        lifecycleScope.launch {
            mainViewModel.user.collect{
                when(it){
                    is ResultWrapper.Failure -> {}
                    ResultWrapper.Loading -> {}
                    is ResultWrapper.Success -> {
                        first_name = it.value.first_name
                        last_name = it.value.last_name
                        user_name = it.value.username
                        email = it.value.email
                        returnResult = true
                    }
                }
            }
        }
        return returnResult
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.theme) {
            currentTheme = if (currentTheme == MODE_NIGHT_NO) MODE_NIGHT_YES
            else MODE_NIGHT_NO
            lifecycleScope.launch {
                saveTheme()
                setTheme()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
}
package com.example.smartshop.tracker

sealed class NetworkStatus {
    object Available: NetworkStatus()
    object Unavailable: NetworkStatus()
}

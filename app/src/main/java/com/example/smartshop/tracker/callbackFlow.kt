package com.example.smartshop.tracker

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

inline fun <Result> Flow<NetworkStatus>.map(
    crossinline onAvailable: suspend () -> Result,
    crossinline onUnavailable: suspend () -> Result,
): Flow<Result> = map { status ->
    when(status) {
        NetworkStatus.Available -> onAvailable()
        NetworkStatus.Unavailable -> onUnavailable()
    }
}
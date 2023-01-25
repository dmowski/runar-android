package com.tnco.runar.util

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkMonitor @Inject constructor(
    connectivityManager: ConnectivityManager,
    networkRequest: NetworkRequest
) {

    val isConnected: Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(false)
                super.onLost(network)
            }
        }

        trySend(isConnected(connectivityManager))
        connectivityManager.registerNetworkCallback(networkRequest, callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }

    private fun isConnected(connectivityManager: ConnectivityManager): Boolean {
        val activeNetwork = connectivityManager.activeNetwork
        return connectivityManager.getNetworkCapabilities(activeNetwork)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}

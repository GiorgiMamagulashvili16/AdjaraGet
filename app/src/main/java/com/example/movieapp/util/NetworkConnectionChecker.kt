package com.example.movieapp.util

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NetworkConnectionChecker(context: Context) : LiveData<Boolean>() {

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val connectivityManager =
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    private val validNetwork: MutableSet<Network> = mutableSetOf()

    override fun onActive() {
        super.onActive()
        networkCallback = networkCallBack()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }
    private fun checkValidNetworks(){
        postValue(validNetwork.size > 0)
    }
    override fun onInactive() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun networkCallBack() =
        object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                val hasInternetConnectivityManager =
                    networkCapabilities?.hasCapability(NET_CAPABILITY_INTERNET)
                if (hasInternetConnectivityManager == true) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val hasInternet = ConnectionChecker.hasInternet()
                        if (hasInternet) {
                            withContext(Dispatchers.Main) {
                                validNetwork.add(network)
                                checkValidNetworks()
                            }
                        }
                    }
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                postValue(false)
            }
        }


}
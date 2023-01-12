package com.example.mainverte.utils

import android.content.Context
import android.net.ConnectivityManager

object Network {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return connectivityManager.allNetworks.firstOrNull { network ->
            val networkInfo = connectivityManager.getNetworkInfo(network)
            networkInfo!!.isConnected
        } != null
    }
}
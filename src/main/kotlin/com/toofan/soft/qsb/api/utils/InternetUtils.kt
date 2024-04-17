package com.toofan.soft.qsb.api.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URL

internal object InternetUtils {
    internal suspend fun isInternetAvailable1(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val address = InetAddress.getByName("www.google.com")
                val reachable = address.isReachable(3000) // Timeout set to 3 seconds
                reachable
            } catch (e: IOException) {
                false
            }
        }
    }

    internal fun isInternetAvailable(): Boolean {
//        return withContext(Dispatchers.IO) {
            return try {
                val url = URL("http://www.google.com")
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 3000 // Timeout set to 3 seconds
                connection.readTimeout = 3000 // Timeout set to 3 seconds
                connection.connect()
                val responseCode = connection.responseCode
                responseCode == HttpURLConnection.HTTP_OK
            } catch (e: IOException) {
                false
            }
//        }
    }

}
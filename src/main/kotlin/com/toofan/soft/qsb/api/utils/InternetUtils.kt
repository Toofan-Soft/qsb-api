package com.toofan.soft.qsb.api.utils

import java.io.IOException
import java.net.InetAddress

internal object InternetUtils {
    internal suspend fun isInternetAvailable(): Boolean {
//        return withContext(Dispatchers.IO) {
           return try {
                val address = InetAddress.getByName("www.google.com")
                val reachable = address.isReachable(3000) // Timeout set to 3 seconds
                reachable
            } catch (e: IOException) {
                false
            }
//        }
    }
}
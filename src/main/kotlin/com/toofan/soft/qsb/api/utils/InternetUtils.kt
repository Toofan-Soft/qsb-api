package com.toofan.soft.qsb.api.utils

import java.io.IOException
import java.net.InetAddress


object InternetUtils {
    fun isInternetAvailable(): Boolean {
        return try {
            val address = InetAddress.getByName("www.google.com")
            val reachable = address.isReachable(3000) // Timeout set to 3 seconds
            reachable
        } catch (e: IOException) {
            false
        }
    }
}

fun main() {
    val isInternetAvailable = InternetUtils.isInternetAvailable()
    if (isInternetAvailable) {
        println("Internet is available")
    } else {
        println("Internet is not available")
    }
}

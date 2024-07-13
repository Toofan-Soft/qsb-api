package com.toofan.soft.qsb.api.utils

internal object InternetUtils {
    internal fun isInternetAvailable(): Boolean {
        return true
//        return try {
//            val url = URL("https://www.google.com")
//            val connection: URLConnection = url.openConnection()
//            connection.connect()
//            true
//        } catch (e: Exception) {
//            false
//        }
    }

}
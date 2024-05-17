package com.toofan.soft.qsb.api

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

object DateStateUpdater {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Sending request to update state...")
        
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("http://127.0.0.1:8000/api/state/update") // Replace with your Laravel app URL
            .build()

        try {
            val response: Response = client.newCall(request).execute()
            if (response.isSuccessful) {
                println("State updated successfully")
            } else {
                println("Failed to update state: ${response.code()}")
            }
        } catch (e: IOException) {
            println("Error occurred: ${e.message}")
        }
    }
}

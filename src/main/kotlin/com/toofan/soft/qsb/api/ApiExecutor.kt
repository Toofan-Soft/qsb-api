package com.toofan.soft.qsb.api

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.services.Logger
import com.toofan.soft.qsb.api.session.Memory
import com.toofan.soft.qsb.api.session.checkToken
import com.toofan.soft.qsb.api.utils.InternetUtils
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object ApiExecutor {
    suspend fun execute(
        route: Route,
        request: IRequest? = null,
        onResponse: (jsonObject: JsonObject) -> Unit = {}
    ) {
//        Coroutine.launch {
            if (InternetUtils.isInternetAvailable()) {
                try {
                    val url = if (route.method == Method.GET.value && request != null) {
                        Logger.log(route.url, "request-parameters: ${request.parameters}")
                        URL(route.url + "?" + request.parameters)
                    } else {
                        URL(route.url)
                    }

                    val connection = url.openConnection() as HttpURLConnection

                    println("route.method: " + route.method)
                    connection.requestMethod = route.method
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                    if (route.isAuthorized) {
                        connection.setRequestProperty("Authorization", "Bearer ${Memory.token}")
                    }

                    if (route.method != Method.GET.value) {
                        connection.doOutput = true

                        request?.let {
                            Logger.log(route.url, "request-parameters: ${request.parameters}")
                            connection.outputStream.use { os ->
                                val input =
                                    request.parameters.toByteArray(charset("utf-8"))
                                os.write(input, 0, input.size)
                            }
                        }
                    }

                    val responseCode = connection.responseCode
                    Logger.log(route.url, "response-code: $responseCode")

                    if (responseCode !in 200..299) {
                        throw RuntimeException("HttpResponseCode: $responseCode, ${connection.responseMessage}")
                    } else {
                        val informationString = StringBuilder()
                        BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
                            var line: String?
                            while (reader.readLine().also { line = it } != null) {
                                informationString.append(line)
                            }
                        }

                        Logger.log(route.url, "informationString: $informationString")

                        // Use Gson for parsing JSON
                        val gson = Gson()
                        val jsonObject = gson.fromJson(informationString.toString(), JsonObject::class.java)
                        jsonObject.addProperty("is_success", true)

                        jsonObject.checkToken()

                        onResponse(jsonObject)
                    }
                } catch (e: Exception) {
                    Logger.log(route.url, "exception: ${e.message}")

                    val jsonObject = JsonObject()
                    jsonObject.addProperty("is_success", false)
//                    jsonObject.addProperty("error_message", "Thank you for your interest! Our server is currently undergoing development to bring you an even better experience. Please check back later, and we'll have everything up and running for you soon. Your patience is greatly appreciated!")
                    jsonObject.addProperty("error_message", "There is an error! Please check back later :)")

                    onResponse(jsonObject)
                }
            } else {
                val jsonObject = JsonObject()
                jsonObject.addProperty("is_success", false)
                jsonObject.addProperty("error_message", "Internet is not available, check it then try again :)")

                onResponse(jsonObject)
            }
//        }
    }
}

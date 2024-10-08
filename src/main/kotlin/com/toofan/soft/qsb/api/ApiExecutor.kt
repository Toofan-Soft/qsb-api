package com.toofan.soft.qsb.api

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.exceptions.ApiException
import com.toofan.soft.qsb.api.services.Logger
import com.toofan.soft.qsb.api.session.Session
import com.toofan.soft.qsb.api.session.checkSession
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
        if (InternetUtils.isInternetAvailable()) {
            try {
                val url = if (route.method == Method.GET.value && request != null && request.parametersGet.isNotEmpty()) {
                    Logger.log(route.url, "request-parameters: ${request.parametersGet}")
                    URL(route.url + "?" + request.parametersGet)
                } else {
                    URL(route.url)
                }
                Logger.log("URL", url.toString())
                Logger.log("Method" , route.method)

                val connection = url.openConnection() as HttpURLConnection

                connection.requestMethod = route.method
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                if (route.isAuthorized) {
                    connection.setRequestProperty("Authorization", "Bearer ${Session.token}")
                }

                if (route.method != Method.GET.value) {
                    connection.doOutput = true

                    request?.let {
                        Logger.log(route.url, "request-parameters: ${request.parametersPost}")
                        connection.outputStream.use { os ->
                            val input =
                                request.parametersPost.toByteArray(charset("utf-8"))
                            os.write(input, 0, input.size)
                        }
                    }
                }

                val responseCode = connection.responseCode

                if (responseCode in 200..299) {
                    val informationString = StringBuilder()
                    BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
                        var line: String?
                        while (reader.readLine().also { line = it } != null) {
                            informationString.append(line)
                        }
                    }

                    Logger.log("Information", informationString)

                    val gson = Gson()
                    val jsonObject = gson.fromJson(informationString.toString(), JsonObject::class.java)
                    jsonObject.addProperty("is_success", true)

                    jsonObject.checkSession()

                    onResponse(jsonObject)
                } else {
                    Logger.log("Api-Exception", ApiException.of(responseCode).message)

                    val jsonObject = JsonObject()
                    jsonObject.addProperty("is_success", false)
                    jsonObject.addProperty("error_message", ApiException.of(responseCode).message)

                    onResponse(jsonObject)
                }
            } catch (e: Exception) {
                Logger.log(route.url, "exception:\n")
                Logger.log("Exception", e.message)

                val jsonObject = JsonObject()
                jsonObject.addProperty("is_success", false)
                jsonObject.addProperty("error_message", "There is an error! Please check back later :)")

                onResponse(jsonObject)

                e.printStackTrace()
            }
        } else {
            val jsonObject = JsonObject()
            jsonObject.addProperty("is_success", false)
            jsonObject.addProperty("error_message", "Internet is not available, check it then try again :)")

            onResponse(jsonObject)
        }
    }
}

package com.toofan.soft.qsb.api

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.services.Logger
import com.toofan.soft.qsb.api.session.Memory
import com.toofan.soft.qsb.api.session.checkToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object ApiExecutor {
    suspend fun execute(
        route: Route,
        request: IRequest? = null,
        onComplete: (jsonObject: JsonObject) -> Unit = { }
    ) {
        withContext(Dispatchers.IO) {
            try {
                val url = URL(route.url)
                val connection = url.openConnection() as HttpURLConnection

                connection.requestMethod = route.method
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                if (route.isAuthorized) {
                    connection.setRequestProperty("Authorization", "Bearer ${Memory.token}")
                }
                connection.doOutput = true

                Logger.log(route.url, "request-properties: ${connection.requestProperties}")

                // Set the request parameters
                request?.let {
                    Logger.log(route.url, "request-parameters: ${request.parameters}")

                    connection.outputStream.use { os ->
                        val input =
                            request.parameters.toByteArray(charset("utf-8"))
                        os.write(input, 0, input.size)
                    }
                }

                val jsonObject: JsonObject

                println("response_code: ${connection.responseCode}")
                when (val responseCode = connection.responseCode) {
                    200 -> {
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
                        jsonObject = gson.fromJson(informationString.toString(), JsonObject::class.java)
                        jsonObject.addProperty("is_success", true)
//                        jsonObject.addProperty("error_message", null as String?)
                        jsonObject.checkToken()
                    }
                    422 -> {
                        Logger.log(route.url, "response-code: $responseCode")
                        val errorResponse = StringBuilder()

                        val errorStream = connection.errorStream
                        if (errorStream != null) {
                            BufferedReader(InputStreamReader(errorStream)).use { reader ->
                                var line: String?
                                while (reader.readLine().also { line = it } != null) {
                                    errorResponse.append(line)
                                }
                                Logger.log(route.url, "Error response: $errorResponse")
                            }
                        }

                        // Use Gson for parsing JSON
                        val gson = Gson()
                        jsonObject = gson.fromJson(errorResponse.toString(), JsonObject::class.java)
                        jsonObject.addProperty("is_success", false)
//                    throw RuntimeException("HttpResponseCode: $responseCode")

                    }
                    else -> {
                        jsonObject = JsonObject()
                    }
                }

                onComplete(jsonObject)

            } catch (e: Exception) {
                Logger.log(route.url, "exception: ${e.message}")
                Logger.log(route.url, "e-message: ${e.message}")

                throw RuntimeException(e)
            }
        }
    }
}

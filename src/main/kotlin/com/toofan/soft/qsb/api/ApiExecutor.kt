package com.toofan.soft.qsb.api

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.services.Logger
import com.toofan.soft.qsb.api.session.Memory
import com.toofan.soft.qsb.api.session.checkToken
import com.toofan.soft.qsb.api.utils.InternetUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object ApiExecutor {
    suspend fun execute1(
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

                Logger.log(url.toString(), "request-properties: ${connection.requestProperties}")

                // Set the request parameters
                request?.let {
                    Logger.log(url.toString(), "request-parameters: ${request.parameters}")

                    connection.outputStream.use { os ->
                        val input =
                            request.parameters.toByteArray(charset("utf-8"))
                        os.write(input, 0, input.size)
                    }
                }

                val jsonObject: JsonObject

                println("response_code: ${connection.responseCode}, connection.responseMessage: ${connection.responseMessage}")
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

                throw RuntimeException(e)
            }
        }
    }

    suspend fun execute2(
        route: Route,
        request: IRequest? = null,
        onResponse: (jsonObject: JsonObject) -> Unit = {}
    ) {
        withContext(Dispatchers.IO) {
            try {
                val url = URL(route.url)
                val connection = url.openConnection() as HttpURLConnection

                println("route.method: " + route.method)
                connection.requestMethod = route.method
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                if (route.isAuthorized) {
                    connection.setRequestProperty("Authorization", "Bearer ${Memory.token}")
//                    connection.setRequestProperty("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiNDllMmU1MDJjM2Y2YTIxNTdiYWU5ZTk0MWU4OTFlYjEzNWQ4MmM0MGRkNDcxYTNjNDRmY2ViZGQzOGU2ZjFlNmEwMTgxZGY0ZjJjY2ZkNWEiLCJpYXQiOjE3MTA4ODk1NzguNzIzMzM1LCJuYmYiOjE3MTA4ODk1NzguNzIzMzM4LCJleHAiOjE3NDI0MjU1NzguNzE3NzAzLCJzdWIiOiJhNmJiZTU3OC1mZTE4LTRkNTItOWZhYy01YjllY2ZiYzRkMjEiLCJzY29wZXMiOltdfQ.hLl1-t1hSALaVzl1E1TopFbgyamKqoczy_jj__ZALxd_nsnMaUVsthlhDJrvMz3zWjfjJ9njYnqqXvoeAk8UC6hbEXbNwvAw4Gwrwm9fxKhsEqzq2-isIECW4ezf3kUjiK4NrpvOuQwVOZlQ5T3punNTwm1PRhnmopjP21Kw90adg6ZdsPFCr4d32bJj3rzME5GRcltwrUMN0QBXSQ3HkQ9FeMLvYDBHURuxhtcwJvWxFMnN-i5cqebXyGXD8Qf_QfyVdxyaWWXxuTt8I8tB63rgXYORHRf4L5v3IBy4hr6s6SH2jq2h6ktZTmEu_WfUDotsukfFIQphflxdBjpLyanrhz21IW6dSV-1LYGr2Upo8210fBst1wVzwNfY1OInxSgtqeFWRh6Nl0e3luApyp4YD1iV-YsD6bpYlIn4ed1P0kRfLDmHC2-3BugmFKSiYecbFuvPGrGpxK495KZ7C7xYJtjjwD2Pu-iwkv3Xr0Rs2HoAzBZtwEW-krgYJp9yqXSVppvIRDW2Zi0wdBfbqnqzWjUpFmzb92DmQnWq5jIc72er5HMpZmDVdNQneGwUO2trqLxxw07hhxPv_wjw3KsjPwtXfGbjvP4dXglf7Wke603TRNAWn-7W5EACgOxR8_g0R7AeB2w7Ncq_ujlXYh4b1tNvTs0wOmClGbsAabo")
                }

                connection.doOutput = true

                // Set the request parameters
                request?.let {
                    Logger.log(route.url, "request-parameters: ${request.parameters}")

                    connection.outputStream.use { os ->
                        val input =
                            request.parameters.toByteArray(charset("utf-8"))
                        os.write(input, 0, input.size)
                    }

                }

                val responseCode = connection.responseCode
                Logger.log(route.url, "response-code: $responseCode")



                if (responseCode != 200) {
                    Logger.log(route.url, "response-code: $responseCode")

                    throw RuntimeException("HttpResponseCode: $responseCode")
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

                    jsonObject.checkToken()

                    onResponse(jsonObject)
                }
            } catch (e: Exception) {
                Logger.log(route.url, "exception: ${e.message}")

                throw RuntimeException(e)
            }
        }
    }

    suspend fun execute(
        route: Route,
        request: IRequest? = null,
        onResponse: (jsonObject: JsonObject) -> Unit = {}
    ) {
        if (InternetUtils.isInternetAvailable()) {
            withContext(Dispatchers.IO) {
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

                    connection.doOutput = true

                    // Set the request parameters
                    request?.let {
                        if (route.method != Method.GET.value) {
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



                    if (responseCode != 200) {
                        Logger.log(route.url, "response-code: $responseCode")

                        throw RuntimeException("HttpResponseCode: $responseCode")
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

                        jsonObject.checkToken()

                        onResponse(jsonObject)
                    }
                } catch (e: Exception) {
                    Logger.log(route.url, "exception: ${e.message}")

                    throw RuntimeException(e)
                }
            }
        } else {
            val info = "{\"is_success\":false,\"error_message\":\"Internet is not available, check it then try again :)\"}"

            val gson = Gson()
            val jsonObject = gson.fromJson(info, JsonObject::class.java)

            println(jsonObject)
            onResponse(jsonObject)
        }
    }

    suspend fun execute3(
        route: Route,
        request: IRequest? = null,
        onResponse: (jsonObject: JsonObject) -> Unit = {}
    ) {
        withContext(Dispatchers.IO) {
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

                connection.doOutput = true

                // Set the request parameters
                request?.let {
                    if (route.method != Method.GET.value) {
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



                if (responseCode != 200) {
                    Logger.log(route.url, "response-code: $responseCode")

                    throw RuntimeException("HttpResponseCode: $responseCode")
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

                    jsonObject.checkToken()

                    onResponse(jsonObject)
                }
            } catch (e: Exception) {
                Logger.log(route.url, "exception: ${e.message}")

                throw RuntimeException(e)
            }
        }
    }
}

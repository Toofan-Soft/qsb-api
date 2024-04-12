package com.toofan.soft.qsb.api

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.repos.college.RetrieveBasicCollegesInfoRepo
import com.toofan.soft.qsb.api.services.Logger
import kotlinx.coroutines.runBlocking
import java.lang.reflect.ParameterizedType

internal interface IResponse {
    fun getResponse(jsonObject: JsonObject): IResponse? {
        return try {
            for (field in this.javaClass.declaredFields) {
                if (field.isAnnotationPresent(Field::class.java)) {
                    setField(field, jsonObject)
                }
            }
            this
        } catch (e: Exception) {
            Logger.log("IResponse.getResponse.catch(Exception)", e.message)
            e.printStackTrace()
            null
        }
    }

    private fun setField(field: java.lang.reflect.Field, jsonObject: JsonObject) {
        field.isAccessible = true
        field.getAnnotation(Field::class.java)?.value?.let {
            val value = jsonObject[it]
            try {
                println("${field.name}: ${field.type} = $value")
                val castedValue = castFieldValue(value, field)
                castedValue?.also {
                    field[this] = castedValue
                }
            } catch (e: Exception) {
                Logger.log("IResponse.setField.catch(Exception)", e.message)
                e.printStackTrace()
            }
        }
    }

    private fun getListType(field: java.lang.reflect.Field): Class<*>? {
        if (field.type == List::class.java) {
            val genericType = (field.genericType as ParameterizedType).actualTypeArguments.firstOrNull()
            if (genericType is Class<*>) {
                println("The generic type is: ${genericType}")
                return genericType as Class<*>
            } else {
                println("Unable to determine generic type.")
            }
        }
        return null
    }

    private fun castFieldValue(value: JsonElement?, field: java.lang.reflect.Field): Any? {
        return value?.let {
            println("field type: ${field.type}")
            when (val targetType = field.type) {
                Int::class.java -> value.asInt
                Long::class.java -> value.asLong
                Float::class.java -> value.asFloat
                Double::class.java -> value.asDouble
                Boolean::class.java -> value.asBoolean
                String::class.java -> if (value.isJsonNull) null else value.asString
                List::class.java -> {
                    val listType = getListType(field)
                    listType?.let {
                        when(listType) {
                            Int::class.java -> value.asJsonArray.map { it.asInt }
                            Long::class.java -> value.asJsonArray.map { it.asLong }
                            Float::class.java -> value.asJsonArray.map { it.asFloat }
                            Double::class.java -> value.asJsonArray.map { it.asDouble }
                            Boolean::class.java -> value.asJsonArray.map { it.asBoolean }
                            String::class.java -> value.asJsonArray.map { it.asString }
                            else -> {
                                if (IResponse::class.java.isAssignableFrom(listType)) {
                                    value.asJsonArray.map { jsonElement ->
                                        try {
                                            println(jsonElement.asJsonObject)
                                            println("methods: ${listType.methods.map { it.name }}")
                                            val staticMethod = listType.getDeclaredMethod("getResponse", (jsonElement.asJsonObject)::class.java)
                                            staticMethod.invoke(listType.newInstance(), jsonElement.asJsonObject)
                                        } catch (e: NoSuchMethodException) {
                                            println("Static method not found in $listType")
                                            e.printStackTrace()
                                            null
                                        } catch (e: IllegalAccessException) {
                                            println("Cannot access static method in $listType")
                                            e.printStackTrace()
                                            null
                                        }
                                    }
                                } else {
                                    null
                                }
                            }
                        }
                    }
                }
                else -> {
                    if (IResponse::class.java.isAssignableFrom(targetType)) {
                        try {
                            println("methods: ${targetType.methods.map { it.name }}")
                            val staticMethod = targetType.getDeclaredMethod("getResponse", JsonObject::class.java)
                            val invoke = staticMethod.invoke(targetType.newInstance(), value.asJsonObject)
                            invoke
                        } catch (e: NoSuchMethodException) {
                            println("Static method not found in $targetType")
                            e.printStackTrace()
                            null
                        } catch (e: IllegalAccessException) {
                            println("Cannot access static method in $targetType")
                            e.printStackTrace()
                            null
                        }
                    } else {
                        null
                    }
                }
            }
        }
    }
}


fun main() {
    val jsonObject = JsonObject()
    jsonObject.addProperty("is_success", true)

    val data = listOf(
        mapOf("id" to 2147483647, "name" to "College 1", "logo_url" to "https://example.com/logo1.png"),
        mapOf("id" to 2, "name" to "College 2", "logo_url" to "https://example.com/logo2.png"),
        // Add more colleges as needed
    )

    val dataArray = JsonArray()
    data.forEach { college ->
        val collegeObject = JsonObject()
        college.forEach { (key, value) ->
            collegeObject.addProperty(key, value.toString())
        }
        dataArray.add(collegeObject)
    }

//    data.first().let { college ->
//        val collegeObject = JsonObject()
//        college.forEach { (key, value) ->
//            collegeObject.addProperty(key, value.toString())
//        }
//        jsonObject.add("data", collegeObject)
//    }

//    (0..9).forEach {
//        dataArray.add(it)
//    }

    jsonObject.add("data", dataArray)

//    val jsonString = Gson().toJson(jsonObject)
//
//    println(jsonString)
//
//    val response = RetrieveBasicCollegesInfoRepo.Response.map(jsonObject)
//    println("response: $response")

    runBlocking {
        execute(
            jsonObject = jsonObject,
            onComplete = {
                when (it) {
                    is Resource.Success -> {
                        println("data: ${it.data}")
                    }
                    is Resource.Error -> {
                        println("message: ${it.message}")
                    }
                }
            }
        )
    }
}

suspend fun execute(
    jsonObject: JsonObject,
//    onComplete: (response: Response) -> Unit
    onComplete: (Resource<List<RetrieveBasicCollegesInfoRepo.Response.Data>>) -> Unit
) {
    runBlocking {
        val response = RetrieveBasicCollegesInfoRepo.Response.map(jsonObject)
//                onComplete(response)

        println("response: $response")

        if (response.isSuccess) {
            onComplete(Resource.Success(response.data))
        } else {
            onComplete(Resource.Error(response.errorMessage))
        }
    }
}
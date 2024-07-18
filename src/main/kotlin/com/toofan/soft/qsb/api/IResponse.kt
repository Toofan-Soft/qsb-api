package com.toofan.soft.qsb.api

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.extensions.date
import com.toofan.soft.qsb.api.extensions.datetime
import com.toofan.soft.qsb.api.extensions.time
import com.toofan.soft.qsb.api.services.Logger
import java.lang.reflect.ParameterizedType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

internal interface IResponse {
    fun getResource(): Resource<out Any> {
        val fields = this.javaClass.declaredFields

        return fields.filter { it.isAnnotationPresent(Field::class.java) }
            .firstOrNull { it.getAnnotation(Field::class.java).value == "is_success" }
            ?.let { isSuccessField ->
                isSuccessField.isAccessible = true
                val isSuccess = isSuccessField.getBoolean(this)
                if (isSuccess) {
                    fields.firstOrNull { it.isAnnotationPresent(Field::class.java) && it.getAnnotation(Field::class.java).value == "data" }
                            ?.let { dataField ->
                                dataField.isAccessible = true
                                Resource.Success(dataField.get(this))
                            } ?: Resource.Success(true)
                } else {
                    fields.firstOrNull { it.isAnnotationPresent(Field::class.java) && it.getAnnotation(Field::class.java).value == "error_message" }
                        ?.let { dataField ->
                            dataField.isAccessible = true
                            Resource.Error(dataField.get(this) as String)
                        } ?: Resource.Error("Error! error_message not found...")
                }
            } ?: Resource.Error("Error! is_success not found...")
    }

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
        field.getAnnotation(Field::class.java)?.value?.let { key ->
            val value = jsonObject[key]
            try {
                println("${field.name}: ${field.type} = $value")
                val castedValue = castFieldValue(value, field)
                castedValue?.also {
//                    field[this] = castedValue
                    field[this] = if (key.endsWith("url")) {
                        Api.IMAGES + "/" + castedValue
                    } else if (key.contains("duration")) {
                        castedValue.toString().toIntOrNull()?.div(60) ?: 0
                    } else {
                        castedValue
                    }
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
                Int::class.java, java.lang.Integer::class.java -> value.asString.toIntOrNull()
                Long::class.java, java.lang.Long::class.java -> value.asString.toLongOrNull()
                Float::class.java -> value.asFloat
                Double::class.java -> value.asDouble
                Boolean::class.java, java.lang.Boolean::class.java -> value.asBoolean
                String::class.java -> if (value.isJsonNull) null else value.asString
                LocalDateTime::class.java -> (if (value.isJsonNull) null else value.asString.toDoubleOrNull()?.toLong())?.datetime
                LocalDate::class.java -> (if (value.isJsonNull) null else value.asString.toDoubleOrNull()?.toLong())?.date
                LocalTime::class.java -> (if (value.isJsonNull) null else value.asString.toDoubleOrNull()?.toLong())?.time
                List::class.java -> {
                    val listType = getListType(field)
                    listType?.let {
                        when(listType) {
                            Int::class.java, java.lang.Integer::class.java -> value.asJsonArray.map { it.asInt }
                            Long::class.java, java.lang.Long::class.java -> value.asJsonArray.map { it.asLong }
                            Float::class.java -> value.asJsonArray.map { it.asFloat }
                            Double::class.java -> value.asJsonArray.map { it.asDouble }
                            Boolean::class.java, java.lang.Boolean::class.java -> value.asJsonArray.map { it.asBoolean }
                            String::class.java -> value.asJsonArray.map { it.asString }
                            LocalDateTime::class.java -> (if (value.isJsonNull) null else value.asString.toDoubleOrNull()?.toLong())?.datetime
                            LocalDate::class.java -> (if (value.isJsonNull) null else value.asString.toDoubleOrNull()?.toLong())?.date
                            LocalTime::class.java -> (if (value.isJsonNull) null else value.asString.toDoubleOrNull()?.toLong())?.time
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


//suspend fun execute(
//    jsonObject: JsonObject,
//    onComplete: (Resource<RetrieveFavoriteQuestionRepo.Response.Data>) -> Unit
//) {
//    runBlocking {
//        val response = RetrieveFavoriteQuestionRepo.Response.map(jsonObject)
////        val response = RetrieveFavoriteQuestionRepo.Response.Data().getResponse(jsonObject) as RetrieveFavoriteQuestionRepo.Response.Data
//
//        println("response: $response")
//
//        if (response.isSuccess) {
////            onComplete(Resource.Success(response.data))
//            val resource = response.getResource() as Resource<RetrieveFavoriteQuestionRepo.Response.Data>
//            println(resource.toString())
//
//            resource.data?.let {
//                println("1-ms: " + it.datetime1.milliseconds)
//                println("1-d: " + it.datetime1.milliseconds.date)
//                println("1-t: " + it.datetime1.milliseconds.time)
//                println("2-ms: " + it.datetime2?.milliseconds)
//                println("2-d: " + it.datetime2?.milliseconds?.date)
//                println("2-t: " + it.datetime2?.milliseconds?.time)
//
//                it.getChoices().forEach {
//                    println(it.toString())
//                }
//            }
//
//        } else {
//            onComplete(Resource.Error(response.errorMessage))
//        }
//    }
//}
//
//
//fun main() {
//    val jsonObject = JsonObject()
//    jsonObject.addProperty("is_success", true)
//
//    val data = mapOf(
//            "datetime1" to 1720126800000,
////            "datetime2" to 1720671477775,
//            "datetime2" to 0,
//
//        "chapter_name" to "ChapterName",
//            "topic_name" to "TopicName",
//            "type_name" to "TypeName",
//            "id" to 1,
//            "content" to "Content",
//            "is_true" to true,
//            "attachment_url" to null,
//            "choices" to null,
////            "" to "",
//        )
//
////    val gson = Gson()
////    val jsonData = gson.toJsonTree(data)
//
//    val gson = GsonBuilder().serializeNulls().create() // Enable serialization of null values
//    val jsonData = gson.toJsonTree(data)
//
//    jsonObject.add("data", jsonData)
//
////    val jsonElement = jsonObject["data"]
//
//    val jsonString = Gson().toJson(jsonObject)
//    println(jsonString)
//
//    runBlocking {
//        execute(
//            jsonObject = jsonObject,
////            jsonObject = jsonElement.asJsonObject,
//            onComplete = {
//                when (it) {
//                    is Resource.Success -> {
//                        println("data: ${it.data}")
//                    }
//                    is Resource.Error -> {
//                        println("message: ${it.message}")
//                    }
//                }
//            }
//        )
//    }
//}
package com.toofan.soft.qsb.api

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.repos.template.RetrieveCollegeRepo
import com.toofan.soft.qsb.api.services.Logger
import com.toofan.soft.qsb.api.session.unescapeBookends
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable

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
                val castedValue = castFieldValue(value, field.type)
                castedValue?.also {
                    field[this] = castedValue
                }
            } catch (e: Exception) {
                Logger.log("IResponse.setField.catch(Exception)", e.message)
                e.printStackTrace()
            }
        }
    }

    private fun castFieldValue(value: JsonElement?, targetType: Class<*>): Any? {
        return value?.let {
            when (targetType) {
                Integer::class.java -> value.asInt
                Long::class.java -> value.asLong
                Float::class.java -> value.asFloat
                Double::class.java -> value.asDouble
                Boolean::class.java -> value.asBoolean
                String::class.java -> if (value.isJsonNull) null else value.asString
                List::class.java -> {
//                    val type = getListComponentType(targetType.componentType())
//                    println("type: $type")
                    println("targetType.componentType(): ${targetType.componentType}")
                    println("targetType.componentType(): ${((targetType as List<*>)::class.java.genericSuperclass)}")
//                    val listType = targetType.componentType
//                    if (listType != null) {
//                        val elementType = getListComponentType(listType)
//                        println("elementType: $elementType")
//
//                    } else {
//                        println("elementType: null :(")
//                    }

//                    val listType = targetType.genericSuperclass as ParameterizedType
//                    val elementType = listType.actualTypeArguments[0] as Class<*>

//                    val listType = targetType.componentType
//                    val elementType = listType
//
//                    println("elementType: $elementType")
//                    when {
//                        elementType == Integer::class.java -> value.asJsonArray.map { it.asInt }
//                        elementType == Long::class.java -> value.asJsonArray.map { it.asLong }
//                        elementType == Float::class.java -> value.asJsonArray.map { it.asFloat }
//                        elementType == Double::class.java -> value.asJsonArray.map { it.asDouble }
//                        elementType == Boolean::class.java -> value.asJsonArray.map { it.asBoolean }
//                        elementType == String::class.java -> value.asJsonArray.map { it.asString }
//                        IResponse::class.java.isAssignableFrom(elementType) -> {
//                            value.asJsonArray.map { jsonElement ->
//                                try {
//                                    val staticMethod = elementType.getDeclaredMethod("getResponse", JsonObject::class.java)
//                                    staticMethod.invoke(elementType.newInstance(), jsonElement.asJsonObject)
//                                } catch (e: NoSuchMethodException) {
//                                    println("Static method not found in $elementType")
//                                    e.printStackTrace()
//                                    null
//                                } catch (e: IllegalAccessException) {
//                                    println("Cannot access static method in $elementType")
//                                    e.printStackTrace()
//                                    null
//                                }
//                            }
//                        }
//                        else -> null
//                    }
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

//    fun getListComponentType(targetType: Type): Type? {
//        if (targetType is ParameterizedType) {
//            val actualTypeArguments = targetType.actualTypeArguments
//            if (actualTypeArguments.isNotEmpty()) {
//                return actualTypeArguments[0]
//            }
//        }
//        return null
//    }



    private fun setField1(field: java.lang.reflect.Field, jsonObject: JsonObject) {
        field.isAccessible = true
        field.getAnnotation(Field::class.java)?.value?.let {
            val value = jsonObject[it]
            try {
                println("${field.name}= $value")
                val castedValue = castFieldValue(value, field.type)
                castedValue?.also {
                    field[this] = castedValue
                }
//            } catch (e: IllegalAccessException) {
            } catch (e: Exception) {
                Logger.log("IResponse.setField.catch(IllegalAccessException)", e.message)
                e.printStackTrace()
            }
        }
    }

    private fun castFieldValue1(value: JsonElement?, targetType: Class<*>): Any? {
//    private fun castFieldValue(value: JsonElement, targetType: Class<*>): Any? {
        return when (targetType) {
//            Int::class.java -> (value as? Number)?.toInt() ?: 0
//            Long::class.java -> (value as? Number)?.toLong() ?: 0
//            Double::class.java -> (value as? Number)?.toDouble() ?: 0.0
//            String::class.java -> value.toString().unescapeBookends()
//            Boolean::class.java -> (value.toString() as? Boolean) ?: false

            Int::class.java -> value.toString().toIntOrNull() ?: 0
//            Long::class.java -> value.toString().toLongOrNull()
            Long::class.java -> {
                value?.asLong
            }
            Double::class.java -> value.toString().toDoubleOrNull() ?: 0
//            String::class.java -> value.toString().unescapeBookends()
//            String::class.java -> (value as? String)?.unescapeBookends()
            String::class.java -> {
                println(value)
//                val v = (value as? String)?.unescapeBookends()
                val s = value?.asString
                println("s: $s")
                val v = s?.unescapeBookends()
                println("v: $v")
                println(v)
                v
            }
            Boolean::class.java -> value.toString().toBoolean()
//            IResponse::class.java -> {
//                println("Res")
//                getResponse((value as JsonElement).asJsonObject)
//            }
//            List::class.java -> getResponse((value as JsonElement).asJsonObject)
            else -> value
        }
    }
}

inline fun <reified T> getListComponentType(list: List<T>): Class<*> {
    return T::class.java
}

//fun main() {
////    val list: List<String> = listOf("apple", "banana", "orange")
//    val list: List<Int> = listOf(1, 2, 3)
//    val componentType = getListComponentType(list)
//
//    println("Component Type: ${componentType.simpleName}")
//
//
//    println(list::class.java.genericInterfaces.map { it.typeName })
//    println(list::class.java.genericSuperclass.typeName)
//
//
//    val listType = list::class.java.genericSuperclass
//    val elementType = (listType as ParameterizedType).actualTypeArguments[0]
//
//    val elementTypeName = if (elementType is TypeVariable<*>) {
//        val genericType = elementType.genericDeclaration.typeParameters
//            .firstOrNull { it.name == elementType.name }
//            ?.bounds?.get(0)
//        genericType?.typeName ?: "Unknown"
//    } else {
//        elementType.typeName
//    }
//
//    println("List generic type: $elementTypeName")
//}

fun getGenericType(classType: Class<*>): String? {
    val genericSuperclass = classType.genericSuperclass
    if (genericSuperclass is ParameterizedType) {
        val actualTypeArguments = genericSuperclass.actualTypeArguments
        if (actualTypeArguments.isNotEmpty()) {
            val typeArgument = actualTypeArguments[0]
            if (typeArgument is Class<*>) {
                return typeArgument.simpleName
            } else if (typeArgument is ParameterizedType) {
                return (typeArgument.rawType as Class<*>).simpleName
            }
        }
    }
    return null
}

fun main() {
    val list: List<String> = listOf("apple", "banana", "orange")
    val typeName = getGenericType(list.javaClass)
    println("Generic type name: $typeName")
}

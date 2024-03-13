package com.toofan.soft.qsb.api

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.services.Logger
import com.toofan.soft.qsb.api.session.unescapeBookends

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

    private fun castFieldValue(value: Any, targetType: Class<*>): Any? {
        return when (targetType) {
//            Int::class.java -> (value as? Number)?.toInt() ?: 0
//            Long::class.java -> (value as? Number)?.toLong() ?: 0
//            Double::class.java -> (value as? Number)?.toDouble() ?: 0.0
//            String::class.java -> value.toString().unescapeBookends()
//            Boolean::class.java -> (value.toString() as? Boolean) ?: false

            Int::class.java -> value.toString().toIntOrNull() ?: 0
            Long::class.java -> value.toString().toLongOrNull() ?: 0
            Double::class.java -> value.toString().toDoubleOrNull() ?: 0
//            String::class.java -> value.toString().unescapeBookends()
            String::class.java -> (value as? String)?.unescapeBookends()
            Boolean::class.java -> value.toString().toBoolean()
            else -> value
        }
    }
}
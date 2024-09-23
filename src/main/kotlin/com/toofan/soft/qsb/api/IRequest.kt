package com.toofan.soft.qsb.api

import com.toofan.soft.qsb.api.extensions.long
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

interface IRequest {
    val parametersGet: String
        get() {
            val parameters = StringBuilder()
            for (field in this.javaClass.declaredFields) {
                if (field.isAnnotationPresent(Field::class.java)) {
                    try {
                        field.isAccessible = true
                        field.getAnnotation(Field::class.java)?.value?.let {
                            val f = field[this]
                            if (f is OptionalVariable<*>) {
                                if (f.isUpdated) {
                                    if (f.value is List<*>) {
                                        parameters.append(it).append("=[")
                                        for (item in f.value as List<*>) {
                                            if (item is IRequest) {
                                                val value = item.parametersGet
                                                parameters.append(value).append(",")
                                            } else {
                                                val value = item?.toString()
                                                parameters.append(it).append("=").append(value).append(",")
                                            }
                                        }
                                        if (parameters.last() == ',') {
                                            parameters.deleteCharAt(parameters.length - 1)
                                        }
                                        parameters.append("]").append("&")
                                    } else {
                                        val value = f.value?.toString()
                                        parameters.append(it).append("=").append(value).append("&")
                                    }
                                } else {
                                }
                            } else {
                                val value = f?.toString()
                                parameters.append(it).append("=").append(value).append("&")
                            }
                        }
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                    }
                }
            }
            if (parameters.isNotEmpty()) {
                parameters.deleteCharAt(parameters.length - 1)
            }
            return parameters.toString()
        }

    val parametersPost: String
        get() {
            val parameters = StringBuilder()
            for (field in this.javaClass.declaredFields) {
                if (field.isAnnotationPresent(Field::class.java)) {
                    try {
                        field.isAccessible = true
                        field.getAnnotation(Field::class.java)?.value?.let {
                            val f = field[this]
                            if (f is OptionalVariable<*>) {
                                if (f.isUpdated) {
                                    if (f.value is List<*>) {
                                        val list = f.value as List<*>
                                        for (index in list.indices) {
                                            val item = list[index]
                                            if (item is IRequest) {
                                                val value = item.parametersList("$it[$index]")
                                                parameters.append(value).append("&")
                                            } else {
                                                val value = item?.toString()
                                                parameters.append(it).append("[]=").append(value).append("&")
                                            }
                                        }
                                    } else {
                                        if (f.value is ByteArray) {
                                            val list = (f.value as? ByteArray)?.toList()
                                            if (list == null) {
                                                parameters.append(it).append("=").append("null").append("&")
                                            } else {
                                                for (item in list) {
                                                    val value = item.toString()
                                                    parameters.append(it).append("[]=").append(value).append("&")
                                                }
                                            }
                                        } else {
                                            val value = when (f.value) {
                                                is LocalDate -> (f.value as? LocalDate)?.long
                                                is LocalTime -> (f.value as? LocalTime)?.long
                                                is LocalDateTime -> (f.value as? LocalDateTime)?.long
                                                else -> {
                                                    if (it.contains("duration")) {
                                                        f.value.toString().toLongOrNull()?.times(60)
                                                    } else {
                                                        f.value
                                                    }
                                                }
                                            }?.toString()

                                            parameters.append(it).append("=").append(value).append("&")
                                        }
                                    }
                                } else {
                                }
                            } else {
                                if (f is List<*>) {
                                    val list = f as List<*>
                                    for (index in list.indices) {
                                        val item = list[index]
                                        if (item is IRequest) {
                                            val value = item.parametersList("$it[$index]")
                                            parameters.append(value).append("&")
                                        } else {
                                            val value = item?.toString()
                                            parameters.append(it).append("[]=").append(value).append("&")
                                        }
                                    }
                                } else {
                                    if (f is ByteArray) {
                                        val list = (f as? ByteArray)?.toList()
                                        if (list == null) {
                                            parameters.append(it).append("=").append("null").append("&")
                                        } else {
                                            for (item in list) {
                                                val value = item.toString()
                                                parameters.append(it).append("[]=").append(value).append("&")
                                            }
                                        }
                                    } else {
                                        val value = when (f) {
                                            is LocalDate -> (f as? LocalDate)?.long
                                            is LocalTime -> (f as? LocalTime)?.long
                                            is LocalDateTime -> (f as? LocalDateTime)?.long
                                            else -> {
                                                if (it.contains("duration")) {
                                                    f.toString().toLongOrNull()?.times(60)
                                                } else {
                                                    f
                                                }
                                            }
                                        }?.toString()

                                        parameters.append(it).append("=").append(value).append("&")
                                    }
                                }
                            }
                        }
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                    }
                }
            }
            if (parameters.isNotEmpty()) {
                parameters.deleteCharAt(parameters.length - 1)
            }
            return parameters.toString()
        }

    private fun parametersList(prefix: String): String {
        val parameters = StringBuilder()
        for (field in this.javaClass.declaredFields) {
            if (field.isAnnotationPresent(Field::class.java)) {
                try {
                    field.isAccessible = true
                    field.getAnnotation(Field::class.java)?.value?.let {
                        val f = field[this]
                        if (f is OptionalVariable<*>) {
                            if (f.isUpdated) {
                                val value = f.value?.toString()
                                parameters.append(prefix).append("[").append(it).append("]=").append(value).append("&")
                            } else {

                            }
                        } else {
                            val value = f?.toString()
                            parameters.append(prefix).append("[").append(it).append("]=").append(value).append("&")
                        }
                    }
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
        }
        if (parameters.isNotEmpty()) {
            parameters.deleteCharAt(parameters.length - 1)
        }
        return parameters.toString()
    }

    fun <T : IRequest> build(block: T.() -> Unit): T {
        @Suppress("UNCHECKED_CAST")
        block(this as T)
        return this
    }
}
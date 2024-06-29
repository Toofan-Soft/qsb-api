package com.toofan.soft.qsb.api

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
                                } else {}
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
//                                                parameters.append(it).append("=").append(value).append("&")
                                            }
                                        }
                                    } else {
                                        val value = f.value?.toString()
                                        parameters.append(it).append("=").append(value).append("&")
                                    }
                                } else {}
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
                                    val value = f?.toString()
                                    parameters.append(it).append("=").append(value).append("&")
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

//    val parameters2: String
//        get() {
//            val parameters = StringBuilder()
//            for (field in this.javaClass.declaredFields) {
//                if (field.isAnnotationPresent(Field::class.java)) {
//                    try {
//                        field.isAccessible = true
//                        field.getAnnotation(Field::class.java)?.value?.let {
//                            val f = field[this]
//                            if (f is OptionalVariable<*>) {
//                                if (f.isUpdated) {
//                                    if (f.value is List<*>) {
//                                        parameters.append("\"").append(it).append("\"").append(":[")
//                                        for (item in f.value as List<*>) {
////                                            parameters.append("{")
//                                            if (item is IRequest) {
//                                                val value = item.parameters2
//                                                parameters.append(value).append(",")
//                                            } else {
//                                                val value = item?.toString()
//                                                parameters.append("\"").append(it).append("\"").append(":")
//                                                if (item is String) {
//                                                    parameters.append("\"").append(value).append("\"").append(",")
//                                                } else {
//                                                    parameters.append(value).append(",")
//                                                }
//                                            }
////                                            parameters.append("},")
//                                        }
//                                        if (parameters.last() == ',') {
//                                            parameters.deleteCharAt(parameters.length - 1)
//                                        }
//                                        parameters.append("]").append(",")
//                                    } else {
//                                        val value = f.value?.toString()
//                                        parameters.append("\"").append(it).append("\"").append(":")
//                                        if (f.value is String) {
//                                            parameters.append("\"").append(value).append("\"").append(",")
//                                        } else {
//                                            parameters.append(value).append(",")
//                                        }
//                                    }
//                                } else {}
//                            } else {
//                                val value = f?.toString()
//                                parameters.append("\"").append(it).append("\"").append(":")
//                                if (f is String) {
//                                    parameters.append("\"").append(value).append("\"").append(",")
//                                } else {
//                                    parameters.append(value).append(",")
//                                }
//                            }
//                        }
//                    } catch (e: IllegalAccessException) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//            if (parameters.isNotEmpty()) {
//                parameters.deleteCharAt(parameters.length - 1)
//            }
//            return "{$parameters}"
//        }
//
//    val parameters1: String
//        get() {
//            val parameters = StringBuilder()
//            for (field in this::class.java.declaredFields) {
//                if (field.isAnnotationPresent(Field::class.java)) {
//                    try {
//                        field.isAccessible = true
//                        field.getAnnotation(Field::class.java)?.value?.let { annotationValue ->
//                            val fieldValue = field[this]
//                            if (fieldValue is OptionalVariable<*>) {
//                                if (fieldValue.isUpdated) {
//                                    processField(annotationValue, fieldValue.value, parameters)
//                                }
//                            } else {
//                                processField(annotationValue, fieldValue, parameters)
//                            }
//                        }
//                    } catch (e: IllegalAccessException) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//            if (parameters.isNotEmpty()) {
//                parameters.deleteCharAt(parameters.length - 1)
//            }
//            return parameters.toString()
//        }


//    private fun processField(name: String, value: Any?, parameters: StringBuilder) {
//        if (value is List<*>) {
//            parameters.append(name).append("=[")
//            value.forEach { item ->
//                if (item is IRequest) {
//                    parameters.append("{")
//                    parameters.append(item.parameters)
//                    parameters.append("},")
//                } else {
//                    parameters.append(item.toString()).append(",")
//                }
//            }
//            if (parameters.last() == ',') {
//                parameters.deleteCharAt(parameters.length - 1)
//            }
//            parameters.append("]&")
//        } else {
//            val encodedValue = URLEncoder.encode(value?.toString() ?: "", "UTF-8")
//            parameters.append(name).append("=").append(encodedValue).append("&")
//        }
//    }


//    val parameters1: String
//        get() {
//            val parameters = StringBuilder()
//            for (field in this.javaClass.declaredFields) {
//                if (field.isAnnotationPresent(Field::class.java)) {
//                    try {
//                        field.isAccessible = true
//                        field.getAnnotation(Field::class.java)?.value?.let {
//
//                            val f = field[this]
//                            if (f is OptionalVariable<*>) {
//                                if (f.isUpdated) {
//                                    val value = f.value.toString()
//                                    parameters.append(it).append("=").append(value).append("&")
//                                } else {
//
//                                }
//                            } else {
//                                val value = f?.toString()
//                                parameters.append(it).append("=").append(value).append("&")
//                            }
//                        }
//                    } catch (e: IllegalAccessException) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//            if (parameters.isNotEmpty()) {
//                parameters.deleteCharAt(parameters.length - 1)
//                return "?$parameters"
//            } else {
//                return parameters.toString()
//            }
//        }

    fun <T : IRequest> build(block: T.() -> Unit): T {
        @Suppress("UNCHECKED_CAST")
        block(this as T)
        return this
    }
}

fun IRequest.string(): String {
    val string = StringBuilder()
    for (field in this.javaClass.declaredFields) {
        if (field.isAnnotationPresent(Field::class.java)) {
            try {
                field.isAccessible = true

                val key = field.getAnnotation(Field::class.java).value
                string.append(key).append("=")

                val value = field.get(this)
                if (value is OptionalVariable<*>) {
                    string.append(value.value)
                } else {
                    string.append(value)
                }
                string.append(", ")
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
    }
    if (string.isNotEmpty()) {
        string.deleteCharAt(string.length - 1)
        string.deleteCharAt(string.length - 1)
    }
    return "${this.javaClass.name}($string)"
}



//    fun interface Optional<T> {
//        operator fun invoke(block: T.() -> Unit)
//    }
//
//    fun<T: IRequest> optional(block: T.() -> Unit): T {
//        return build(block)
//    }
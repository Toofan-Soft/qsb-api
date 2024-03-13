package com.toofan.soft.qsb.api

interface IRequest {
    val parameters: String
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
                                    val value = f.value.toString()
                                    parameters.append(it).append("=").append(value).append("&")
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

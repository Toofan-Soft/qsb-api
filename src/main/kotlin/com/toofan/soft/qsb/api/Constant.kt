package com.toofan.soft.qsb.api

private object Constant {
    //    private const val URL = "http://192.168.1.14:80/"
//    private const val URL = "http://192.168.1.16:8000/"
    //    private const val URL = "http://127.0.0.1:8000/"
        private const val URL = "http://26.21.87.97:8000/"
    const val HOME = URL + "api"
}

enum class Route(
    private val _name: String,
    private val _method: Method,
    private val _isAuthorized: Boolean = false
) {
    REGISTER("register", Method.POST),
    LOGIN("login", Method.POST),
    USERINFO("userinfo", Method.GET, true),

    STUDENTS("students", Method.GET);

    internal val isAuthorized: Boolean
        get() = _isAuthorized

    internal val url: String
        get() = "${Constant.HOME}/$_name"

    internal val method: String
        get() = _method.value
}

internal enum class Method(internal val value: String) {
    POST("POST"),
    GET("GET")
}
package com.toofan.soft.qsb.api

private object Constant {
        private const val URL = "http://26.21.87.97:8000/"
    const val HOME = URL + "api"
}

sealed class Route(
    private val _path: String,
    private val _method: Method,
    private val _isAuthorized: Boolean
) {
    internal val url get() = "${Constant.HOME}/$_path"
    internal val method get() = _method.value
    internal val isAuthorized get() = _isAuthorized

    sealed class User(
        name: String,
        method: Method,
        isAuthorized: Boolean = false
    ): Route("user/$name", method, isAuthorized) {
        object Register: User("add", Method.POST)
        object Login: User("modify", Method.POST)
        object Profile: User("delete", Method.GET, true)
    }
    sealed class University(
        name: String,
        method: Method,
        isAuthorized: Boolean = false
    ): Route("university/$name", method, isAuthorized) {
        object Add: University("add", Method.POST)
        object Modify: University("modify", Method.PUT)
        object Delete: University("delete", Method.DELETE)
        object Retrieve: University("retrieve", Method.GET)
    }

    sealed class College(
        name: String,
        method: Method,
        isAuthorized: Boolean = false
    ): Route("college/$name", method, isAuthorized) {
        object Add: College("add", Method.POST)
        object Modify: College("modify", Method.PUT)
        object Delete: College("delete", Method.DELETE)
        object Retrieve: College("retrieve", Method.GET)
        object RetrieveList: College("retrieve-list", Method.GET)
        object RetrieveBasicInfoList: College("retrieve-basic-info-list", Method.GET)
    }
}

internal enum class Method(internal val value: String) {
    POST("POST"),
    GET("GET"),
    PUT("PUT"),
    DELETE("DELETE");
}

fun main() {
    val url = Route.College.RetrieveList.url
    println(url)
}
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
        object Retrieve: University("retrieve", Method.GET)
        object RetrieveBasicInfo: University("retrieve-basic-info", Method.GET)
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

    sealed class Department(
        name: String,
        method: Method,
        isAuthorized: Boolean = false
    ): Route("department/$name", method, isAuthorized) {
        object Add: Department("add", Method.POST)
        object Modify: Department("modify", Method.PUT)
        object Delete: Department("delete", Method.DELETE)
        object Retrieve: Department("retrieve", Method.GET)
        object RetrieveList: Department("retrieve-list", Method.GET)
        object RetrieveBasicInfoList: Department("retrieve-basic-info-list", Method.GET)
    }

    sealed class Course(
        name: String,
        method: Method,
        isAuthorized: Boolean = false
    ): Route("course/$name", method, isAuthorized) {
        object Add: Course("add", Method.POST)
        object Modify: Course("modify", Method.PUT)
        object Delete: Course("delete", Method.DELETE)
        object RetrieveList: Course("retrieve-list", Method.GET)
    }

    sealed class CoursePart(
        name: String,
        method: Method,
        isAuthorized: Boolean = false
    ): Route("course-part/$name", method, isAuthorized) {
        object Add: CoursePart("add", Method.POST)
        object Modify: CoursePart("modify", Method.PUT)
        object Delete: CoursePart("delete", Method.DELETE)
        object RetrieveList: CoursePart("retrieve-list", Method.GET)
    }

    sealed class Chapter(
        name: String,
        method: Method,
        isAuthorized: Boolean = false
    ): Route("chapter/$name", method, isAuthorized) {
        object Add: Chapter("add", Method.POST)
        object Modify: Chapter("modify", Method.PUT)
        object Delete: Chapter("delete", Method.DELETE)
        object Retrieve: Chapter("retrieve", Method.GET)
        object RetrieveList: Chapter("retrieve-list", Method.GET)
        object RetrieveAvailableList: Chapter("retrieve-available-list", Method.GET)
        object RetrieveDescription: Chapter("retrieve-description", Method.GET)
    }

    sealed class Topic(
        name: String,
        method: Method,
        isAuthorized: Boolean = false
    ): Route("topic/$name", method, isAuthorized) {
        object Add: Topic("add", Method.POST)
        object Modify: Topic("modify", Method.PUT)
        object Delete: Topic("delete", Method.DELETE)
        object Retrieve: Topic("retrieve", Method.GET)
        object RetrieveList: Topic("retrieve-list", Method.GET)
        object RetrieveAvailableList: Topic("retrieve-available-list", Method.GET)
        object RetrieveDescription: Topic("retrieve-description", Method.GET)
    }

    sealed class Question(
        name: String,
        method: Method,
        isAuthorized: Boolean = false
    ): Route("question/$name", method, isAuthorized) {
        object Add: Question("add", Method.POST)
        object Modify: Question("modify", Method.PUT)
        object Delete: Question("delete", Method.DELETE)
        object Retrieve: Question("retrieve", Method.GET)
        object RetrieveList: Question("retrieve-list", Method.GET)
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
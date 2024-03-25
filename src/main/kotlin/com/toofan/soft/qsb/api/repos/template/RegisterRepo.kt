package com.toofan.soft.qsb.api.repos.template

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.Route
import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object RegisterRepo {
    @JvmStatic
    fun execute(name: String, email: String?, password: String?, onComplete: (response: Response) -> Unit) {
        runBlocking {
            val request = Request(name, email, password)
            ApiExecutor.execute(
//                route = Route.User.Register,
                route = Route.Template.Register,
                request = request
            ) { jsonObject ->
                println("jsonObject: $jsonObject")
                val response = Response.map(jsonObject)
                onComplete(response)
            }
        }
    }

    private data class Request(
        @Field("name")
        val name: String?,
        @Field("email")
        val email: String?,
        @Field("password")
        val password: String?
    ) : IRequest

    data class Response(
        @Field("is_success")
        internal val isSuccess: Boolean = false,
        @Field("error_message")
        internal val errorMessage: String? = null
    ) : IResponse {
        companion object {
            private fun getInstance(): Response {
                return Response()
            }

            fun map(data: JsonObject): Response {
                return getInstance().getResponse(data) as Response
            }
        }
    }
}

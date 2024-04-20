package com.toofan.soft.qsb.api.repos.template

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object UserInfoRepo {
    @JvmStatic
    fun execute(onComplete: (Response) -> Unit) {
        runBlocking {
            ApiExecutor.execute(
                route = Route.Template.UserInfo
            ) { jsonObject ->
//                val data = jsonObject.get("user")
//                println("data: $data")

                val response = Response.map(jsonObject)
                onComplete(response)
            }
        }
    }

    data class Response(
        @Field("id")
        internal val id: Int = 0,
        @Field("name")
        internal val name: String? = null,
        @Field("email")
        internal val email: String? = null
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

package com.toofan.soft.qsb.api.repos.template

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object StudentsRepo {
    @JvmStatic
    fun execute(onComplete: (List<Response>) -> Unit) {
        runBlocking {
//            ApiExecutor.execute(
//                route = Route.STUDENTS
//            )  { jsonObject ->
//                val data = jsonObject.get("students").asJsonArray
//                println(data)
//
//                val response = data.map {
//                    Response.map(it.asJsonObject)
//                }
//                onComplete(response)
//            }
        }
    }

    data class Response(
        @Field("id")
        internal val id: Long = 0,
        @Field("name")
        internal val name: String? = null,
        @Field("number")
        internal val number: Long = 0
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

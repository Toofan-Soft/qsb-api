package com.toofan.soft.qsb.api.repos.enums

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*

object RetrieveExamTypesRepo {
    @JvmStatic
    suspend fun execute(
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        Coroutine.launch {
            ApiExecutor.execute(
                route = Route.Enum.RetrieveExamTypeList
            ) {
                onComplete(Response.map(it).getResource() as Resource<List<Response.Data>>)
            }
        }
    }

    data class Response(
        @Field("is_success")
        val isSuccess: Boolean = false,
        @Field("error_message")
        val errorMessage: String? = null,
        @Field("data")
        val data: List<Data> = emptyList()
    ) : IResponse {

        data class Data(
            @Field("id")
            val id: Int = 0,
            @Field("name")
            val name: String = "",
        ) : IResponse

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

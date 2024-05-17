package com.toofan.soft.qsb.api.repos.university

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*

object RetrieveBasicUniversityInfoRepo {
    @JvmStatic
    suspend fun execute(
        onComplete: (Resource<Response.Data>) -> Unit
    ) {
        Coroutine.launch {
            ApiExecutor.execute(
                route = Route.University.RetrieveBasicInfo
            ) {
                onComplete(Response.map(it).getResource() as Resource<Response.Data>)
            }
        }
    }

    data class Response(
        @Field("is_success")
        val isSuccess: Boolean = false,
        @Field("error_message")
        val errorMessage: String? = null,
        @Field("data")
        val data: Data? = null
    ) : IResponse {

        data class Data(
            @Field("arabic_name")
            val arabicName: String = "",
            @Field("english_name")
            val englishName: String = "",
            @Field("logo_url")
            val logoUrl: String = ""
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

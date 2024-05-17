package com.toofan.soft.qsb.api.repos.university

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*

object RetrieveUniversityInfoRepo {
    @JvmStatic
    suspend fun execute(
        onComplete: (Resource<Response.Data>) -> Unit
    ) {
        Coroutine.launch {
            ApiExecutor.execute(
                route = Route.University.Retrieve
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
            val logoUrl: String = "",
            @Field("phone")
            val phone: Long? = null,
            @Field("email")
            val email: String? = null,
            @Field("address")
            val address: String? = null,
            @Field("description")
            val description: String? = null,
            @Field("web")
            val web: String? = null,
            @Field("youtube")
            val youtube: String? = null,
            @Field("x_platform")
            val xPlatform: String? = null,
            @Field("facebook")
            val facebook: String? = null,
            @Field("telegram")
            val telegram: String? = null
        )

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

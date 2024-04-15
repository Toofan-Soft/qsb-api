package com.toofan.soft.qsb.api.repos.university

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object RetrieveUniversityInfoRepo {
    @JvmStatic
    fun execute(
        onComplete: (Resource<Response.Data>) -> Unit
    ) {
        runBlocking {
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
            private val _description: String? = null,
            @Field("web")
            private val _web: String? = null,
            @Field("youtube")
            private val _youtube: String? = null,
            @Field("x_platform")
            private val _xPlatform: String? = null,
            @Field("facebook")
            private val _facebook: String? = null,
            @Field("telegram")
            private val _telegram: String? = null
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

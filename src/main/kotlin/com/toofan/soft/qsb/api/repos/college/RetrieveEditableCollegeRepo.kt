package com.toofan.soft.qsb.api.repos.college

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*

object RetrieveEditableCollegeRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Response.Data>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { id ->
                request = Request(id)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.College.RetrieveEditable,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Response.Data>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            id: Int
        )
    }

    data class Request(
        @Field("id")
        private val _id: Int
    ) : IRequest

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
            @Field("phone")
            val phone: Long? = null,
            @Field("email")
            val email: String? = null,
            @Field("description")
            val description: String? = null,
            @Field("youtube")
            val youtube: String? = null,
            @Field("x_platform")
            val xPlatform: String? = null,
            @Field("facebook")
            val facebook: String? = null,
            @Field("telegram")
            val telegram: String? = null,
            @Field("logo_url")
            val logoUrl: String? = null
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

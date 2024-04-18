package com.toofan.soft.qsb.api.repos.college

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object RetrieveCollegesRepo {
    @JvmStatic
    suspend fun execute(
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            ApiExecutor.execute(
                route = Route.College.RetrieveList
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
            @Field("arabic_name")
            val arabicName: String = "",
            @Field("english_name")
            val englishName: String = "",
            @Field("phone")
            val phone: Long? = null,
            @Field("email")
            val email: String? = null,
            @Field("logo_url")
            val logoUrl: String? = null
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

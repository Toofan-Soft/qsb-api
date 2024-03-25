package com.toofan.soft.qsb.api.repos.template

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import kotlinx.coroutines.runBlocking

object RetrieveCollegesRepo {
    @JvmStatic
    fun execute(
        onComplete: (response: Response) -> Unit
    ) {
        runBlocking {
            ApiExecutor.execute(
                route = Route.Template.RetrieveColleges
            ) {
                println("json: $it")
                val response = Response.map(it)
                println("response: $response")
                onComplete(response)
            }
        }
    }

    data class Response(
        @Field("is_success")
        val isSuccess: Boolean = false,
        @Field("error_message")
        val errorMessage: String? = null,
        @Field("data")
        val data: List<Data> = List<Data>(5) { Data() }
    ) : IResponse {

        data class Data(
        @Field("id")
        val id: Int? = null,
        @Field("arabic_name")
        val arabicName: String = "",
        @Field("english_name")
        val englishName: String = "",
        @Field("phone")
        val phone: Long? = null
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

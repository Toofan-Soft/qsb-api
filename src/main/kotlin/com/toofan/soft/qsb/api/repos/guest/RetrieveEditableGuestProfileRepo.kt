package com.toofan.soft.qsb.api.repos.guest

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object RetrieveEditableGuestProfileRepo {
    @JvmStatic
    fun execute(
        onComplete: (Resource<Response.Data>) -> Unit
    ) {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.Guest.RetrieveEditable
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
            @Field("name")
            val name: String,
            @Field("gender_id")
            val genderId: Int,
            @Field("phone")
            val phone: Long? = null,
            @Field("image_url")
            val imageUrl: String? = null
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

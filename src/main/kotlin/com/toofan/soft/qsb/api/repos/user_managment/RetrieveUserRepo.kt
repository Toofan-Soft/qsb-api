package com.toofan.soft.qsb.api.repos.user_managment

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object RetrieveUserRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Response.Data>) -> Unit
    ) {
        var request: Request? = null

        data.invoke { id ->
            request = Request(id)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.UserManagement.Retrieve
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
            @Field("email")
            val email: String,
            @Field("status_name")
            val statusName: String,
            @Field("is_active")
            val isActive: Boolean,
            @Field("owner_type_name")
            val ownerTypeName: String,
            @Field("owner_name")
            val ownerName: String,
            @Field("roles")
            val roles: Data,
            @Field("image_url")
            val imageUrl: String? = null
        ) {
            data class Data(
                @Field("id")
                val id: Int,
                @Field("name")
                val name: String,
                @Field("is_selected")
                val isSelected: Boolean,
                @Field("is_mandatory")
                val isMandatory: Boolean
            )
        }

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

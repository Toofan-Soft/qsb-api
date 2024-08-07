package com.toofan.soft.qsb.api.repos.user_managment

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*

object RetrieveUserRepo {
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
                    route = Route.UserManagement.Retrieve,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Response.Data>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            id: String
        )
    }

    data class Request(
        @Field("id")
        private val _id: String
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
            val email: String = "",
            @Field("status_name")
            val statusName: String = "",
            @Field("is_active")
            val isActive: Boolean = false,
            @Field("owner_type_name")
            val ownerTypeName: String = "",
            @Field("owner_name")
            val ownerName: String = "",
            @Field("roles")
            val roles: List<Data> = emptyList(),
            @Field("image_url")
            val imageUrl: String? = null,

            @Field("is_deletable")
            val isDeletable: Boolean = false
        ) : IResponse {
            data class Data(
                @Field("id")
                val id: Int = 0,
                @Field("name")
                val name: String = "",
                @Field("is_selected")
                val isSelected: Boolean = false,
                @Field("is_mandatory")
                val isMandatory: Boolean = false
            ) : IResponse
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

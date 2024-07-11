package com.toofan.soft.qsb.api.repos.user_managment

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*

object RetrieveUsersRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { ownerTypeId, roleId ->
                request = Request(ownerTypeId, roleId)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.UserManagement.RetrieveList,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<List<Response.Data>>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            ownerTypeId: Int,
            roleId: Int
        )
    }

    data class Request(
        @Field("owner_type_id")
        private val _ownerTypeId: Int,
        @Field("role_id")
        private val _roleId: Int
    ) : IRequest

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
            val id: String = "",
            @Field("owner_name")
            val ownerName: String = "",
            @Field("email")
            val email: String = "",
            @Field("status_name")
            val statusName: String = "",
            @Field("image_url")
            val imageUrl: String? = null
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

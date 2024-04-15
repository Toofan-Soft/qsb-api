package com.toofan.soft.qsb.api.repos.user_managment

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object RetrieveOwnerRolesRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        var request: Request? = null

        data.invoke { ownerTypeId ->
            request = Request(ownerTypeId)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.UserManagement.RetrieveOwnerRoleList
                ) {
                    onComplete(Response.map(it).getResource() as Resource<List<Response.Data>>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            ownerTypeId: Int
        )
    }

    data class Request(
        @Field("owner_type_id")
        private val _ownerTypeId: Int
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
            val id: Int = 0,
            @Field("name")
            val name: String = "",
            @Field("is_mandatory")
            val isMandatory: Boolean = false
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

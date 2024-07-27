package com.toofan.soft.qsb.api.repos.filter

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*

object RetrieveNonOwnerStudentsRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { departmentId, levelId ->
                request = Request(departmentId, levelId)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.Filter.RetrieveNonOwnerStudentList,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<List<Response.Data>>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            departmentId: Int,
            levelId: Int
        )
    }

    data class Request(
        @Field("department_id")
        private val _departmentId: Int,
        @Field("level_id")
        private val _levelId: Int
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
            val name: String = ""
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

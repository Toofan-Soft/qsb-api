package com.toofan.soft.qsb.api.repos.filter

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*

object RetrieveNonOwnerEmployeesRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { jobTypeId ->
                request = Request(jobTypeId)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.Filter.RetrieveNonOwnerEmployeeList,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<List<Response.Data>>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            jobTypeId: Int
        )
    }

    data class Request(
        @Field("job_type_id")
        private val _jobTypeId: Int
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

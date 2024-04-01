package com.toofan.soft.qsb.api.repos.filter

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import kotlinx.coroutines.runBlocking

object RetrieveDepartmentLevelsRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
//        onComplete: (response: Response) -> Unit
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        var request: Request? = null

        data.invoke { departmentId ->
            request = Request(departmentId)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.Filter.RetrieveDepartmentLevelList
                ) {
                    val response = Response.map(it)
//                    onComplete(response)

                    if (response.isSuccess) {
                        onComplete(Resource.Success(response.data))
                    } else {
                        onComplete(Resource.Error(response.errorMessage))
                    }
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            departmentId: Int
        )
    }

    data class Request(
        @Field("department_id")
        private val _departmentId: Int
    ) : IRequest

    data class Response(
        @Field("is_success")
        val isSuccess: Boolean = false,
        @Field("error_message")
        val errorMessage: String? = null,
        @Field("data")
        val data: List<Data>? = null
    ) : IResponse {

        data class Data(
            @Field("id")
            val id: Int,
            @Field("name")
            val name: String
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

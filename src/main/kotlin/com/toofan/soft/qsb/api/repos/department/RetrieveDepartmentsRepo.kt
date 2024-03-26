package com.toofan.soft.qsb.api.repos.department

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import kotlinx.coroutines.runBlocking

object RetrieveDepartmentsRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke { collegeId ->
            request = Request(collegeId)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.Department.RetrieveList
                ) {
                    val response = Response.map(it)
                    onComplete(response)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            collegeId: Int
        )
    }

    data class Request(
        @Field("college_id")
        private val _collegeId: Int
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
            @Field("arabic_name")
            val arabicName: String,
            @Field("english_name")
            val englishName: String,
            @Field("level_count")
            val levelCount: Int,
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
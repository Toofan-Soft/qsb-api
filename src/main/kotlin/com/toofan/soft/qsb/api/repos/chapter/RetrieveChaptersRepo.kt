package com.toofan.soft.qsb.api.repos.chapter

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import kotlinx.coroutines.runBlocking

object RetrieveChaptersRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke { coursePartId ->
            request = Request(coursePartId)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.Chapter.RetrieveList
                ) {
                    val response = Response.map(it)
                    onComplete(response)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            coursePartId: Int
        )
    }

    data class Request(
        @Field("course_part_id")
        private val _coursePartId: Int
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
            @Field("arabic_title")
            val arabicTitle: String,
            @Field("english_title")
            val englishTitle: String,
            @Field("status_name")
            val statusName: String,
            @Field("description")
            val description: String? = null,
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
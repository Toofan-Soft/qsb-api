package com.toofan.soft.qsb.api.repos.favorite_question

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import kotlinx.coroutines.runBlocking

object RetrieveFavoriteQuestionsRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke { departmentCoursePartId ->
            request = Request(departmentCoursePartId)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.FavoriteQuestion.RetrieveList
                ) {
                    val response = Response.map(it)
                    onComplete(response)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            departmentCoursePartId: Int
        )
    }

    data class Request(
        @Field("department_course_part_id")
        private val _departmentCoursePartId: Int
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
            @Field("question_id")
            val questionId: Int,
            @Field("combination_id")
            val combinationId: Int? = null,
            @Field("content")
            val content: String,
            @Field("type_name")
            val typeName: String,
            @Field("datetime")
            val datetime: Long
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

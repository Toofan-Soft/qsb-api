package com.toofan.soft.qsb.api.repos.favorite_question

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object RetrieveFavoriteQuestionsRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            var request: Request? = null

            data.invoke { departmentCoursePartId ->
                request = Request(departmentCoursePartId)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.FavoriteQuestion.RetrieveList
                ) {
                    onComplete(Response.map(it).getResource() as Resource<List<Response.Data>>)
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
        val data: List<Data> = emptyList()
    ) : IResponse {

        data class Data(
            @Field("question_id")
            val questionId: Int = 0,
            @Field("content")
            val content: String = "",
            @Field("type_name")
            val typeName: String = "",
            @Field("datetime")
            val datetime: Long = 0,
            @Field("combination_id")
            val combinationId: Int? = null
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

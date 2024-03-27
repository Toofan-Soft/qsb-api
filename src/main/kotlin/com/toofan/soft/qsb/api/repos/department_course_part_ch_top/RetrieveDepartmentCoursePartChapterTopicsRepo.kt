package com.toofan.soft.qsb.api.repos.department_course_part_ch_top

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import kotlinx.coroutines.runBlocking

object RetrieveDepartmentCoursePartChapterTopicsRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke { departmentCoursePartId, chapterId ->
            request = Request(departmentCoursePartId, chapterId)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.DepartmentCoursePartChapterAndTopic.RetrieveTopicList
                ) {
                    val response = Response.map(it)
                    onComplete(response)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            departmentCoursePartId: Int,
            chapterId: Int
        )
    }

    data class Request(
        @Field("department_course_part_id")
        private val _departmentCoursePartId: Int,
        @Field("chapter_id")
        private val _chapterId: Int
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
            val englishTitle: String
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

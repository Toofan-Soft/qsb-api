package com.toofan.soft.qsb.api.repos.department_course_part_ch_top

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*

object RetrieveEditableDepartmentCoursePartChapterTopicsRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { departmentCoursePartId, chapterId ->
                request = Request(departmentCoursePartId, chapterId)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.DepartmentCoursePartChapterAndTopic.RetrieveEditableTopicList,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<List<Response.Data>>)
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
        val data: List<Data> = emptyList()
    ) : IResponse {

        data class Data(
            @Field("id")
            val id: Int = 0,
            @Field("arabic_title")
            val arabicTitle: String = "",
            @Field("english_title")
            val englishTitle: String = "",
            @Field("is_selected")
            val isSelected: Boolean = false
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

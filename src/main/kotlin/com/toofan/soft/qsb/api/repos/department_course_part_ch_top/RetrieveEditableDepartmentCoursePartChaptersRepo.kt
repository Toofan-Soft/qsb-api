package com.toofan.soft.qsb.api.repos.department_course_part_ch_top

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*

object RetrieveEditableDepartmentCoursePartChaptersRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { departmentCoursePartId ->
                request = Request(departmentCoursePartId)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.DepartmentCoursePartChapterAndTopic.RetrieveEditableChapterList,
                    request = it
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
            @Field("id")
            val id: Int = 0,
            @Field("arabic_title")
            val arabicTitle: String = "",
            @Field("english_title")
            val englishTitle: String = "",
            @Field("selection_status")
            val selectionStatus: Data = Data()
        ) : IResponse {
            data class Data(
                @Field("is_nune")
                val isNune: Boolean = false,
                @Field("is_half")
                val isHalf: Boolean = false,
                @Field("is_full")
                val isFull: Boolean = false
            ) : IResponse
        }

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

package com.toofan.soft.qsb.api.repos.department_course_part_ch_top

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import kotlinx.coroutines.runBlocking

object RetrieveAvailavleDepartmentCoursePartChaptersRepo {
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
                    route = Route.Topic.RetrieveList
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
            @Field("id")
            val id: Int,
            @Field("arabic_title")
            val arabicTitle: String,
            @Field("english_title")
            val englishTitle: String,
            @Field("selection_status")
            val selectionStatus: SelectionStatus
        ) {
            data class SelectionStatus(
                @Field("is_nune")
                val isNune: Boolean,
                @Field("is_half")
                val isHalf: Boolean,
                @Field("is_full")
                val isFull: Boolean
            )
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

package com.toofan.soft.qsb.api.repos.paper_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object RetrievePaperExamsRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        var request: Request? = null

        data.invoke(
            { departmentCoursePartId ->
                request = Request(departmentCoursePartId)
            },
            { request!!.optional(it) }
        )

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.PaperExam.RetrieveList
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

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("department_course_part_id")
        private val _departmentCoursePartId: Int,
        @Field("type_id")
        private val _typeId: OptionalVariable<Int> = OptionalVariable()
    ) : IRequest {
        val typeId = loggableProperty(_typeId)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }

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
            @Field("datetime")
            val datetime: Long = 0,
            @Field("forms_count")
            val formsCount: Int = 0,
            @Field("score")
            val score: Float = 0.0f,
            @Field("lecturer_name")
            val lecturerName: String = "",
            @Field("type_name")
            val typeName: String? = null
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

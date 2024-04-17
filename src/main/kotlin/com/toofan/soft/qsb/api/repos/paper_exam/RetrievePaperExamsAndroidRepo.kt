package com.toofan.soft.qsb.api.repos.paper_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object RetrievePaperExamsAndroidRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        var request: Request? = null

        data.invoke { request!!.optional(it) }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.PaperExam.RetrieveAndroidList
                ) {
                    onComplete(Response.map(it).getResource() as Resource<List<Response.Data>>)
                }
            }
        }
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("department_course_part_id")
        private val _departmentCoursePartId: OptionalVariable<Int> = OptionalVariable(),
        @Field("type_id")
        private val _typeId: OptionalVariable<Int> = OptionalVariable()
    ) : IRequest {
        val departmentCoursePartId = loggableProperty(_departmentCoursePartId)
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
            @Field("course_name")
            val courseName: String = "",
            @Field("course_part_name")
            val coursePartName: String = "",
            @Field("language_name")
            val languageName: String = "",
            @Field("datetime")
            val datetime: Long = 0,
            @Field("lecturer_name")
            val lecturerName: String = "",
            @Field("type_name")
            val typeName: String = ""
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

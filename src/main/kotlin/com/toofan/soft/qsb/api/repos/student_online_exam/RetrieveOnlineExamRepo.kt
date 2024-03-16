package com.toofan.soft.qsb.api.repos.student_online_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import kotlinx.coroutines.runBlocking

object RetrieveOnlineExamRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke { id ->
            request = Request(id)
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
            id: Int
        )
    }

    data class Request(
        @Field("id")
        private val _id: Int
    ) : IRequest

    data class Response(
        @Field("is_success")
        val isSuccess: Boolean = false,
        @Field("error_message")
        val errorMessage: String? = null,
        @Field("data")
        val data: Data? = null
    ) : IResponse {

        data class Data(
            @Field("college_name")
            val collegeName: String,
            @Field("department_name")
            val departmentName: String,
            @Field("level_name")
            val levelName: String,
            @Field("semester_name")
            val semesterName: String,
            @Field("course_name")
            val courseName: String,
            @Field("course_part_name")
            val coursePartName: String,
            @Field("type_name")
            val typeName: String,
            @Field("datetime")
            val datetime: Long,
            @Field("duration")
            val duration: Int,
            @Field("score")
            val score: Float,
            @Field("lecturer_name")
            val lecturerName: String,
            @Field("language_name")
            val languageName: String,
            @Field("is_mandatory_question_sequence")
            val isMandatoryQuestionSequence: Boolean,
            @Field("general_note")
            val generalNote: String,
            @Field("special_note")
            val specialNote: String? = null
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

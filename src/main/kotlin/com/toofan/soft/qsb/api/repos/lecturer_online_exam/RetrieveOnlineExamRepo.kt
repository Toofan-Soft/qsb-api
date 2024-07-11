package com.toofan.soft.qsb.api.repos.lecturer_online_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.extensions.string
import java.time.LocalDateTime

object RetrieveOnlineExamRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Response.Data>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { id ->
                request = Request(id)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.LecturerOnlineExam.Retrieve,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Response.Data>)
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
            val collegeName: String = "",
            @Field("department_name")
            val departmentName: String = "",
            @Field("level_name")
            val levelName: String = "",
            @Field("semester_name")
            val semesterName: String = "",
            @Field("course_name")
            val courseName: String = "",
            @Field("course_part_name")
            val coursePartName: String = "",
            @Field("conduct_method_name")
            val conductMethodName: String = "",
            @Field("type_name")
            val typeName: String = "",
            @Field("datetime")
            private val _datetime: LocalDateTime = LocalDateTime.now(),
            @Field("duration")
            val duration: Int = 0,
            @Field("datetime_notification_datetime")
            private val _datetimeNotificationDatetime: LocalDateTime = LocalDateTime.now(),
            @Field("result_notification_datetime")
            private val _resultNotificationDatetime: LocalDateTime = LocalDateTime.now(),
            @Field("language_name")
            val languageName: String = "",
            @Field("lecturer_name")
            val lecturerName: String = "",
            @Field("difficulty_level_name")
            val difficultyLevelName: String = "",
            @Field("status_name")
            val statusName: String = "",
            @Field("forms_count")
            val formsCount: Int = 0,
            @Field("form_configuration_method_name")
            val formConfigurationMethodName: String = "",
            @Field("form_name_method_name")
            val formNameMethodName: String = "",
            @Field("questions_types")
            val questionsTypes: List<Data> = emptyList(),
            @Field("proctor_name")
            val proctorName: String? = null,
            @Field("special_note")
            val specialNote: String? = null,

            @Field("is_suspended")
            val isSuspended: Boolean? = false,
            @Field("is_complete")
            val isComplete: Boolean = false,

            @Field("is_editable")
            val isEditable: Boolean = false,
            @Field("is_deletable")
            val isDeletable: Boolean = false
        ) : IResponse {
            val datetime get() = _datetime.string
            val datetimeNotificationDatetime get() = _datetimeNotificationDatetime.string
            val resultNotificationDatetime get() = _resultNotificationDatetime.string

            data class Data(
                @Field("type_name")
                val typeName: String = "",
                @Field("questions_count")
                val questionsCount: Int = 0,
                @Field("question_score")
                val questionScore: Float = 0.0f
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

package com.toofan.soft.qsb.api.repos.lecturer_online_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import java.time.LocalDateTime

object AddOnlineExamRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<Response.Data>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke(
                { departmentCoursePartId, conductMethodId, typeId, datetime, duration,
                  datetimeNotificationDatetime, resultNotificationDatetime, languageId, difficultyLevelId,
                  formsCount, formConfigurationMethodId, formNameMethodId, questionsTypes, topicsIds ->
                    val _questionsTypes: ArrayList<Request.Data> = arrayListOf()

                    questionsTypes.invoke { typeId, questionsCount, questionScore ->
                        _questionsTypes.add(Request.Data(typeId, questionsCount, questionScore))
                    }

                    request = Request(
                        departmentCoursePartId, conductMethodId, typeId, datetime, duration,
                        datetimeNotificationDatetime, resultNotificationDatetime, languageId, difficultyLevelId,
                        formsCount, formConfigurationMethodId, formNameMethodId, _questionsTypes, topicsIds
                    )
                },
                { request!!.optional(it) }
            )

            request?.let {
                ApiExecutor.execute(
                    route = Route.LecturerOnlineExam.Add,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Response.Data>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            departmentCoursePartId: Int,
            conductMethodId: Int,
            typeId: Int,
            datetime: LocalDateTime,
            duration: Int,
            datetimeNotificationDatetime: LocalDateTime,
            resultNotificationDatetime: LocalDateTime,
            languageId: Int,
            difficultyLevelId: Int,

            formsCount: Int,
            formConfigurationMethodId: Int,
            formNameMethodId: Int,

            questionsTypes: (Data) -> Unit,

            topicsIds: List<Int>
        )

        fun interface Data {
            operator fun invoke(
                typeId: Int,
                questionsCount: Int,
                questionScore: Float
            )
        }
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("department_course_part_id")
        private val _departmentCoursePartId: Int,
        @Field("conduct_method_id")
        private val _conductMethodId: Int,
        @Field("type_id")
        private val _typeId: Int,
        @Field("datetime")
        private val _datetime: LocalDateTime,
        @Field("duration")
        private val _duration: Int,
        @Field("datetime_notification_datetime")
        private val _datetimeNotificationDatetime: LocalDateTime,
        @Field("result_notification_datetime")
        private val _resultNotificationDatetime: LocalDateTime,
        @Field("language_id")
        private val _languageId: Int,
        @Field("difficulty_level_id")
        private val _difficultyLevelId: Int,

        @Field("forms_count")
        private val _formsCount: Int,
        @Field("form_configuration_method_id")
        private val _formConfigurationMethodId: Int,
        @Field("form_name_method_id")
        private val _formNameMethodId: Int,
        @Field("questions_types")
        private val _questionsTypes: List<Data>,
        @Field("topics_ids")
        private val _topicsIds: List<Int>,

        @Field("proctor_id")
        private val _proctorId: OptionalVariable<Int> = OptionalVariable(),
        @Field("special_note")
        private val _specialNote: OptionalVariable<String> = OptionalVariable()
    ) : IRequest {
        val proctorId = loggableProperty(_proctorId)
        val specialNote = loggableProperty(_specialNote)

        data class Data(
            @Field("type_id")
            private val _typeId: Int,
            @Field("questions_count")
            private val _questionsCount: Int,
            @Field("question_score")
            private val _questionScore: Float,
        ) : IRequest

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
        val data: Data? = null
    ) : IResponse {

        data class Data(
            @Field("id")
            val id: Int = 0
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

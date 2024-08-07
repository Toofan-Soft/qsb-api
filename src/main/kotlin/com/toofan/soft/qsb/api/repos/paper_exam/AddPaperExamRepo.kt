package com.toofan.soft.qsb.api.repos.paper_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import java.time.LocalDateTime

object AddPaperExamRepo {
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
                { departmentCoursePartId, typeId, datetime, duration,
                  languageId, difficultyLevelId, lecturerName,
                  formsCount, questionsTypes, topicsIds ->
                    val _questionsTypes: ArrayList<Request.Data> = arrayListOf()

                    questionsTypes.invoke { typeId, questionsCount, questionScore ->
                        _questionsTypes.add(Request.Data(typeId, questionsCount, questionScore))
                    }

                    request = Request(
                        departmentCoursePartId, typeId, datetime, duration,
                        languageId, difficultyLevelId, lecturerName,
                        formsCount, _questionsTypes, topicsIds
                    )
                },
                { request!!.optional(it) }
            )

            request?.let {
                val hasError = (it._formsCount == 1 &&
                        listOf(
                            it._formConfigurationMethodId.value,
                            it._formNameMethodId.value
                        ).any { it != null }) || (it._formsCount > 1 &&
                        listOf(
                            it._formConfigurationMethodId.value,
                            it._formNameMethodId.value
                        ).any { it == null })

                if (!hasError) {
                    ApiExecutor.execute(
                        route = Route.PaperExam.Add,
                        request = it
                    ) {
                        onComplete(Response.map(it).getResource() as Resource<Response.Data>)
                    }
                } else {
                    onComplete(Resource.Error("Invalid Inputs!"))
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            departmentCoursePartId: Int,
            typeId: Int,
            datetime: LocalDateTime,
            duration: Int,
            languageId: Int,
            difficultyLevelId: Int,
            lecturerName: String,

            formsCount: Int,

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
        @Field("type_id")
        private val _typeId: Int,
        @Field("datetime")
        private val _datetime: LocalDateTime,
        @Field("duration")
        private val _duration: Int,
        @Field("language_id")
        private val _languageId: Int,
        @Field("difficulty_level_id")
        private val _difficultyLevelId: Int,
        @Field("lecturer_name")
        private val _lecturerName: String,

        @Field("forms_count")
        internal val _formsCount: Int,
        @Field("questions_types")
        private val _questionsTypes: List<Data>,
        @Field("topics_ids")
        private val _topicsIds: List<Int>,

        @Field("form_configuration_method_id")
        internal val _formConfigurationMethodId: OptionalVariable<Int> = OptionalVariable(),
        @Field("form_name_method_id")
        internal val _formNameMethodId: OptionalVariable<Int> = OptionalVariable(),

        @Field("special_note")
        private val _specialNote: OptionalVariable<String> = OptionalVariable()
    ) : IRequest {
        val formConfigurationMethodId = loggableProperty(_formConfigurationMethodId)
        val formNameMethodId = loggableProperty(_formNameMethodId)
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

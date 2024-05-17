package com.toofan.soft.qsb.api.repos.paper_exam

import com.toofan.soft.qsb.api.*

object ExportPaperExamToPdfRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke(
                { id ->
                    request = Request(id)
                },
                { request!!.optional(it) }
            )

            request?.let {
                ApiExecutor.execute(
                    route = Route.PaperExam.Export
                ) {
//                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            id: Int
        )
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("id")
        private val _id: Int,
        @Field("with_mirror")
        private val _withMirror: OptionalVariable<Boolean> = OptionalVariable(),
        @Field("with_answer_mirror")
        private val _withAnswerMirror: OptionalVariable<Boolean> = OptionalVariable()
    ) : IRequest {
        val withMirror = loggableProperty(_withMirror)
        val withAnswerMirror = loggableProperty(_withAnswerMirror)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }

//    data class Response(
//        @Field("is_success")
//        val isSuccess: Boolean = false,
//        @Field("error_message")
//        val errorMessage: String? = null,
//        @Field("data")
//        val data: Data? = null
//    ) : IResponse {
//
//        data class Data(
//            @Field("college_name")
//            val collegeName: String,
//            @Field("department_name")
//            val departmentName: String,
//            @Field("level_name")
//            val levelName: String,
//            @Field("semester_name")
//            val semesterName: String,
//            @Field("course_name")
//            val courseName: String,
//            @Field("course_part_name")
//            val coursePartName: String,
//            @Field("type_id")
//            val typeId: Int,
//            @Field("datetime")
//            val datetime: Long,
//            @Field("duration")
//            val duration: Int,
//            @Field("language_name")
//            val languageName: String,
//            @Field("lecturer_name")
//            val lecturerName: String,
//            @Field("difficulty_level_name")
//            val difficultyLevelName: String,
//            @Field("form_count")
//            val formCount: Int,
//            @Field("form_configuration_method_name")
//            val formConfigurationMethodName: String,
//            @Field("form_name_method_id")
//            val formNameMethodId: Int,
//            @Field("questions_types")
//            val questionsTypes: List<Data>,
//            @Field("special_note")
//            val specialNote: String? = null
//        ) : IResponse {
//            data class Data(
//                @Field("type_name")
//                val typeName: String,
//                @Field("questions_count")
//                val questionsCount: Int,
//                @Field("question_score")
//                val questionScore: Float,
//            ) : IResponse
//        }
//
//        companion object {
//            private fun getInstance(): Response {
//                return Response()
//            }
//
//            fun map(data: JsonObject): Response {
//                return getInstance().getResponse(data) as Response
//            }
//        }
//    }

    data class Result(
        val forms: List<Data>
    ) {
        data class Data(
            val questionsSheet: ByteArray,
            val mirrorSheet: ByteArray? = null,
            val answerMirrorSheet: ByteArray? = null
        )
    }
}

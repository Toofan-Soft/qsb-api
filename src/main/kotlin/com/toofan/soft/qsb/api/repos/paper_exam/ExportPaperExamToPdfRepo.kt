package com.toofan.soft.qsb.api.repos.paper_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.helper.QuestionHelper
//import com.toofan.soft.qsb.api.services.pdf.Data
//import com.toofan.soft.qsb.api.services.pdf.PdfGenerator

object ExportPaperExamToPdfRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<Result.Data>) -> Unit
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
                    route = Route.PaperExam.Export,
                    request = it
                ) {
                    when (val resource = Response.map(it).getResource() as Resource<Response.Data>) {
                        is Resource.Success -> {
                            if (resource.data != null) {
//                                PdfGenerator.build(
//                                    Data(
//                                        resource.data.universityName,
//                                        resource.data.universityLogoUrl,
//                                        resource.data.collegeName,
//                                        resource.data.departmentName,
//                                        resource.data.levelName,
//                                        resource.data.semesterName,
//                                        resource.data.courseName,
//                                        resource.data.coursePartName,
//                                        resource.data.typeName,
//                                        resource.data.datetime.toString(),
//                                        resource.data.duration,
//                                        resource.data.lecturerName,
//                                        resource.data.score,
//                                        resource.data.languageId,
//                                        resource.data.notes,
//                                        resource.data.forms.map {
//                                            Data.Form(
//                                                name = it.name,
//                                                trueFalseQuestions = it.questions.filter {
//                                                    it.getChoices().all { it.id == 0 }
//                                                }.map {
//                                                    Data.Form.TrueFalse(
//                                                        it.content,
//                                                        it.attachmentUrl,
//                                                        it.isTrue
//                                                    )
//                                                },
//                                                multiChoicesQuestions = it.questions.filter {
//                                                    it.getChoices().all { it.id != 0 }
//                                                }.map {
//                                                    Data.Form.MultiChoices(
//                                                        it.content,
//                                                        it.attachmentUrl,
//                                                        it.getChoices().map {
//                                                            Data.Form.MultiChoices.Choice(
//                                                                it.content,
//                                                                it.attachmentUrl,
//                                                                it.isTrue
//                                                            )
//                                                        }
//                                                    )
//                                                }
//                                            )
//                                        }
//                                    ),
//                                    true,
//                                    request?._withMirror?.value ?: false,
//                                    request?._withAnswerMirror?.value ?: false
//                                ) { papers, mirrors, answerMirrors ->
//                                    onComplete(
//                                        Resource.Success(
//                                            Result.Data(
//                                                papers = (papers ?: emptyList()).map {
//                                                    Result.Data.Data(
//                                                        it.form,
//                                                        it.bytes
//                                                    )
//                                                },
//                                                mirrors = (mirrors ?: emptyList()).map {
//                                                    Result.Data.Data(
//                                                        it.form,
//                                                        it.bytes
//                                                    )
//                                                },
//                                                answerMirrors = (answerMirrors ?: emptyList()).map {
//                                                    Result.Data.Data(
//                                                        it.form,
//                                                        it.bytes
//                                                    )
//                                                }
//                                            )
//                                        )
//                                    )
//                                }
                            } else {
                                onComplete(
                                    Resource.Error("Error!")
                                )
                            }
                        }
                        is Resource.Error -> {
                            onComplete(
                                resource as Resource<Result.Data>
                            )
                        }
                    }
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
        internal val _withMirror: OptionalVariable<Boolean> = OptionalVariable(),
        @Field("with_answer_mirror")
        internal val _withAnswerMirror: OptionalVariable<Boolean> = OptionalVariable()
    ) : IRequest {
        val withMirror = loggableProperty(_withMirror)
        val withAnswerMirror = loggableProperty(_withAnswerMirror)

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
            @Field("university_name")
            val universityName: String = "",
            @Field("university_logo_url")
            val universityLogoUrl: String = "",
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
            @Field("type_name")
            val typeName: String = "",
            @Field("datetime")
            val datetime: Long = 0,
            @Field("duration")
            val duration: Int = 0,
            @Field("language_id")
            val languageId: Int = 0,
            @Field("lecturer_name")
            val lecturerName: String = "",
            @Field("score")
            val score: Int = 0,
            @Field("notes")
            val notes: String = "",
            @Field("forms")
            val forms: List<Data> = emptyList()
        ) : IResponse {
            data class Data(
                @Field("name")
                val name: String = "",
                @Field("questions")
                val questions: List<Data> = emptyList()
            ) : IResponse {
                data class Data(
                    @Field("content")
                    val content: String = "",
                    @Field("attachment_url")
                    val attachmentUrl: String? = null,
                    @Field("is_true")
                    internal val isTrue: Boolean? = null,
                    @Field("choices")
                    private val choices: List<Data>? = null
                ) : IResponse {
                    data class Data(
                        @Field("content")
                        val content: String = "",
                        @Field("is_true")
                        val isTrue: Boolean = false,
                        @Field("attachment_url")
                        val attachmentUrl: String? = null
                    ) : IResponse

                    fun getChoices(): List<QuestionHelper.Data.Data> {
                        return if (isTrue != null && choices == null) {
                            listOf(
                                QuestionHelper.Data.Data.Type.CORRECT.toData().copy(isTrue = isTrue),
                                QuestionHelper.Data.Data.Type.INCORRECT.toData().copy(isTrue = !isTrue)
                            )
                        } else if (isTrue == null && choices != null) {
                            choices.map {
                                QuestionHelper.Data.Data(
                                    id = 0,
                                    content = it.content,
                                    isTrue = it.isTrue,
                                    attachmentUrl = it.attachmentUrl
                                )
                            }
                        } else {
                            emptyList()
                        }
                    }
                }
            }
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

//    data class Result(
//        val data: Data
//    ) {
    interface Result {
        data class Data(
            val papers: List<Data> = emptyList(),
            val mirrors: List<Data> = emptyList(),
            val answerMirrors: List<Data> = emptyList()
        ) {
            data class Data(
                val name: String,
                val bytes: ByteArray
            )
        }
    }
}

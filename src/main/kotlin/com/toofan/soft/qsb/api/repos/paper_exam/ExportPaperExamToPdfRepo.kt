package com.toofan.soft.qsb.api.repos.paper_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.helper.QuestionHelper
import com.toofan.soft.qsb.api.repos.user.LoginRepo
import com.toofan.soft.qsb.api.services.pdf.Data
import com.toofan.soft.qsb.api.services.pdf.PdfGenerator
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

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
                                println("**************************")
                                println(resource.data)
                                println("**************************")
//                                return@execute
                                PdfGenerator.build(
                                    Data(
                                        resource.data.universityName,
                                        resource.data.universityLogoUrl,
                                        resource.data.collegeName,
                                        resource.data.departmentName,
                                        resource.data.levelName,
                                        resource.data.semesterName,
                                        resource.data.courseName,
                                        resource.data.coursePartName,
                                        resource.data.typeName,
                                        resource.data.datetime.toString(),
                                        resource.data.duration,
                                        resource.data.lecturerName,
                                        resource.data.score,
                                        resource.data.languageId,
                                        resource.data.notes,
                                        if (resource.data.forms != null) {
                                            resource.data.forms.map {
                                                Data.Form(
                                                    name = it.name,
                                                    trueFalseQuestions = it.questions.filter {
                                                        it.getChoices().all { it.id == 0 }
                                                    }.map {
                                                        Data.Form.TrueFalse(
                                                            it.content,
                                                            it.attachmentUrl,
                                                            it.isTrue
                                                        )
                                                    },
                                                    multiChoicesQuestions = it.questions.filter {
                                                        it.getChoices().all { it.id != 0 }
                                                    }.map {
                                                        Data.Form.MultiChoices(
                                                            it.content,
                                                            it.attachmentUrl,
                                                            it.getChoices().map {
                                                                Data.Form.MultiChoices.Choice(
                                                                    it.content,
                                                                    it.attachmentUrl,
                                                                    it.isTrue
                                                                )
                                                            }
                                                        )
                                                    }
                                                )
                                            }
                                        } else emptyList(),
                                        if (resource.data.questions != null) {
                                            resource.data.questions.filter {
                                                it.getChoices().all { it.id == 0 }
                                            }.map {
                                                Data.Form.TrueFalse(
                                                    it.content,
                                                    it.attachmentUrl,
                                                    it.isTrue
                                                )
                                            }
                                        } else emptyList(),
                                        if (resource.data.questions != null) {
                                            resource.data.questions.filter {
                                                it.getChoices().all { it.id != 0 }
                                            }.map {
                                                Data.Form.MultiChoices(
                                                    it.content,
                                                    it.attachmentUrl,
                                                    it.getChoices().map {
                                                        Data.Form.MultiChoices.Choice(
                                                            it.content,
                                                            it.attachmentUrl,
                                                            it.isTrue
                                                        )
                                                    }
                                                )
                                            }
                                        } else emptyList()
                                    ),
                                    true,
                                    request?._withMirror?.value ?: false,
                                    request?._withAnswerMirror?.value ?: false
                                ) { papers, mirrors, answerMirrors ->
                                    onComplete(
                                        Resource.Success(
                                            Result.Data(
                                                papers = (papers ?: emptyList()).map {
                                                    Result.Data.Data(
                                                        it.form,
                                                        it.bytes
                                                    )
                                                },
                                                mirrors = (mirrors ?: emptyList()).map {
                                                    Result.Data.Data(
                                                        it.form,
                                                        it.bytes
                                                    )
                                                },
                                                answerMirrors = (answerMirrors ?: emptyList()).map {
                                                    Result.Data.Data(
                                                        it.form,
                                                        it.bytes
                                                    )
                                                }
                                            )
                                        )
                                    )
                                }
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
//        @Field("with_mirror")
        internal val _withMirror: OptionalVariable<Boolean> = OptionalVariable(),
//        @Field("with_answer_mirror")
        @Field("with_answer")
        internal val _withAnswerMirror: OptionalVariable<Boolean> = OptionalVariable()
    ) : IRequest {
        val withMirror = loggableProperty(_withMirror)
        val withAnswerMirror = loggableProperty(_withAnswerMirror)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }

    internal data class Response(
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
            val datetime: LocalDateTime = LocalDateTime.now(),
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
            val forms: List<Data>? = null,
            @Field("questions")
            val questions: List<Data.Data>? = null
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
                    private val choices: Data? = null
                ) : IResponse {
//                    data class Data(
//                        @Field("content")
//                        val content: String = "",
//                        @Field("is_true")
//                        val isTrue: Boolean = false,
//                        @Field("attachment_url")
//                        val attachmentUrl: String? = null,
//                        @Field("mix")
//                        val mix: List<Data>? = null
//                    ) : IResponse

                    data class Data(
                        @Field("unmixed")
                        val unmixed: List<Data.Data>? = null,
                        @Field("mixed")
                        val mixed: Data? = null
                    ) : IResponse {
                        data class Data(
                            @Field("is_true")
                            val isTrue: Boolean? = null,
                            @Field("choices")
                            val choices: List<Data> = emptyList()
                        ) : IResponse {
                            data class Data(
                                @Field("content")
                                val content: String = "",
                                @Field("attachment_url")
                                val attachmentUrl: String? = null,
                                @Field("is_true")
                                val isTrue: Boolean? = null
                            ) : IResponse
                        }
                    }

                    fun getChoices(): List<QuestionHelper.Data.Data> {
                        return if (isTrue != null && choices == null) {
                            listOf(
                                QuestionHelper.Data.Data.Type.CORRECT.toData().copy(isTrue = isTrue),
                                QuestionHelper.Data.Data.Type.INCORRECT.toData().copy(isTrue = !isTrue)
                            )
                        } else if (isTrue == null && choices != null) {
                            ArrayList<QuestionHelper.Data.Data>().apply {
                                if (choices.mixed != null) {
                                    choices.mixed.choices.map {
                                        QuestionHelper.Data.Data(
                                            id = 0,
                                            content = it.content,
                                            isTrue = it.isTrue,
                                            attachmentUrl = it.attachmentUrl
                                        )
                                    }.also {
                                        addAll(it)
                                        add(
                                            QuestionHelper.Data.Data.MIX_CHOICE.copy(
                                                isTrue = choices.mixed.isTrue
                                            )
                                        )
                                    }
                                }

                                if (choices.unmixed != null) {
                                    choices.unmixed.map {
                                        QuestionHelper.Data.Data(
                                            id = 0,
                                            content = it.content,
                                            isTrue = it.isTrue,
                                            attachmentUrl = it.attachmentUrl
                                        )
                                    }.also {
                                        addAll(it)
                                    }
                                }
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


    interface Result {
        data class Data(
            val papers: List<Data> = emptyList(),
            val mirrors: List<Data> = emptyList(),
            val answerMirrors: List<Data> = emptyList()
        ) {
            data class Data(
                val name: String?,
                val bytes: ByteArray
            )
        }
    }
}


fun main() {
    val thread1 = Thread {
        // First process
        for (i in 1..10) {
            println("Thread 1 - Count: $i")
            Thread.sleep(2000) // Simulating work with sleep
        }
    }
    val thread2 = Thread {
        runBlocking {
            Api.init("192.168.1.15")
            LoginRepo.execute(
                data = {
                    it.invoke("fadi@gmail.com", "fadi1234")
                },
                onComplete = {
                    println("complete")
                    runBlocking {
                        ExportPaperExamToPdfRepo.execute(
                            data = { mandatory, optional ->
                                mandatory.invoke(6)
                                optional.invoke {
                                    withAnswerMirror(true)
                                }
                            },
                            onComplete = {
                                when (it) {
                                    is Resource.Success -> {
                                        it.data?.let {
                                            println("**************** Listen ****************")
                                            println("papers: " + it.papers.size)
                                            println("mirrors: " + it.mirrors.size)
                                            println("answer mirrors: " + it.answerMirrors.size)
                                            println("****************************************")
                                        } ?: println("Error")
                                    }
                                    is Resource.Error -> {

                                    }
                                }
                            }
                        )
                    }
                }
            )
        }
    }

    thread1.start()
    thread2.start()

    thread1.join() // Wait for thread1 to finish
    thread2.join() // Wait for thread2 to finish

    println("Both threads have finished")
}

package com.toofan.soft.qsb.api.repos.practice_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.helper.QuestionHelper

object RetrievePracticeExamQuestionsRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { examId ->
                request = Request(examId)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.PracticeOnlineExam.RetrieveQuestionList,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<List<Response.Data>>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            examId: Int
        )
    }

    data class Request(
        @Field("exam_id")
        private val _examId: Int
    ) : IRequest

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
            @Field("content")
            val content: String = "",
            @Field("attachment_url")
            val attachmentUrl: String? = null,

            @Field("user_answer")
            private val userAnswer: Boolean? = null,
            @Field("is_true")
            private val isTrue: Boolean? = null,
            @Field("choices")
            private val choices: List<Data>? = null
        ) : IResponse {
            data class Data(
                @Field("id")
                val id: Int = 0,
                @Field("content")
                val content: String = "",
                @Field("attachment_url")
                val attachmentUrl: String? = null,
                @Field("is_selected")
                val isSelected: Boolean? = null,
                @Field("is_true")
                val isTrue: Boolean? = null
            ) : IResponse

            fun getChoices(): List<QuestionHelper.Data.Data> {
                return if (isTrue == null && userAnswer == null) {
                    if (choices == null) {
                        QuestionHelper.Data.Data.Type.values().map { it.toData() }
                    } else {
                        if (choices.all { it.isTrue == null && it.isSelected == null }) {
                            choices.map {
                                QuestionHelper.Data.Data(
                                    id = it.id,
                                    content = it.content,
                                    attachmentUrl = it.attachmentUrl
                                )
                            }
                        } else if (choices.all { it.isTrue != null && it.isSelected != null }) {
                            choices.map {
                                QuestionHelper.Data.Data(
                                    id = it.id,
                                    content = it.content,
                                    isTrue = it.isTrue,
                                    isSelected = it.isSelected,
                                    attachmentUrl = it.attachmentUrl
                                )
                            }
                        } else {
                            emptyList()
                        }
                    }
                } else if (isTrue != null && userAnswer != null) {
                    if (choices == null) {
                        listOf(
                            QuestionHelper.Data.Data.Type.CORRECT.toData().copy(isTrue = isTrue, isSelected = userAnswer),
                            QuestionHelper.Data.Data.Type.INCORRECT.toData().copy(isTrue = !isTrue, isSelected = !userAnswer)
                        )
                    } else {
                        emptyList()
                    }
                } else {
                    emptyList()
                }
            }
        }

//        data class Data(
//            @Field("type_name")
//            val typeName: String = "",
//            @Field("questions")
//            val questions: List<Data> = emptyList()
//        ) : IResponse {
//            sealed interface Data {
//                data class TrueFalse(
//                    @Field("id")
//                    val id: Int = 0,
//                    @Field("content")
//                    val content: String = "",
//                    @Field("attachment_url")
//                    val attachmentUrl: String? = null,
//                    @Field("user_answer")
//                    val userAnswer: Boolean? = null,
//                    @Field("is_true")
//                    val isTrue: Boolean? = null
//                ) : Data
//
//                data class MultiChoice(
//                    @Field("id")
//                    val id: Int = 0,
//                    @Field("content")
//                    val content: String = "",
//                    @Field("attachment_url")
//                    val attachmentUrl: String? = null,
//                    @Field("choices")
//                    val choices: List<Data> = emptyList()
//                ) : Data {
//                    data class Data(
//                        @Field("id")
//                        val id: Int = 0,
//                        @Field("content")
//                        val content: String = "",
//                        @Field("attachment_url")
//                        val attachmentUrl: String? = null,
//                        @Field("is_selected")
//                        val isSelected: Boolean? = null,
//                        @Field("is_true")
//                        val isTrue: Boolean? = null
//                    )
//                }
//            }
//        }

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

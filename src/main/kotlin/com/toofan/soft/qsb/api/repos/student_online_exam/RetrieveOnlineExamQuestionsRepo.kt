package com.toofan.soft.qsb.api.repos.student_online_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.helper.QuestionHelper
import com.toofan.soft.qsb.api.repos.user.LoginRepo
import com.toofan.soft.qsb.api.services.TimerListener
import kotlinx.coroutines.runBlocking

object RetrieveOnlineExamQuestionsRepo {
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
                    route = Route.StudentOnlineExam.RetrieveQuestionList,
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

                @Field("choices")
                private val choices: Data? = null
            ) : IResponse {
                data class Data(
                    @Field("unmixed")
                    val unmixed: List<Data.Data>? = null,
                    @Field("mixed")
                    val mixed: Data? = null
                ) : IResponse {
                    data class Data(
                        @Field("id")
                        val id: Int = 0,
                        @Field("is_selected")
                        val isSelected: Boolean? = null,
                        @Field("choices")
                        val choices: List<Data> = emptyList()
                    ) : IResponse {
                        data class Data(
                            @Field("id")
                            val id: Int = 0,
                            @Field("content")
                            val content: String = "",
                            @Field("attachment_url")
                            val attachmentUrl: String? = null,
                            @Field("is_selected")
                            val isSelected: Boolean? = null
                        ) : IResponse
                    }
                }

                fun getChoices(): List<QuestionHelper.Data.Data> {
                    return if (userAnswer == null) {
                        if (choices == null) {
                            QuestionHelper.Data.Data.Type.values().map { it.toData() }
                        } else {
                            ArrayList<QuestionHelper.Data.Data>().apply {
                                if (choices.mixed != null) {
                                    choices.mixed.choices.map {
                                        QuestionHelper.Data.Data(
                                            id = it.id,
                                            content = it.content,
                                            isSelected = it.isSelected,
                                            attachmentUrl = it.attachmentUrl
                                        )
                                    }.also {
                                        addAll(it)
                                        add(
                                            QuestionHelper.Data.Data.MIX_CHOICE.copy(
                                                isSelected = choices.mixed.isSelected
                                            )
                                        )
                                    }
                                }

                                if (choices.unmixed != null) {
                                    choices.unmixed.map {
                                        QuestionHelper.Data.Data(
                                            id = it.id,
                                            content = it.content,
                                            isSelected = it.isSelected,
                                            attachmentUrl = it.attachmentUrl
                                        )
                                    }.also {
                                        addAll(it)
                                    }
                                }
                            }
                        }
                    } else {
                        if (choices == null) {
                            listOf(
                                QuestionHelper.Data.Data.Type.CORRECT.toData().copy(isSelected = userAnswer),
                                QuestionHelper.Data.Data.Type.INCORRECT.toData().copy(isSelected = !userAnswer)
                            )
                        } else {
                            emptyList()
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
}

fun main() {
    runBlocking {
        Api.init("192.168.1.15")
        LoginRepo.execute(
            data = {
                it.invoke("777300003@gmail.com", "s777300002s")
            },
            onComplete = {
                println("complete")
                runBlocking {
                    RetrieveOnlineExamRepo.execute(
                        data = {
                            it.invoke(2)
                        },
                        onComplete = {
                            when (it) {
                                is Resource.Success -> {
                                    it.data?.let {
                                        println("data: $it")
                                        it.setOnRemainingTimerListener(object : TimerListener {
                                            override fun onUpdate(value: Long) {
                                                println(value)
                                            }

                                            override fun onFinish() {
                                                TODO("Not yet implemented")
                                            }
                                        })
                                    }
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
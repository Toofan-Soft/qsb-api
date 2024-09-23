package com.toofan.soft.qsb.api.repos.question

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.helper.QuestionHelper
import com.toofan.soft.qsb.api.repos.user.LoginRepo
import kotlinx.coroutines.runBlocking

object RetrieveQuestionRepo {
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
                    route = Route.Question.Retrieve,
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
            @Field("difficulty_level_name")
            val difficultyLevelName: String = "",
            @Field("accessibility_status_name")
            val accessibilityStatusName: String = "",
            @Field("language_name")
            val languageName: String = "",
            @Field("estimated_answer_time")
            val estimatedAnswerTime: Int = 0,
            @Field("content")
            val content: String = "",
            @Field("attachment_url")
            val attachmentUrl: String? = null,
            @Field("title")
            val title: String? = null,
            @Field("status")
            val status: Status = Status(),
            @Field("is_true")
            private val isTrue: Boolean? = null,
            @Field("choices")
            private val choices: List<Data>? = null,

            @Field("is_deletable")
            val isDeletable: Boolean = false
        ) : IResponse {
            data class Status(
                @Field("is_requested")
                val isRequested: Boolean = false,
                @Field("is_accepted")
                val isAccepted: Boolean = false
            ) : IResponse

            data class Data(
                @Field("id")
                val id: Int = 0,
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
                            id = it.id,
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
    val thread1 = Thread {
        // First process
        for (i in 1..10) {
            println("Thread 1 - Count: $i")
            Thread.sleep(2000) // Simulating work with sleep
        }
    }

    val thread2 = Thread {
        runBlocking {
            Api.init("192.168.1.13")
            LoginRepo.execute(
                data = {
//                    it.invoke("fadi@gmail.com", "fadi1234")
                    it.invoke("fadiadmin@gmail.com", "fadiadmin")
                },
                onComplete = {
                    println("complete")
                    runBlocking {
                        RetrieveQuestionRepo.execute(
                            data = {
                                it.invoke(60)
                            },
                            onComplete = {
                                when (it) {
                                    is Resource.Success -> {
                                        it.data?.let {
                                            println("**************** Listen ****************")
                                            println(it.toString())
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

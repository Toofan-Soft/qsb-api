package com.toofan.soft.qsb.api.repos.proctor_online_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.extensions.string
import com.toofan.soft.qsb.api.repos.user.LoginRepo
import com.toofan.soft.qsb.api.services.ProctorPusherListener
import kotlinx.coroutines.runBlocking
import java.time.LocalTime

object RetrieveOnlineExamStudentsRepo {
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
                    route = Route.ProctorOnlineExam.RetrieveStudentList,
                    request = it
                ) {
                    when (val resource = Response.map(it).getResource() as Resource<List<Response.Data>>) {
                        is Resource.Success -> {
                            resource.data?.let { _data ->
                                onComplete(Resource.Success(_data))

                                var data = _data
                                ProctorPusherListener.addListener { new ->
                                    data = data.map {
                                        if (it.id != new.id) {
                                            it
                                        } else {
                                            it.copy(
                                                formName = new.formName,
                                                statusName = new.statusName,
                                                _startTime = new.startTime,
                                                _endTime = new.endTime,
                                                answeredQuestionsCount = new.answeredQuestionsCount,
                                                isSuspended = new.isSuspended
                                            )
                                        }
                                    }
                                    onComplete(Resource.Success(data))
                                }
                            }
                        }
                        is Resource.Error -> {}
                    }
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
            @Field("academic_id")
            val academicId: Int = 0,
            @Field("name")
            val name: String = "",
            @Field("gender_name")
            val genderName: String = "",
            @Field("image_url")
            val imageUrl: String? = null,

            @Field("form_name")
            val formName: String? = null,
            @Field("status_name")
            val statusName: String? = null,
            @Field("start_time")
            private val _startTime: LocalTime? = null,
            @Field("end_time")
            private val _endTime: LocalTime? = null,
            @Field("answered_questions_count")
            val answeredQuestionsCount: Int? = null,

            @Field("is_suspended")
            val isSuspended: Boolean? = null
        ) : IResponse {
            val startTime get() = _startTime?.string
            val endTime get() = _endTime?.string

            val isStarted get() = _startTime != null
            val isFinished get() = _endTime != null
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
            Api.init("192.168.1.15")
            LoginRepo.execute(
                data = {
//                    it.invoke("fadi@gmail.com", "fadi1234")
                    it.invoke("eyadadmin@gmail.com", "eyadadmin1")
                },
                onComplete = {
                    println("complete")
                    runBlocking {
                        RetrieveOnlineExamStudentsRepo.execute(
                            data = {
                                it.invoke(5)
                            },
                            onComplete = {
                                when (it) {
                                    is Resource.Success -> {
                                        it.data?.let {
                                            println("**************** Listen ****************")
                                            it.forEach {
                                                println(it.toString())
                                            }
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

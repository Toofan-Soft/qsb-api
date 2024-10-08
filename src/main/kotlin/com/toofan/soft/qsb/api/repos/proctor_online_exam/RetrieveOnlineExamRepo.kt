package com.toofan.soft.qsb.api.repos.proctor_online_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.extensions.string
import com.toofan.soft.qsb.api.repos.user.LoginRepo
import com.toofan.soft.qsb.api.services.Timer
import com.toofan.soft.qsb.api.services.TimerListener
import kotlinx.coroutines.runBlocking
import java.time.Duration
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
                    route = Route.ProctorOnlineExam.Retrieve,
                    request = it
                ) {
                    when (val resource = Response.map(it).getResource() as Resource<Response.Data>) {
                        is Resource.Success -> {
                            resource.data?.let {
                                if (it.isComplete) {
                                    Response.Data.stop()
                                }
                                if (it.isTakable) {
                                    it.run()
                                }
                                onComplete(Resource.Success(it))
                            }
                        }
                        is Resource.Error -> {
                            onComplete(Resource.Error(resource.message))
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
            @Field("type_name")
            val typeName: String = "",
            @Field("lecturer_name")
            val lecturerName: String = "",
            @Field("datetime")
            private val _datetime: LocalDateTime = LocalDateTime.now(),
            @Field("duration")
            val duration: Int = 0,
            @Field("score")
            val score: Float = 0.0f,
            @Field("general_note")
            val generalNote: String = "",
            @Field("special_note")
            val specialNote: String? = null,

            @Field("is_takable")
            val isTakable: Boolean = false,

            @Field("is_complete")
            val isComplete: Boolean = false
        ) : IResponse {
            val datetime get() = _datetime.string

            companion object {
                private var listener: TimerListener? = null
                private var isRun: Boolean = false

                internal fun stop() {
                    isRun = false
                }
            }

            internal fun run() {
                if (isRun) {
                    stop()
                    Thread {
                        Thread.sleep(3000)
                    }.apply {
                        start()
                        join()
                    }
                }
                isRun = true
                startTimer()
            }

            private fun startTimer() {
                val remainingTime = Duration.between(LocalDateTime.now(), _datetime.plusSeconds((duration * 60).toLong())).toMillis()

                if (isTakable && !isComplete && remainingTime > 0) {
                    Timer((remainingTime))
                        .schedule(
                            onUpdate = {
                                if (isRun) {
                                    listener?.onUpdate(it / 1000)
                                }
                                isRun
                            },
                            onFinish = {
                                listener?.onFinish()
                                stop()
                            }
                        )
                }
            }

            fun setOnRemainingTimerListener(listener: TimerListener) {
                Data.listener = listener
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
                it.invoke("fadi@gmail.com", "fadi1234")
            },
            onComplete = {
                println("complete")
                runBlocking {
                    RetrieveOnlineExamRepo.execute(
                        data = {
                            it.invoke(12)
                        },
                        onComplete = {
                            println("complete...")
                            println(it.data)

                            it.data!!.setOnRemainingTimerListener(object : TimerListener {
                                override fun onUpdate(value: Long) {
                                    println("...")
                                    println((value).toString())
                                }

                                override fun onFinish() {
                                    println("Finish.")
                                }

                            })
                        }
                    )
                }
            }
        )
    }
}

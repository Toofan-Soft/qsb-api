package com.toofan.soft.qsb.api.repos.practice_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.extensions.string
import com.toofan.soft.qsb.api.services.Timer
import com.toofan.soft.qsb.api.services.TimerListener
import java.time.LocalDateTime

object RetrievePracticeExamRepo {
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
                    route = Route.PracticeOnlineExam.Retrieve,
                    request = it
                ) {
//                    onComplete(Response.map(it).getResource() as Resource<Response.Data>)

                    when (val resource = Response.map(it).getResource() as Resource<Response.Data>) {
                        is Resource.Success -> {
                            resource.data?.let {
                                if (it.isComplete || it.isSuspended == true || !it.isStarted) {
                                    Response.Data.stop()
                                }
                                if (it.isStarted && it.isSuspended == false && !it.isComplete) {
                                    it.run()
                                }
                                onComplete(resource)
                            }
                        }
                        is Resource.Error -> {
                            onComplete(resource)
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
        internal val _id: Int
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
            @Field("datetime")
            private val _datetime: LocalDateTime = LocalDateTime.now(),
            @Field("duration")
            val duration: Int = 0,
            @Field("language_name")
            val languageName: String = "",
            @Field("is_mandatory_question_sequence")
            val isMandatoryQuestionSequence: Boolean = false,
            @Field("remaining_time")
            private val _remainingTime: Int = 0,
            @Field("title")
            val title: String? = null,

            @Field("is_started")
            val isStarted: Boolean = false,
            @Field("is_suspended")
            val isSuspended: Boolean? = null,
            @Field("is_complete")
            val isComplete: Boolean = false,

            @Field("is_deletable")
            val isDeletable: Boolean = false
        ) : IResponse {
            val datetime get() = _datetime.string

            companion object {
                private var listener: TimerListener? = null
                private var isRun: Boolean = false

                internal fun stop() {
                    isRun = false
                }
            }

//            internal fun run() {
//                isRun = true
//                startTimer()
//            }

            internal fun run() {
                if (isRun) {
                    stop()
                    Thread {
                        Thread.sleep(3000) // Simulating work with sleep
                    }.apply {
                        start()
                        join()
                    }
                }
                isRun = true
                startTimer()
            }

            private fun startTimer() {
                if (isStarted && isSuspended == false && !isComplete && _remainingTime > 0) {
                    Timer((_remainingTime * 1000).toLong())
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

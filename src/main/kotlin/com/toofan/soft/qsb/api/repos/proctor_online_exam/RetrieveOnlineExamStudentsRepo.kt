package com.toofan.soft.qsb.api.repos.proctor_online_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.extensions.string
import com.toofan.soft.qsb.api.test.ProctorPusherListener
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
//                    onComplete(Response.map(it).getResource() as Resource<List<Response.Data>>)

                    val resource = Response.map(it).getResource() as Resource<List<Response.Data>>
                    onComplete(resource)

                    when (resource) {
                        is Resource.Success -> {
                            resource.data?.let { data ->
                                ProctorPusherListener.addListener { new ->
                                    data.map {
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
                                    }.also {
                                        onComplete(Resource.Success(it))
                                    }
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

            val isSuspended: Boolean? = null
        ) : IResponse {
            val startTime get() = _startTime?.string
            val dateTime get() = _endTime?.string

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

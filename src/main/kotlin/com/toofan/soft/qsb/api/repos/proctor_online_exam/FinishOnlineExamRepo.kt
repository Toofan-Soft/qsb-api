package com.toofan.soft.qsb.api.repos.proctor_online_exam

import com.toofan.soft.qsb.api.*

object FinishOnlineExamRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { examId ->
                request = Request(examId)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.ProctorOnlineExam.Finish,
                    request = it
                ) {
//                    onComplete(Response.map(it).getResource() as Resource<Boolean>)

                    when (val resource = Response.map(it).getResource() as Resource<Boolean>) {
                        is Resource.Success -> {
                            RetrieveOnlineExamRepo.Response.Data.stop()
                            onComplete(resource)
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
            examId: Int
        )
    }

    data class Request(
        @Field("exam_id")
        private val _examId: Int
    ) : IRequest
}

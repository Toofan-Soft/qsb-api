package com.toofan.soft.qsb.api.repos.practice_exam

import com.toofan.soft.qsb.api.*

object SuspendPracticeExamRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { id ->
                request = Request(id)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.PracticeOnlineExam.Suspend,
                    request = it
                ) {
//                    onComplete(Response.map(it).getResource() as Resource<Boolean>)

                    when (val resource = Response.map(it).getResource() as Resource<Boolean>) {
                        is Resource.Success -> {
                            resource.data?.let {
                                RetrievePracticeExamRepo.Response.Data.stop()
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
        private val _id: Int
    ) : IRequest
}

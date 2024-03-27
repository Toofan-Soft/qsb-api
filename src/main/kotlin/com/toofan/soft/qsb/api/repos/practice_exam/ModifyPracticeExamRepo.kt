package com.toofan.soft.qsb.api.repos.practice_exam

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object ModifyPracticeExamRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke { id, title ->
            request = Request(id, title)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.PracticeOnlineExam.Modify,
                    request = it
                ) {
                    val response = Response.map(it)
                    onComplete(response)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            id: Int,
            title: String
        )
    }

    data class Request(
        @Field("id")
        private val _id: Int,
        @Field("title")
        private val _title: String
    ) : IRequest
}

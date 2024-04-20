package com.toofan.soft.qsb.api.repos.practice_exam

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object ModifyPracticeExamRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            var request: Request? = null

            data.invoke { id, title ->
                request = Request(id, title)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.PracticeOnlineExam.Modify,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
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

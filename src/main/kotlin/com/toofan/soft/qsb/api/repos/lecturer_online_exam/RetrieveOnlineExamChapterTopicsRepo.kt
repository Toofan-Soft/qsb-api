package com.toofan.soft.qsb.api.repos.lecturer_online_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object RetrieveOnlineExamChapterTopicsRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        var request: Request? = null

        data.invoke { examId, chapterId ->
            request = Request(examId, chapterId)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.LecturerOnlineExam.RetrieveChapterTopicList
                ) {
                    onComplete(Response.map(it).getResource() as Resource<List<Response.Data>>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            examId: Int,
            chapterId: Int
        )
    }

    data class Request(
        @Field("exam_id")
        private val _examId: Int,
        @Field("chapter_id")
        private val _chapterId: Int
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
            @Field("title")
            val title: String = ""
        )

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

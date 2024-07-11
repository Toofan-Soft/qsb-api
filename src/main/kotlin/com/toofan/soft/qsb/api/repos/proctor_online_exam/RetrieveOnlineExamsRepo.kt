package com.toofan.soft.qsb.api.repos.proctor_online_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.extensions.string
import java.time.LocalDateTime

object RetrieveOnlineExamsRepo {
    @JvmStatic
    suspend fun execute(
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        Coroutine.launch {
            ApiExecutor.execute(
                route = Route.ProctorOnlineExam.RetrieveList
            ) {
                onComplete(Response.map(it).getResource() as Resource<List<Response.Data>>)
            }
        }
    }

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
            @Field("course_name")
            val courseName: String = "",
            @Field("course_part_name")
            val coursePartName: String = "",
            @Field("datetime")
            private val _datetime: LocalDateTime = LocalDateTime.now()
        ) : IResponse {
            val datetime get() = _datetime.string
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

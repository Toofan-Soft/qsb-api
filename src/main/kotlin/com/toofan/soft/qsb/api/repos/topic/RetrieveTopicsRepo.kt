package com.toofan.soft.qsb.api.repos.topic

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object RetrieveTopicsRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            var request: Request? = null

            data.invoke { chapterId ->
                request = Request(chapterId)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.Topic.RetrieveList
                ) {
                    onComplete(Response.map(it).getResource() as Resource<List<Response.Data>>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            chapterId: Int
        )
    }

    data class Request(
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
            @Field("id")
            val id: Int = 0,
            @Field("arabic_title")
            val arabicTitle: String = "",
            @Field("english_title")
            val englishTitle: String = "",
            @Field("description")
            val description: String? = null,
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

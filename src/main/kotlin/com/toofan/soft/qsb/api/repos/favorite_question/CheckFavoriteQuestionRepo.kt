package com.toofan.soft.qsb.api.repos.favorite_question

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object CheckFavoriteQuestionRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<Response.Data>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            var request: Request? = null

            data.invoke(
                { questionId ->
                    request = Request(questionId)
                },
                { request!!.optional(it) }
            )

            request?.let {
                ApiExecutor.execute(
                    route = Route.FavoriteQuestion.Check,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Response.Data>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            questionId: Int
        )
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("question_id")
        private val _questionId: Int,
        @Field("combination_id")
        private val _combinationId: OptionalVariable<Int> = OptionalVariable()
    ) : IRequest {
        val combinationId = loggableProperty(_combinationId)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }

    data class Response(
        @Field("is_success")
        val isSuccess: Boolean = false,
        @Field("error_message")
        val errorMessage: String? = null,
        @Field("data")
        val data: Data? = null
    ) : IResponse {

        data class Data(
            @Field("is_favorite")
            val isFavorite: Boolean
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

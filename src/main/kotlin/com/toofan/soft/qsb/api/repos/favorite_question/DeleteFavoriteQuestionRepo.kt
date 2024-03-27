package com.toofan.soft.qsb.api.repos.favorite_question

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import com.toofan.soft.qsb.api.loggableProperty
import com.toofan.soft.qsb.api.repos.college.AddCollegeRepo
import kotlinx.coroutines.runBlocking

object DeleteFavoriteQuestionRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke(
            { questionId ->
                request = Request(questionId)
            },
            { request!!.optional(it) }
        )

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.FavoriteQuestion.Delete,
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
            questionId: Int
        )
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("question_id")
        private val _questionId: Int,
        @Field("combinationId")
        private val _combinationId: OptionalVariable<Int> = OptionalVariable()
    ) : IRequest {
        val combinationId = loggableProperty(_combinationId)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}

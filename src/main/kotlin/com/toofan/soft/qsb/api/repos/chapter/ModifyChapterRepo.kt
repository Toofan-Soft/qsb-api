package com.toofan.soft.qsb.api.repos.chapter

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ModifyChapterRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            var request: Request? = null

            data.invoke(
                { id ->
                    request = Request(id)
                },
                { request!!.optional(it) }
            )

            request?.let {
                ApiExecutor.execute(
                    route = Route.Chapter.Modify,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            id: Int
        )
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("id")
        private val _id: Int,
        @Field("arabic_title")
        private val _arabicTitle: OptionalVariable<String> = OptionalVariable(),
        @Field("english_title")
        private val _englishTitle: OptionalVariable<String> = OptionalVariable(),
        @Field("status_id")
        private val _statusId: OptionalVariable<Int> = OptionalVariable(),
        @Field("description")
        private val _description: OptionalVariable<String> = OptionalVariable()
    ) : IRequest {
        val arabicTitle = loggableProperty(_arabicTitle)
        val englishTitle = loggableProperty(_englishTitle)
        val statusId = loggableProperty(_statusId)
        val description = loggableProperty(_description)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}

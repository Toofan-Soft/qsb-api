package com.toofan.soft.qsb.api.repos.chapter

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object AddChapterRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        var request: Request? = null

        data.invoke(
            { coursePartId, arabicTitle, englishTitle ->
                request = Request(coursePartId, arabicTitle, englishTitle)
            },
            { request!!.optional(it) }
        )

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.Chapter.Add,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            coursePartId: Int,
            arabicTitle: String,
            englishTitle: String
        )
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("course_part_id")
        private val _coursePartId: Int,
        @Field("arabic_title")
        private val _arabicTitle: String,
        @Field("english_title")
        private val _englishTitle: String,
        @Field("description")
        private val _description: OptionalVariable<String> = OptionalVariable(),
    ) : IRequest {
        val description = loggableProperty(_description)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}

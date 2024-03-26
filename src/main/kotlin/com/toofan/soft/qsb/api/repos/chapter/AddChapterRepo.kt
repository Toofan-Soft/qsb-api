package com.toofan.soft.qsb.api.repos.chapter

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import com.toofan.soft.qsb.api.repos.department.AddDepartmentRepo
import kotlinx.coroutines.runBlocking

object AddChapterRepo {
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
                    val response = Response.map(it)
                    onComplete(response)
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
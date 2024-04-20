package com.toofan.soft.qsb.api.repos.course_part

import com.toofan.soft.qsb.api.*

object AddCoursePartRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke(
                { courseId, partId ->
                    request = Request(courseId, partId)
                },
                { request!!.optional(it) }
            )

            request?.let {
                ApiExecutor.execute(
                    route = Route.CoursePart.Add,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            courseId: Int,
            coursePartId: Int
        )
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("course_id")
        private val _courseId: Int,
        @Field("course_part_id")
        private val _coursePartId: Int,
        @Field("description")
        private val _description: OptionalVariable<String> = OptionalVariable(),
    ) : IRequest {
        val description = loggableProperty(_description)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}

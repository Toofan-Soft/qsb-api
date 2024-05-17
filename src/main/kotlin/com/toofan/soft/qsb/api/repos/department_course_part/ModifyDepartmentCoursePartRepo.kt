package com.toofan.soft.qsb.api.repos.department_course_part

import com.toofan.soft.qsb.api.*

object ModifyDepartmentCoursePartRepo {
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
                { id ->
                    request = Request(id)
                },
                { request!!.optional(it) }
            )

            request?.let {
                ApiExecutor.execute(
                    route = Route.DepartmentCoursePart.Modify,
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
        @Field("score")
        private val _score: OptionalVariable<Int> = OptionalVariable(),
        @Field("lectures_count")
        private val _lecturesCount: OptionalVariable<Int> = OptionalVariable(),
        @Field("lecture_duration")
        private val _lectureDuration: OptionalVariable<Int> = OptionalVariable(),
        @Field("note")
        private val _note: OptionalVariable<String> = OptionalVariable()
    ) : IRequest {
        val score = loggableProperty(_score)
        val lecturesCount = loggableProperty(_lecturesCount)
        val lectureDuration = loggableProperty(_lectureDuration)
        val note = loggableProperty(_note)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}

package com.toofan.soft.qsb.api.repos.department_course

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import com.toofan.soft.qsb.api.loggableProperty
import kotlinx.coroutines.runBlocking

object ModifyDepartmentCourseRepo {
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
            { id ->
                request = Request(id)
            },
            { request!!.optional(it) }
        )

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.DepartmentCourse.Modify,
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
            id: Int
        )
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("id")
        private val _id: Int,
        @Field("level_id")
        private val _levelId: OptionalVariable<Int> = OptionalVariable(),
        @Field("semester_id")
        private val _semesterId: OptionalVariable<Int> = OptionalVariable()
    ) : IRequest {
        val semesterId = loggableProperty(_semesterId)
        val levelId = loggableProperty(_levelId)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}

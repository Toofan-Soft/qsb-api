package com.toofan.soft.qsb.api.repos.department_course

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import kotlinx.coroutines.runBlocking

object AddDepartmentCourseRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke { departmentId, levelId, semesterId, courseId ->
            request = Request(departmentId, levelId, semesterId, courseId)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.DepartmentCourse.Add,
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
            departmentId: Int,
            levelId: Int,
            semesterId: Int,
            courseId: Int
        )
    }

    data class Request(
        @Field("department_id")
        private val _departmentId: Int,
        @Field("level_id")
        private val _levelId: Int,
        @Field("semester_id")
        private val _semesterId: Int,
        @Field("course_id")
        private val _courseId: Int
    ) : IRequest
}

package com.toofan.soft.qsb.api.repos.course_student

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import kotlinx.coroutines.runBlocking

object PassCourseStudentRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke { departmentCourseId, studentId ->
            request = Request(departmentCourseId, studentId)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.CourseStudent.Pass,
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
            departmentCourseId: Int,
            studentId: Int
        )
    }

    data class Request(
        @Field("department_course_id")
        private val _departmentCourseId: Int,
        @Field("student_id")
        private val _studentId: Int
    ) : IRequest
}

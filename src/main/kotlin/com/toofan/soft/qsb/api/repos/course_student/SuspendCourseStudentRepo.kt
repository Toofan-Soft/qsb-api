package com.toofan.soft.qsb.api.repos.course_student

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object SuspendCourseStudentRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        var request: Request? = null

        data.invoke { departmentCourseId, studentId ->
            request = Request(departmentCourseId, studentId)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.CourseStudent.Suspend,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
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

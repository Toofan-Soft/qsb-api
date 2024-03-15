package com.toofan.soft.qsb.api.repos.course_student

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import kotlinx.coroutines.runBlocking

object ModifyCourseStudentRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke { departmentCourseId, studentId, academicYear ->
            request = Request(departmentCourseId, studentId, academicYear)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.Question.Add,
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
            studentId: Int,
            academicYear: Int
        )
    }

    data class Request(
        @Field("department_course_id")
        private val _departmentCourseId: Int,
        @Field("student_id")
        private val _studentId: Int,
        @Field("academic_year")
        private val _academicYear: Int
    ) : IRequest
}

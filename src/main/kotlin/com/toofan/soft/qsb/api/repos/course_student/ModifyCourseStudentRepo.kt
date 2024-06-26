package com.toofan.soft.qsb.api.repos.course_student

import com.toofan.soft.qsb.api.*

object ModifyCourseStudentRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { departmentCourseId, studentId, academicYear ->
                request = Request(departmentCourseId, studentId, academicYear)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.CourseStudent.Modify,
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

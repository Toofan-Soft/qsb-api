package com.toofan.soft.qsb.api.repos.course_student

import com.toofan.soft.qsb.api.*

object AddCourseStudentsRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { departmentCourseId, studentsIds ->
                request = Request(departmentCourseId, studentsIds)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.CourseStudent.AddList,
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
            studentsIds: List<Int>
        )
    }

    data class Request(
        @Field("department_course_id")
        private val _departmentCourseId: Int,
        @Field("students_ids")
        private val _studentsIds: List<Int>
    ) : IRequest
}

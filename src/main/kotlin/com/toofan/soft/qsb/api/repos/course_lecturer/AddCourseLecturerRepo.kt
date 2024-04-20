package com.toofan.soft.qsb.api.repos.course_lecturer

import com.toofan.soft.qsb.api.*

object AddCourseLecturerRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { departmentCoursePartId, lecturerId ->
                request = Request(departmentCoursePartId, lecturerId)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.CourseLecture.Add,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            departmentCoursePartId: Int,
            lecturerId: Int
        )
    }

    data class Request(
        @Field("department_course_part_id")
        private val _departmentCoursePartId: Int,
        @Field("lecturer_id")
        private val _lecturerId: Int
    ) : IRequest
}

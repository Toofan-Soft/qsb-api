package com.toofan.soft.qsb.api.repos.course_lecturer

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import com.toofan.soft.qsb.api.repos.department.AddDepartmentRepo
import kotlinx.coroutines.runBlocking

object AddCourseLecturerRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke { departmentCoursePartId, lecturerId ->
            request = Request(departmentCoursePartId, lecturerId)
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

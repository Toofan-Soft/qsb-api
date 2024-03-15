package com.toofan.soft.qsb.api.repos.course_student

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import kotlinx.coroutines.runBlocking

object AddCourseStudentsRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke { departmentCourseId, studentsIds ->
            request = Request(departmentCourseId, studentsIds)
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

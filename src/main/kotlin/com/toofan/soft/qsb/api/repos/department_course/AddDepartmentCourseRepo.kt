package com.toofan.soft.qsb.api.repos.department_course

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object AddDepartmentCourseRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { departmentId, levelId, semesterId, courseId ->
                request = Request(departmentId, levelId, semesterId, courseId)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.DepartmentCourse.Add,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
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

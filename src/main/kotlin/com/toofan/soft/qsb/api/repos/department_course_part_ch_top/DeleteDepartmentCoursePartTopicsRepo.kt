package com.toofan.soft.qsb.api.repos.department_course_part_ch_top

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DeleteDepartmentCoursePartTopicsRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            var request: Request? = null

            data.invoke { departmentCoursePartId, topicsIds ->
                request = Request(departmentCoursePartId, topicsIds)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.DepartmentCoursePartChapterAndTopic.DeleteTopicList,
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
            topicsIds: List<Int>
        )
    }

    data class Request(
        @Field("department_course_part_id")
        private val _departmentCoursePartId: Int,
        @Field("topics_ids")
        private val _topicsIds: List<Int>
    ) : IRequest
}

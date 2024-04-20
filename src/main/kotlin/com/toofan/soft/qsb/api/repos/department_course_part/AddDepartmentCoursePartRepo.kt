package com.toofan.soft.qsb.api.repos.department_course_part

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.repos.question.AddQuestionRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object AddDepartmentCoursePartRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            var request: Request? = null

            data.invoke { departmentCourseId, coursePartId ->
                request = Request(departmentCourseId, coursePartId)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.DepartmentCoursePart.Add,
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
            coursePartId: Int
        )
    }

    data class Request(
        @Field("department_course_id")
        private val _departmentCourseId: Int,
        @Field("course_part_id")
        private val _coursePartId: Int,
        @Field("score")
        private val _score: OptionalVariable<Int> = OptionalVariable(),
        @Field("lectures_count")
        private val _lecturesCount: OptionalVariable<Int> = OptionalVariable(),
        @Field("lecture_duration")
        private val _lectureDuration: OptionalVariable<Int> = OptionalVariable(),
        @Field("note")
        private val _note: OptionalVariable<String> = OptionalVariable()
    ) : IRequest {
        val score = loggableProperty(_score)
        val lecturesCount = loggableProperty(_lecturesCount)
        val lectureDuration = loggableProperty(_lectureDuration)
        val note = loggableProperty(_note)

        fun optional(block: AddQuestionRepo.Request.() -> Unit): AddQuestionRepo.Request {
            return build(block)
        }
    }
}

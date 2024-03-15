package com.toofan.soft.qsb.api.repos.department_course_part

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import com.toofan.soft.qsb.api.repos.question.AddQuestionRepo
import kotlinx.coroutines.runBlocking

object AddDepartmentCoursePartRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke { departmentCourseId, coursePartId ->
            request = Request(departmentCourseId, coursePartId)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.Topic.Delete,
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
        @Field("lecture_count")
        private val _lectureCount: OptionalVariable<Int> = OptionalVariable(),
        @Field("lecture_duration")
        private val _lectureDuration: OptionalVariable<Int> = OptionalVariable(),
        @Field("note")
        private val _note: OptionalVariable<String> = OptionalVariable()
    ) : IRequest {
        val score = loggableProperty(_score)
        val lectureCount = loggableProperty(_lectureCount)
        val lectureDuration = loggableProperty(_lectureDuration)
        val note = loggableProperty(_note)

        fun optional(block: AddQuestionRepo.Request.() -> Unit): AddQuestionRepo.Request {
            return build(block)
        }
    }
}

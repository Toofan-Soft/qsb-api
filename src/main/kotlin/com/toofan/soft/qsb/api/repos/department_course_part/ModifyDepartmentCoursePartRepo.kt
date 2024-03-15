package com.toofan.soft.qsb.api.repos.department_course_part

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import com.toofan.soft.qsb.api.repos.question.AddQuestionRepo
import kotlinx.coroutines.runBlocking

object ModifyDepartmentCoursePartRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke { id ->
            request = Request(id)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.Topic.Modify,
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
            id: Int
        )
    }

    data class Request(
        @Field("id")
        private val _id: Int,
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

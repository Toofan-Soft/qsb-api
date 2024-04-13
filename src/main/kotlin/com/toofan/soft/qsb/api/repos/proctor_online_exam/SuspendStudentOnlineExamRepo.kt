package com.toofan.soft.qsb.api.repos.proctor_online_exam

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object SuspendStudentOnlineExamRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        var request: Request? = null

        data.invoke { examId, studentId ->
            request = Request(examId, studentId)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.ProctorOnlineExam.SuspendStudent,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            examId: Int,
            studentId: Int
        )
    }

    data class Request(
        @Field("exam_id")
        private val _examId: Int,
        @Field("student_id")
        private val _studentId: Int
    ) : IRequest
}

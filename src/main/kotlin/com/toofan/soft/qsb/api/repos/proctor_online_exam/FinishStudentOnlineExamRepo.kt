package com.toofan.soft.qsb.api.repos.proctor_online_exam

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import kotlinx.coroutines.runBlocking

object FinishStudentOnlineExamRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke { examId, studentId ->
            request = Request(examId, studentId)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.ProctorOnlineExam.FinishStudent,
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

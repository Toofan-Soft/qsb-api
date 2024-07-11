package com.toofan.soft.qsb.api.repos.lecturer_online_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import java.time.LocalDateTime

object RetrieveEditableOnlineExamRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Response.Data>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { id ->
                request = Request(id)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.LecturerOnlineExam.RetrieveEditable,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Response.Data>)
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
        private val _id: Int
    ) : IRequest

    data class Response(
        @Field("is_success")
        val isSuccess: Boolean = false,
        @Field("error_message")
        val errorMessage: String? = null,
        @Field("data")
        val data: Data? = null
    ) : IResponse {

        data class Data(
            @Field("conduct_method_id")
            val conductMethodId: Int = 0,
            @Field("type_id")
            val typeId: Int = 0,
            @Field("datetime")
            val datetime: LocalDateTime = LocalDateTime.now(),
            @Field("duration")
            val duration: Int = 0,
            @Field("datetime_notification_datetime")
            val datetimeNotificationDatetime: LocalDateTime = LocalDateTime.now(),
            @Field("result_notification_datetime")
            val resultNotificationDatetime: LocalDateTime = LocalDateTime.now(),
            @Field("form_name_method_id")
            val formNameMethodId: Int = 0,
            @Field("proctor_id")
            val proctorId: Int? = null,
            @Field("special_note")
            val specialNote: String? = null
        ) : IResponse

        companion object {
            private fun getInstance(): Response {
                return Response()
            }

            fun map(data: JsonObject): Response {
                return getInstance().getResponse(data) as Response
            }
        }
    }
}

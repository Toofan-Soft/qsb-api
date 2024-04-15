package com.toofan.soft.qsb.api.repos.lecturer_online_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object RetrieveEditableOnlineExamRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Response.Data>) -> Unit
    ) {
        var request: Request? = null

        data.invoke { id ->
            request = Request(id)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.LecturerOnlineExam.RetrieveEditable
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
            val datetime: Long = 0,
            @Field("duration")
            val duration: Int = 0,
            @Field("datetime_notification_datetime")
            val datetimeNotificationDatetime: Long = 0,
            @Field("result_notification_datetime")
            val resultNotificationDatetime: Long = 0,
            @Field("form_name_method_id")
            val formNameMethodId: Int = 0,
            @Field("proctor_id")
            val proctorId: Int? = null,
            @Field("special_note")
            val specialNote: String? = null
        )

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

package com.toofan.soft.qsb.api.repos.lecturer_online_exam

import com.toofan.soft.qsb.api.*
import java.time.LocalDateTime

object ModifyOnlineExamRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke(
                { id ->
                    request = Request(id)
                },
                { request!!.optional(it) }
            )

            request?.let {
                ApiExecutor.execute(
                    route = Route.LecturerOnlineExam.Modify,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            id: Int
        )
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("id")
        private val _id: Int,

        @Field("conduct_method_id")
        private val _conductMethodId: OptionalVariable<Int> = OptionalVariable(),
        @Field("type_id")
        private val _typeId: OptionalVariable<Int> = OptionalVariable(),
        @Field("datetime")
        private val _datetime: OptionalVariable<LocalDateTime> = OptionalVariable(),
        @Field("datetime_notification_datetime")
        private val _datetimeNotificationDatetime: OptionalVariable<LocalDateTime> = OptionalVariable(),
        @Field("result_notification_datetime")
        private val _resultNotificationDatetime: OptionalVariable<LocalDateTime> = OptionalVariable(),

        @Field("form_name_method_id")
        private val _formNameMethodId: OptionalVariable<Int> = OptionalVariable(),

        @Field("proctor_id")
        private val _proctorId: OptionalVariable<Int> = OptionalVariable(),
        @Field("special_note")
        private val _specialNote: OptionalVariable<String> = OptionalVariable()
    ) : IRequest {
        val conductMethodId = loggableProperty(_conductMethodId)
        val typeId = loggableProperty(_typeId)
        val datetime = loggableProperty(_datetime)
        val datetimeNotificationDatetime = loggableProperty(_datetimeNotificationDatetime)
        val resultNotificationDatetime = loggableProperty(_resultNotificationDatetime)

        val formNameMethodId = loggableProperty(_formNameMethodId)

        val proctorId = loggableProperty(_proctorId)
        val specialNote = loggableProperty(_specialNote)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}

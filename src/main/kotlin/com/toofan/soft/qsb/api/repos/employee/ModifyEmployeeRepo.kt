package com.toofan.soft.qsb.api.repos.employee

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import com.toofan.soft.qsb.api.loggableProperty
import kotlinx.coroutines.runBlocking

object ModifyEmployeeRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke(
            { id ->
                request = Request(id)
            },
            { request!!.optional(it) }
        )

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

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("id")
        private val _id: Int,

        @Field("arabic_name")
        private val _arabicName: OptionalVariable<String> = OptionalVariable(),
        @Field("english_name")
        private val _englishName: OptionalVariable<String> = OptionalVariable(),
        @Field("gender_id")
        private val _genderId: OptionalVariable<Int> = OptionalVariable(),
        @Field("qualification_id")
        private val _qualificationId: OptionalVariable<Int> = OptionalVariable(),
        @Field("job_type_id")
        private val _jobTypeId: OptionalVariable<Int> = OptionalVariable(),
        @Field("phone")
        private val _phone: OptionalVariable<Long> = OptionalVariable(),
        @Field("specialization")
        private val _specialization: OptionalVariable<String> = OptionalVariable(),
        @Field("image")
        private val _image: OptionalVariable<ByteArray> = OptionalVariable()
    ) : IRequest {
        val arabicName = loggableProperty(_arabicName)
        val englishName = loggableProperty(_englishName)
        val genderId = loggableProperty(_genderId)
        val qualificationId = loggableProperty(_qualificationId)
        val jobTypeId = loggableProperty(_jobTypeId)
        val phone = loggableProperty(_phone)
        val specialization = loggableProperty(_specialization)
        val image = loggableProperty(_image)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}
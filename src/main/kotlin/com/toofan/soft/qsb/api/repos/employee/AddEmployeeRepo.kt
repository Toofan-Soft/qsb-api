package com.toofan.soft.qsb.api.repos.employee

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object AddEmployeeRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            var request: Request? = null

            data.invoke(
                { arabicName, englishName, genderId, qualificationId, jobTypeId ->
                    request = Request(arabicName, englishName, genderId, qualificationId, jobTypeId)
                },
                { request!!.optional(it) }
            )

            request?.let {
                ApiExecutor.execute(
                    route = Route.Employee.Add,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            arabicName: String,
            englishName: String,
            genderId: Int,
            qualificationId: Int,
            jobTypeId: Int
        )
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("arabic_name")
        private val _arabicName: String,
        @Field("english_name")
        private val _englishName: String,
        @Field("gender_id")
        private val _genderId: Int,
        @Field("qualification_id")
        private val _qualificationId: Int,
        @Field("job_type_id")
        private val _jobTypeId: Int,
        @Field("email")
        private val _email: OptionalVariable<String> = OptionalVariable(),
        @Field("phone")
        private val _phone: OptionalVariable<Long> = OptionalVariable(),
        @Field("specialization")
        private val _specialization: OptionalVariable<String> = OptionalVariable(),
        @Field("image")
        private val _image: OptionalVariable<ByteArray> = OptionalVariable()
    ) : IRequest {
        val email = loggableProperty(_email)
        val phone = loggableProperty(_phone)
        val specialization = loggableProperty(_specialization)
        val image = loggableProperty(_image)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}

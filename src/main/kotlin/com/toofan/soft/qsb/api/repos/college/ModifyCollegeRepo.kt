package com.toofan.soft.qsb.api.repos.college

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import com.toofan.soft.qsb.api.loggableProperty
import kotlinx.coroutines.runBlocking

object ModifyCollegeRepo {
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
                    route = Route.College.Modify,
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
        @Field("phone")
        private val _phone: OptionalVariable<Long> = OptionalVariable(),
        @Field("email")
        private val _email: OptionalVariable<String> = OptionalVariable(),
        @Field("description")
        private val _description: OptionalVariable<String> = OptionalVariable(),
        @Field("youtube")
        private val _youtube: OptionalVariable<String> = OptionalVariable(),
        @Field("x_platform")
        private val _xPlatform: OptionalVariable<String> = OptionalVariable(),
        @Field("facebook")
        private val _facebook: OptionalVariable<String> = OptionalVariable(),
        @Field("telegram")
        private val _telegram: OptionalVariable<String> = OptionalVariable(),
        @Field("logo")
        private val _logo: OptionalVariable<ByteArray> = OptionalVariable()
    ) : IRequest {
        val arabicName = loggableProperty(_arabicName)
        val englishName = loggableProperty(_englishName)
        val phone = loggableProperty(_phone)
        val email = loggableProperty(_email)
        val description = loggableProperty(_description)
        val youtube = loggableProperty(_youtube)
        val xPlatform = loggableProperty(_xPlatform)
        val facebook = loggableProperty(_facebook)
        val telegram = loggableProperty(_telegram)
        val logo = loggableProperty(_logo)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}

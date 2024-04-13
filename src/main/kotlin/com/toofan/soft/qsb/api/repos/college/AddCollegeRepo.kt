package com.toofan.soft.qsb.api.repos.college

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object AddCollegeRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        var request: Request? = null

        data.invoke(
            { arabicName, englishName ->
                request = Request(arabicName, englishName)
            },
            { request!!.optional(it) }
        )

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.College.Add,
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
            englishName: String
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

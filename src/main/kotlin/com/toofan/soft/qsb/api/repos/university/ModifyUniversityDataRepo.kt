package com.toofan.soft.qsb.api.repos.university

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object ModifyUniversityDataRepo {
    @JvmStatic
    fun execute(
        data: (
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        val request = Request()

        data.invoke { request.optional(it) }

        runBlocking {
            ApiExecutor.execute(
                route = Route.University.Modify,
                request = request
            ) {
                onComplete(Response.map(it).getResource() as Resource<Boolean>)
            }
        }
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("arabic_name")
        private val _arabicName: OptionalVariable<String> = OptionalVariable(),
        @Field("english_name")
        private val _englishName: OptionalVariable<String> = OptionalVariable(),
        @Field("phone")
        private val _phone: OptionalVariable<Long> = OptionalVariable(),
        @Field("logo")
        private val _logo: OptionalVariable<ByteArray> = OptionalVariable(),
        @Field("email")
        private val _email: OptionalVariable<String> = OptionalVariable(),
        @Field("address")
        private val _address: OptionalVariable<String> = OptionalVariable(),
        @Field("description")
        private val _description: OptionalVariable<String> = OptionalVariable(),
        @Field("web")
        private val _web: OptionalVariable<String> = OptionalVariable(),
        @Field("youtube")
        private val _youtube: OptionalVariable<String> = OptionalVariable(),
        @Field("x_platform")
        private val _xPlatform: OptionalVariable<String> = OptionalVariable(),
        @Field("facebook")
        private val _facebook: OptionalVariable<String> = OptionalVariable(),
        @Field("telegram")
        private val _telegram: OptionalVariable<String> = OptionalVariable()
    ) : IRequest {
        val arabicName = loggableProperty(_arabicName)
        val englishName = loggableProperty(_englishName)
        val logo = loggableProperty(_logo)
        val phone = loggableProperty(_phone)
        val email = loggableProperty(_email)
        val address = loggableProperty(_address)
        val description = loggableProperty(_description)
        val web = loggableProperty(_web)
        val youtube = loggableProperty(_youtube)
        val xPlatform = loggableProperty(_xPlatform)
        val facebook = loggableProperty(_facebook)
        val telegram = loggableProperty(_telegram)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}

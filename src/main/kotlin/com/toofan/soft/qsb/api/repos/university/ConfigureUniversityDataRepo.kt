package com.toofan.soft.qsb.api.repos.university

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ConfigureUniversityDataRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            var request: Request? = null

            data.invoke(
                { arabicName, englishName, logo ->
                    request = Request(arabicName, englishName, logo)
                },
                { request!!.optional(it) }
            )

            request?.let {
                ApiExecutor.execute(
                    route = Route.University.Configure,
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
            logo: ByteArray
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
        @Field("logo")
        private val _logo: ByteArray,
        @Field("phone")
        private val _phone: OptionalVariable<Long> = OptionalVariable(),
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

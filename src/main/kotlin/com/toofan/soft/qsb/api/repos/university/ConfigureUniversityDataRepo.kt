package com.toofan.soft.qsb.api.repos.university

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import com.toofan.soft.qsb.api.loggableProperty
import kotlinx.coroutines.runBlocking

object ConfigureUniversityDataRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: MandatoryFunction,
            optional: OptionalFunction
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke(
            { arabicName, englishName ->
                request = Request(arabicName, englishName)
            },
            { request!!.optional(it) }
        )

        println(request!!.string())

//        request?.let {
//            runBlocking {
//                ApiExecutor.execute(
//                    route = Route.LOGIN,
//                    request = request
//                ) {
//                    val response = Response.map(it)
//                    onComplete(response)
//                }
//            }
//        }
    }

    fun interface MandatoryFunction {
        operator fun invoke(arabicName: String, englishName: String)
    }


    fun interface OptionalFunction {
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
        @Field("address")
        private val _address: OptionalVariable<String> = OptionalVariable(),
    ) : IRequest {
        val phone = loggableProperty(_phone)
        val email = loggableProperty(_email)
        val address = loggableProperty(_address)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }

    data class Response(
        @Field("is_success")
        internal val isSuccess: Boolean = false,
        @Field("error_message")
        internal val errorMessage: String? = null
    ) : IResponse {
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

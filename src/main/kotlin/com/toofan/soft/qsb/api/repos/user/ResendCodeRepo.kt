package com.toofan.soft.qsb.api.repos.user

import com.toofan.soft.qsb.api.*

object ResendCodeRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { email ->
                request = Request(email)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.User.ResendCode,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            email: String
        )
    }

    data class Request(
        @Field("email")
        private val _email: String
    ) : IRequest
}

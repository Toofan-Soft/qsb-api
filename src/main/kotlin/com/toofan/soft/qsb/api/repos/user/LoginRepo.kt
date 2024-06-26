package com.toofan.soft.qsb.api.repos.user

import com.toofan.soft.qsb.api.*

object LoginRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { email, password -> request = Request(email, password) }

            request?.let {
                ApiExecutor.execute(
                    route = Route.User.Login,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            email: String,
            password: String
        )
    }

    data class Request(
        @Field("email")
        private val _email: String,
        @Field("password")
        private val _password: String
    ) : IRequest
}
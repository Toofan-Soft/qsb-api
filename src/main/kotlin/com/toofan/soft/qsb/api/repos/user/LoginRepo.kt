package com.toofan.soft.qsb.api.repos.user

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object LoginRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
//        onComplete: (response: Response) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        var request: Request? = null

        data.invoke { email, password -> request = Request(email, password) }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.User.Login,
                    request = it
                ) {
                    val response = Response.map(it)
//                    onComplete(response)

                    if (response.isSuccess) {
                        onComplete(Resource.Success(true))
                    } else {
                        onComplete(Resource.Error(response.errorMessage))
                    }
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
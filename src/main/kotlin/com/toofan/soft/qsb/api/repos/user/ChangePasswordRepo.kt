package com.toofan.soft.qsb.api.repos.user

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object ChangePasswordRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
//        onComplete: (response: Response) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        var request: Request? = null

        data.invoke { email, password ->
            request = Request(email, password)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.User.ChangePassword,
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
        @Field("old_password")
        private val _oldPassword: String,
        @Field("new_password")
        private val _newPassword: String
    ) : IRequest
}

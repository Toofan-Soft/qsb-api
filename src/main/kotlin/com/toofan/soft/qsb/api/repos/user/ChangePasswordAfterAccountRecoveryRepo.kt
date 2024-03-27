package com.toofan.soft.qsb.api.repos.user

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object ChangePasswordAfterAccountRecoveryRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke { newPassword ->
            request = Request(newPassword)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.User.ChangePasswordAfterAccountRecovery,
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
            newPassword: String
        )
    }

    data class Request(
        @Field("new_password")
        private val _newPassword: String
    ) : IRequest
}

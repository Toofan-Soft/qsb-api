package com.toofan.soft.qsb.api.repos.user

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object ChangePasswordAfterAccountRecoveryRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
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
                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
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

package com.toofan.soft.qsb.api.repos.user

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ChangePasswordAfterAccountRecoveryRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            var request: Request? = null

            data.invoke { newPassword ->
                request = Request(newPassword)
            }

            request?.let {
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

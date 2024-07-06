package com.toofan.soft.qsb.api.repos.user

import com.toofan.soft.qsb.api.*

object ChangePasswordRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { oldPassword, newPassword ->
                request = Request(oldPassword, newPassword)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.User.ChangePassword,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            oldPassword: String,
            newPassword: String
        )
    }

    data class Request(
        @Field("old_password")
        private val _oldPassword: String,
        @Field("new_password")
        private val _newPassword: String
    ) : IRequest
}

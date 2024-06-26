package com.toofan.soft.qsb.api.repos.user

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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

            data.invoke { email, password ->
                request = Request(email, password)
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

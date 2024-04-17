package com.toofan.soft.qsb.api.repos.user

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object RequestAccountRecoveryRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        var request: Request? = null

        data.invoke { email ->
            request = Request(email)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.User.RequestAccountRecovery,
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

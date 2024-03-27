package com.toofan.soft.qsb.api.repos.user

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object RequestAccountRecoveryRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (response: Response) -> Unit
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
                    val response = Response.map(it)
                    onComplete(response)
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

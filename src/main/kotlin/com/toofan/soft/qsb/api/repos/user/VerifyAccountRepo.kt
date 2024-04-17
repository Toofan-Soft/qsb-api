package com.toofan.soft.qsb.api.repos.user

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object VerifyAccountRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        var request: Request? = null

        data.invoke { code ->
            request = Request(code)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.User.Verify,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            code: String
        )
    }

    data class Request(
        @Field("code")
        private val _code: String
    ) : IRequest
}

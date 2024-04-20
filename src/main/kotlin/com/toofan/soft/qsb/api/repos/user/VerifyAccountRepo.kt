package com.toofan.soft.qsb.api.repos.user

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object VerifyAccountRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            var request: Request? = null

            data.invoke { code ->
                request = Request(code)
            }

            request?.let {
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

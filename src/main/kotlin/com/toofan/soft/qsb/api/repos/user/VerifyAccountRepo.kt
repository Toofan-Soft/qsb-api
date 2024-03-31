package com.toofan.soft.qsb.api.repos.user

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object VerifyAccountRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
//        onComplete: (response: Response) -> Unit
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
            code: String
        )
    }

    data class Request(
        @Field("code")
        private val _code: String
    ) : IRequest
}

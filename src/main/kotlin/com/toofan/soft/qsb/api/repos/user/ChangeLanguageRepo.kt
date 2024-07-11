package com.toofan.soft.qsb.api.repos.user

import com.toofan.soft.qsb.api.*

object ChangeLanguageRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { languageId ->
                request = Request(languageId)
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
            languageId: Int
        )
    }

    data class Request(
        @Field("language_id")
        private val _languageId: Int
    ) : IRequest
}

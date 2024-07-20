package com.toofan.soft.qsb.api.repos.user

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.session.Language
import com.toofan.soft.qsb.api.session.Session

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
                    route = Route.User.ChangeLanguage,
                    request = it
                ) {
//                    onComplete(Response.map(it).getResource() as Resource<Boolean>)

                    when (val resource = Response.map(it).getResource() as Resource<Boolean>) {
                        is Resource.Success -> {
                            Language.of(request!!._languageId)?.let {
                                Session.updateLanguage(it)
                                onComplete(Resource.Success(resource.data))
                            } ?: onComplete(Resource.Error("Error!"))
                        }
                        is Resource.Error -> {
                            onComplete(Resource.Error(resource.message))
                        }
                    }
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
        internal val _languageId: Int
    ) : IRequest
}

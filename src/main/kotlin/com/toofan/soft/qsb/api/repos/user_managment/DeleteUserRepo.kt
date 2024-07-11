package com.toofan.soft.qsb.api.repos.user_managment

import com.toofan.soft.qsb.api.*

object DeleteUserRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { id ->
                request = Request(id)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.UserManagement.Delete,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            id: String
        )
    }

    data class Request(
        @Field("id")
        private val _id: String
    ) : IRequest
}

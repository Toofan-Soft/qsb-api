package com.toofan.soft.qsb.api.repos.user_managment

import com.toofan.soft.qsb.api.*

object AddUserRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke(
                { email, ownerTypeId, ownerId ->
                    request = Request(email, ownerTypeId, ownerId)
                },
                { request!!.optional(it) }
            )

            request?.let {
                ApiExecutor.execute(
                    route = Route.UserManagement.Add,
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
            ownerTypeId: Int,
            ownerId: Int
        )
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("email")
        private val _email: String,
        @Field("owner_type_id")
        private val _ownerTypeId: Int,
        @Field("owner_id")
        private val _ownerId: Int,
        @Field("roles_ids")
        private val _rolesIds: OptionalVariable<List<Int>> = OptionalVariable()
    ) : IRequest {
        val rolesIds = loggableProperty(_rolesIds)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}

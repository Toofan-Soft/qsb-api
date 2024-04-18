package com.toofan.soft.qsb.api.repos.user_managment

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AddUserRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            var request: Request? = null

            data.invoke { email, ownerTypeId, ownerId, rolesIds ->
                request = Request(email, ownerTypeId, ownerId, rolesIds)
            }

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
            ownerId: Int,
            rolesIds: List<Int>
        )
    }

    data class Request(
        @Field("email")
        private val _email: String,
        @Field("owner_type_id")
        private val _ownerTypeId: Int,
        @Field("owner_id")
        private val _ownerId: Int,
        @Field("roles_ids")
        private val _rolesIds: List<Int>
    ) : IRequest
}

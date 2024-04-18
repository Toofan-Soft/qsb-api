package com.toofan.soft.qsb.api.repos.user_managment

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ModifyUserRolesRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            var request: Request? = null

            data.invoke { id, rolesIds ->
                request = Request(id, rolesIds)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.UserManagement.ModifyRoleList,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            id: Int,
            rolesIds: List<Int>
        )
    }

    data class Request(
        @Field("id")
        private val _id: Int,
        @Field("roles_ids")
        private val _rolesIds: List<Int>
    ) : IRequest
}

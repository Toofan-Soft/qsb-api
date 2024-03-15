package com.toofan.soft.qsb.api.repos.user

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import com.toofan.soft.qsb.api.repos.department.AddDepartmentRepo
import kotlinx.coroutines.runBlocking

object AddUserRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke { email, ownerTypeId, ownerId, rolesIds ->
            request = Request(email, ownerTypeId, ownerId, rolesIds)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.Question.Add,
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

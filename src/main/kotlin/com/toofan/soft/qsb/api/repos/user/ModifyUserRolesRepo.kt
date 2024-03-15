package com.toofan.soft.qsb.api.repos.user

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import com.toofan.soft.qsb.api.repos.department.AddDepartmentRepo
import kotlinx.coroutines.runBlocking

object ModifyUserRolesRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke { id, rolesIds ->
            request = Request(id, rolesIds)
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

package com.toofan.soft.qsb.api.repos.college

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import kotlinx.coroutines.runBlocking

object DeleteCollegeRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        var request: Request? = null

        data.invoke { id ->
            request = Request(id)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.College.Delete,
                    request = it
                ) {
                    val response = Response.map(it)

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
            id: Int
        )
    }

    data class Request(
        @Field("id")
        private val _id: Int
    ) : IRequest
}

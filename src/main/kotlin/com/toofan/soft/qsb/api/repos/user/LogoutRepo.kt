package com.toofan.soft.qsb.api.repos.user

import com.toofan.soft.qsb.api.ApiExecutor
import com.toofan.soft.qsb.api.Resource
import com.toofan.soft.qsb.api.Response
import com.toofan.soft.qsb.api.Route
import kotlinx.coroutines.runBlocking

object LogoutRepo {
    @JvmStatic
    fun execute(
//        onComplete: (response: Response) -> Unit
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        runBlocking {
            ApiExecutor.execute(
                route = Route.User.Logout
            ) {
                val response = Response.map(it)
//                onComplete(response)

                if (response.isSuccess) {
                    onComplete(Resource.Success(true))
                } else {
                    onComplete(Resource.Error(response.errorMessage))
                }
            }
        }
    }
}

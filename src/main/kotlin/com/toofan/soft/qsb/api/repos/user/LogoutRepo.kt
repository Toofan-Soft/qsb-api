package com.toofan.soft.qsb.api.repos.user

import com.toofan.soft.qsb.api.*

object LogoutRepo {
    @JvmStatic
    suspend fun execute(
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        Coroutine.launch  {
            ApiExecutor.execute(
                route = Route.User.Logout
            ) {
                onComplete(Response.map(it).getResource() as Resource<Boolean>)
            }
        }
    }
}

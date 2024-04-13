package com.toofan.soft.qsb.api.repos.user

import com.toofan.soft.qsb.api.ApiExecutor
import com.toofan.soft.qsb.api.Resource
import com.toofan.soft.qsb.api.Response
import com.toofan.soft.qsb.api.Route
import kotlinx.coroutines.runBlocking

object LogoutRepo {
    @JvmStatic
    fun execute(
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        runBlocking {
            ApiExecutor.execute(
                route = Route.User.Logout
            ) {
                onComplete(Response.map(it).getResource() as Resource<Boolean>)
            }
        }
    }
}

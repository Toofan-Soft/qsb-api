package com.toofan.soft.qsb.api.repos.user

import com.toofan.soft.qsb.api.ApiExecutor
import com.toofan.soft.qsb.api.Resource
import com.toofan.soft.qsb.api.Response
import com.toofan.soft.qsb.api.Route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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

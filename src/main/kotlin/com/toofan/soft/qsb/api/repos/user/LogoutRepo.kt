package com.toofan.soft.qsb.api.repos.user

import com.toofan.soft.qsb.api.ApiExecutor
import com.toofan.soft.qsb.api.Response
import com.toofan.soft.qsb.api.Route
import kotlinx.coroutines.runBlocking

object LogoutRepo {
    @JvmStatic
    fun execute(
        onComplete: (response: Response) -> Unit
    ) {
        runBlocking {
            ApiExecutor.execute(
                route = Route.Question.Add
            ) {
                val response = Response.map(it)
                onComplete(response)
            }
        }
    }
}

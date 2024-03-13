package com.toofan.soft.qsb.api.repos

import com.toofan.soft.qsb.api.Route
import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object LoginRepo {
    @JvmStatic
    fun execute(email: String, password: String) {
        runBlocking {
            val request = Request(email, password)
            ApiExecutor.execute(
                route = Route.LOGIN,
                request = request
            )
        }
    }

    private data class Request(
        @Field("email")
        val email: String,
        @Field("password")
        val password: String
    ) : IRequest
}

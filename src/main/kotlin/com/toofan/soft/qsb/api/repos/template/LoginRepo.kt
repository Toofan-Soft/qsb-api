package com.toofan.soft.qsb.api.repos.template

import com.toofan.soft.qsb.api.Route
import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object LoginRepo {
    @JvmStatic
    fun execute(email: String, password: String) {
        runBlocking {
            val request = Request(email, password)
            ApiExecutor.execute(
                route = Route.Template.Login,
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

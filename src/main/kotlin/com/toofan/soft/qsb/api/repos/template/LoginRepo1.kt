package com.toofan.soft.qsb.api.repos.template

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.Field
import com.toofan.soft.qsb.api.IRequest
import com.toofan.soft.qsb.api.Resource
import com.toofan.soft.qsb.api.Response
import kotlinx.coroutines.runBlocking

object LoginRepo1 {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        var request: Request? = null

        data.invoke { email, password -> request = Request(email, password) }

        request?.let {
            runBlocking {
                val jsonObject = JsonObject()
                jsonObject.addProperty("is_success", false)
                jsonObject.addProperty("error_message", "Internet is not available, check it then try again :)")

                onComplete(Response.map(jsonObject).getResource() as Resource<Boolean>)
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            email: String,
            password: String
        )
    }

    data class Request(
        @Field("email")
        private val _email: String,
        @Field("password")
        private val _password: String
    ) : IRequest
}
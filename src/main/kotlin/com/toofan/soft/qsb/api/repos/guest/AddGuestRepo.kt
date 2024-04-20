package com.toofan.soft.qsb.api.repos.guest

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object AddGuestRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            var request: Request? = null

            data.invoke(
                { name, genderId, email, password ->
                    request = Request(name, genderId, email, password)
                },
                { request!!.optional(it) }
            )

            request?.let {
                ApiExecutor.execute(
                    route = Route.Guest.Add,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            name: String,
            genderId: Int,
            email: String,
            password: String
        )
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("name")
        private val _name: String,
        @Field("gender_id")
        private val _genderId: Int,
        @Field("email")
        private val _email: String,
        @Field("password")
        private val _password: String,

        @Field("phone")
        private val _phone: OptionalVariable<Long> = OptionalVariable(),
        @Field("image")
        private val _image: OptionalVariable<ByteArray> = OptionalVariable()
    ) : IRequest {
        val phone = loggableProperty(_phone)
        val image = loggableProperty(_image)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}

package com.toofan.soft.qsb.api.repos.guest

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import com.toofan.soft.qsb.api.repos.department.AddDepartmentRepo
import kotlinx.coroutines.runBlocking

object AddGuestRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
//        onComplete: (response: Response) -> Unit
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        var request: Request? = null

        data.invoke(
            { name, genderId, email, password ->
                request = Request(name, genderId, email, password)
            },
            { request!!.optional(it) }
        )

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.Guest.Add,
                    request = it
                ) {
                    val response = Response.map(it)
//                    onComplete(response)

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

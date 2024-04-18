package com.toofan.soft.qsb.api.repos.guest

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ModifyGuestProfileRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            var request: Request? = null

            data.invoke {
                request = Request()
                request!!.optional(it)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.Guest.Modify,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
                }
            }
        }
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("name")
        private val _name: OptionalVariable<String> = OptionalVariable(),
        @Field("gender_id")
        private val _genderId: OptionalVariable<Int> = OptionalVariable(),
        @Field("phone")
        private val _phone: OptionalVariable<Long> = OptionalVariable(),
        @Field("image")
        private val _image: OptionalVariable<ByteArray> = OptionalVariable()
    ) : IRequest {
        val name = loggableProperty(_name)
        val genderId = loggableProperty(_genderId)
        val phone = loggableProperty(_phone)
        val image = loggableProperty(_image)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}

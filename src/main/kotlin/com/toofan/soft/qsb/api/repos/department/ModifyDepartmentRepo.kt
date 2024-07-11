package com.toofan.soft.qsb.api.repos.department

import com.toofan.soft.qsb.api.*

object ModifyDepartmentRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke(
                { id ->
                    request = Request(id)
                },
                { request!!.optional(it) }
            )

            request?.let {
                ApiExecutor.execute(
                    route = Route.Department.Modify,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            id: Int
        )
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("id")
        private val _id: Int,
        @Field("arabic_name")
        private val _arabicName: OptionalVariable<String> = OptionalVariable(),
        @Field("english_name")
        private val _englishName: OptionalVariable<String> = OptionalVariable(),
        @Field("levels_count_id")
        private val _levelsCountId: OptionalVariable<Int> = OptionalVariable(),
        @Field("description")
        private val _description: OptionalVariable<String> = OptionalVariable(),
        @Field("logo")
        private val _logo: OptionalVariable<ByteArray> = OptionalVariable()
    ) : IRequest {
        val arabicName = loggableProperty(_arabicName)
        val englishName = loggableProperty(_englishName)
        val levelsCountId = loggableProperty(_levelsCountId)
        val description = loggableProperty(_description)
        val logo = loggableProperty(_logo)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}

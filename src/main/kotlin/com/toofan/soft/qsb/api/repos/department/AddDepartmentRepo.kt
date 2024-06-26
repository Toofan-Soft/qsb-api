package com.toofan.soft.qsb.api.repos.department

import com.toofan.soft.qsb.api.*

object AddDepartmentRepo {
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
                { collegeId, arabicName, englishName, levelCount ->
                    request = Request(collegeId, arabicName, englishName, levelCount)
                },
                { request!!.optional(it) }
            )

            request?.let {
                ApiExecutor.execute(
                    route = Route.Department.Add,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            collegeId: Int,
            arabicName: String,
            englishName: String,
            levelCount: Int
        )
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("college_id")
        private val _collegeId: Int,
        @Field("arabic_name")
        private val _arabicName: String,
        @Field("english_name")
        private val _englishName: String,
        @Field("levels_count")
        private val _levelsCount: Int,
        @Field("description")
        private val _description: OptionalVariable<String> = OptionalVariable(),
        @Field("logo")
        private val _logo: OptionalVariable<ByteArray> = OptionalVariable()
    ) : IRequest {
        val description = loggableProperty(_description)
        val logo = loggableProperty(_logo)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}

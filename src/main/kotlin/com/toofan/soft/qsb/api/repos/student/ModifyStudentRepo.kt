package com.toofan.soft.qsb.api.repos.student

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object ModifyStudentRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        var request: Request? = null

        data.invoke(
            { id ->
                request = Request(id)
            },
            { request!!.optional(it) }
        )

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.Student.Modify,
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

        @Field("academic_id")
        private val _academicId: OptionalVariable<Int> = OptionalVariable(),
        @Field("arabic_name")
        private val _arabicName: OptionalVariable<String> = OptionalVariable(),
        @Field("english_name")
        private val _englishName: OptionalVariable<String> = OptionalVariable(),
        @Field("gender_id")
        private val _genderId: OptionalVariable<Int> = OptionalVariable(),
        @Field("phone")
        private val _phone: OptionalVariable<Long> = OptionalVariable(),
        @Field("birthdate")
        private val _birthdate: OptionalVariable<Long> = OptionalVariable(),
        @Field("image")
        private val _image: OptionalVariable<ByteArray> = OptionalVariable(),
        @Field("level_id")
        private val _levelId: OptionalVariable<Int> = OptionalVariable()
    ) : IRequest {
        val academicId = loggableProperty(_academicId)
        val arabicName = loggableProperty(_arabicName)
        val englishName = loggableProperty(_englishName)
        val genderId = loggableProperty(_genderId)
        val phone = loggableProperty(_phone)
        val birthdate = loggableProperty(_birthdate)
        val image = loggableProperty(_image)
        val levelId = loggableProperty(_levelId)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}

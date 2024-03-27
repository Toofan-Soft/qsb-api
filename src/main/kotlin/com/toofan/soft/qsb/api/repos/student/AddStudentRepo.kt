package com.toofan.soft.qsb.api.repos.student

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import com.toofan.soft.qsb.api.repos.department.AddDepartmentRepo
import kotlinx.coroutines.runBlocking

object AddStudentRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke(
            { academicId, arabicName, englishName, genderId, departmentId, levelId ->
                request = Request(academicId, arabicName, englishName, genderId, departmentId, levelId)
            },
            { request!!.optional(it) }
        )

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.Student.Add,
                    request = it
                ) {
                    val response = Response.map(it)
                    onComplete(response)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            academicId: Int,
            arabicName: String,
            englishName: String,
            genderId: Int,
            departmentId: Int,
            levelId: Int
        )
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("academic_id")
        private val _academicId: Int,
        @Field("arabic_name")
        private val _arabicName: String,
        @Field("english_name")
        private val _englishName: String,
        @Field("gender_id")
        private val _genderId: Int,
        @Field("department_id")
        private val departmentId: Int,
        @Field("level_id")
        private val _levelId: Int,

        @Field("email")
        private val _email: OptionalVariable<String> = OptionalVariable(),
        @Field("phone")
        private val _phone: OptionalVariable<Long> = OptionalVariable(),
        @Field("birthdate")
        private val _birthdate: OptionalVariable<Long> = OptionalVariable(),
        @Field("image")
        private val _image: OptionalVariable<ByteArray> = OptionalVariable()
    ) : IRequest {
        val email = loggableProperty(_email)
        val phone = loggableProperty(_phone)
        val birthdate = loggableProperty(_birthdate)
        val image = loggableProperty(_image)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}

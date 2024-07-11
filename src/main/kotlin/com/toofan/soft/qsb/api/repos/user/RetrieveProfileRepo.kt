package com.toofan.soft.qsb.api.repos.user

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.session.Auth
import com.toofan.soft.qsb.api.session.Profile
import com.toofan.soft.qsb.api.session.UserType
import java.time.LocalDate

object RetrieveProfileRepo {
    @JvmStatic
    suspend fun execute(
        onComplete: (Resource<Profile>) -> Unit
    ) {
        Coroutine.launch {
            ApiExecutor.execute(
                route = Route.User.RetrieveProfile,
            ) {
                when (val resource = Response.map(it).getResource() as Resource<Response.Data>) {
                    is Resource.Success -> {
                        resource.data?.let {
                            onComplete(Resource.Success(it.getData()))
                        }
                    }
                    is Resource.Error -> {
                        onComplete(Resource.Error(resource.message))
                    }
                }
            }
        }
    }

    internal data class Response(
        @Field("is_success")
        val isSuccess: Boolean = false,
        @Field("error_message")
        val errorMessage: String? = null,
        @Field("data")
        val data: Data? = null
    ) : IResponse {
        internal data class Data(
            @Field("name")
            val name: String = "",

            @Field("arabic_name")
            val arabicName: String = "",
            @Field("english_name")
            val englishName: String = "",
            @Field("email")
            val email: String = "",
            @Field("gender_name")
            val genderName: String = "",
            @Field("phone")
            val phone: Long? = null,
            @Field("image_url")
            val imageUrl: String? = null,
            @Field("birthdate")
            val birthdate: LocalDate? = null,

            @Field("qualification_name")
            val qualificationName: String = "",
            @Field("job_type_name")
            val jobTypeName: String = "",
            @Field("specialization")
            val specialization: String? = null
        ) : IResponse {
            fun getData(): Profile? {
                return when (Auth.user) {
                    UserType.GUEST -> {
                        Profile.Guest(
                            name,
                            email,
                            genderName,
                            phone,
                            imageUrl
                        )
                    }
                    UserType.STUDENT -> {
                        Profile.Student(
                            arabicName,
                            englishName,
                            email,
                            genderName,
                            phone,
                            imageUrl,
                            birthdate?.toString()
                        )
                    }
                    UserType.EMPLOYEE -> {
                        Profile.Employee(
                            arabicName,
                            englishName,
                            email,
                            genderName,
                            qualificationName,
                            jobTypeName,
                            phone,
                            imageUrl,
                            specialization
                        )
                    }
                    null -> null
                }
            }
        }

        companion object {
            private fun getInstance(): Response {
                return Response()
            }

            fun map(data: JsonObject): Response {
                return getInstance().getResponse(data) as Response
            }
        }
    }
}
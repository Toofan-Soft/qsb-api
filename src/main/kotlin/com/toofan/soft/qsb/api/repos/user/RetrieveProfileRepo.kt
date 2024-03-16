package com.toofan.soft.qsb.api.repos.user

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object RetrieveProfileRepo {
    @JvmStatic
    fun execute(
        onComplete: (response: Response) -> Unit
    ) {
        runBlocking {
            ApiExecutor.execute(
                route = Route.Question.Add,
            ) {
                val response = Response.map(it)
                onComplete(response)
            }
        }
    }

    data class Response(
        @Field("is_success")
        val isSuccess: Boolean = false,
        @Field("error_message")
        val errorMessage: String? = null,
        @Field("data")
        val data: Data? = null
    ) : IResponse {

        sealed interface Data {
            data class Guest(
                @Field("name")
                val name: String,
                @Field("email")
                val email: String,
                @Field("gender_id")
                val genderId: Int,
                @Field("phone")
                val phone: Long? = null,
                @Field("image_url")
                val imageUrl: String? = null
            ) : Data

            data class Employee (
                @Field("arabic_name")
                val arabicName: String,
                @Field("english_name")
                val englishName: String,
                @Field("email")
                val email: String,
                @Field("gender_name")
                val genderName: String,
                @Field("qualification_name")
                val qualificationName: String,
                @Field("job_type_name")
                val jobTypeName: String,
                @Field("phone")
                val phone: Long? = null,
                @Field("image_url")
                val imageUrl: String? = null,
                @Field("specialization")
                val specialization: String? = null
            ) : Data

            data class Student(
                @Field("arabic_name")
                val arabicName: String,
                @Field("english_name")
                val englishName: String,
                @Field("email")
                val email: String,
                @Field("gender_name")
                val genderName: String,
                @Field("phone")
                val phone: Long? = null,
                @Field("image_url")
                val imageUrl: String? = null,
                @Field("birthdate")
                val birthdate: Long? = null
            ) : Data
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

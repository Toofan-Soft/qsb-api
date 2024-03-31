package com.toofan.soft.qsb.api.repos.course

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import kotlinx.coroutines.runBlocking

object RetrieveCoursesRepo {
    @JvmStatic
    fun execute(
//        onComplete: (response: Response) -> Unit
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        runBlocking {
            ApiExecutor.execute(
                route = Route.Course.RetrieveList
            ) {
                val response = Response.map(it)
//                onComplete(response)

                if (response.isSuccess) {
                    onComplete(Resource.Success(response.data))
                } else {
                    onComplete(Resource.Error(response.errorMessage))
                }
            }
        }
    }

    data class Response(
        @Field("is_success")
        val isSuccess: Boolean = false,
        @Field("error_message")
        val errorMessage: String? = null,
        @Field("data")
        val data: List<Data> = emptyList()
    ) : IResponse {

        data class Data(
            @Field("id")
            val id: Int,
            @Field("arabic_name")
            val arabicName: String,
            @Field("english_name")
            val englishName: String
        )

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

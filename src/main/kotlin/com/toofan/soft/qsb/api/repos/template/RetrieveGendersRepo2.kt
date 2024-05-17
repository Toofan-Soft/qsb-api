package com.toofan.soft.qsb.api.repos.template

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.utils.InternetUtils

object RetrieveGendersRepo2 {
    @JvmStatic
    suspend fun execute(
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        println("bbbbbbb")
        Coroutine.launch {
            println("aaaaaaa")

//            val resource = withContext(Dispatchers.IO) {
//                if (InternetUtils.isInternetAvailable()) {
//                    Resource.Success(
//                        data = listOf(
//                            Response.Data(1, "Male"),
//                            Response.Data(2, "Female"),
//                        )
//                    )
//                } else {
//                    Resource.Error(
//                        message = "Internet is not available, check it then try again :)"
//                    )
//                }
//            }

            onComplete(
                if (InternetUtils.isInternetAvailable()) {
                    Resource.Success(
                        data = listOf(
                            Response.Data(1, "Male"),
                            Response.Data(2, "Female"),
                        )
                    )
                } else {
                    Resource.Error(
                        message = "Internet is not available, check it then try again :)"
                    )
                }
            )

//            onComplete(resource)
            println("aaaaaaa2")
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
            val id: Int = 0,
            @Field("name")
            val name: String = ""
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
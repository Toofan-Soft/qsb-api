package com.toofan.soft.qsb.api.repos.template

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.utils.InternetUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

object RetrieveGendersRepo2 {
    @JvmStatic
    suspend fun execute(
        dispatcher: CoroutineDispatcher,
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        println("dispatcher: $dispatcher")
        withContext(dispatcher) {
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

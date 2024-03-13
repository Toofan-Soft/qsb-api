package com.toofan.soft.qsb.api

import com.google.gson.JsonObject

data class Response(
    @Field("is_success")
    val isSuccess: Boolean = false,
    @Field("error_message")
    val errorMessage: String? = null
) : IResponse {
    companion object {
        private fun getInstance(): Response {
            return Response()
        }

        fun map(data: JsonObject): Response {
            return getInstance().getResponse(data) as Response
        }
    }
}
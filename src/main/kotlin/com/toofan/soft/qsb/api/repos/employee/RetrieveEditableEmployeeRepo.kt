package com.toofan.soft.qsb.api.repos.employee

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object RetrieveEditableEmployeeRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Response.Data>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            var request: Request? = null

            data.invoke { id ->
                request = Request(id)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.Employee.RetrieveEditable
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Response.Data>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            id: Int
        )
    }

    data class Request(
        @Field("id")
        private val _id: Int
    ) : IRequest

    data class Response(
        @Field("is_success")
        val isSuccess: Boolean = false,
        @Field("error_message")
        val errorMessage: String? = null,
        @Field("data")
        val data: Data? = null
    ) : IResponse {

        data class Data(
            @Field("arabic_name")
            val arabicName: String = "",
            @Field("english_name")
            val englishName: String = "",
            @Field("gender_id")
            val genderId: Int = 0,
            @Field("qualification_id")
            val qualificationId: Int = 0,
            @Field("job_type_id")
            val jobTypeId: Int = 0,
            @Field("phone")
            val phone: Long? = null,
            @Field("specialization")
            val specialization: String? = null,
            @Field("image_url")
            val imageUrl: String? = null
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

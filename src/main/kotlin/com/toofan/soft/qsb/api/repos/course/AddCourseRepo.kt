package com.toofan.soft.qsb.api.repos.course

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object AddCourseRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            var request: Request? = null

            data.invoke { arabicName, englishName ->
                request = Request(arabicName, englishName)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.Course.Add,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            arabicName: String,
            englishName: String
        )
    }

    data class Request(
        @Field("arabic_name")
        private val _arabicName: String,
        @Field("english_name")
        private val _englishName: String,
    ) : IRequest
}

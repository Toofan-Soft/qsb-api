package com.toofan.soft.qsb.api.repos.practice_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*

object AddPracticeExamRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<Response.Data>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke(
                { departmentCoursePartId, conductMethodId, duration, languageId, difficultyLevelId, questionsTypes, topicsIds ->
                    val _questionsTypes: ArrayList<Request.Data> = arrayListOf()

                    questionsTypes.invoke { typeId, questionsCount ->
                        _questionsTypes.add(Request.Data(typeId, questionsCount))
                    }

                    request = Request(
                        departmentCoursePartId, conductMethodId, duration, languageId,
                        difficultyLevelId, _questionsTypes, topicsIds
                    )
                },
                { request!!.optional(it) }
            )

            request?.let {
                ApiExecutor.execute(
                    route = Route.PracticeOnlineExam.Add,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Response.Data>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            departmentCoursePartId: Int,
            conductMethodId: Int,
            duration: Int,
            languageId: Int,
            difficultyLevelId: Int,
            questionsTypes: (Data) -> Unit,
            topicsIds: List<Int>
        )

        fun interface Data {
            operator fun invoke(
                typeId: Int,
                questionsCount: Int
            )
        }
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("department_course_part_id")
        private val _departmentCoursePartId: Int,
        @Field("conduct_method_id")
        private val _conductMethodId: Int,
        @Field("duration")
        private val _duration: Int,
        @Field("language_id")
        private val _languageId: Int,
        @Field("difficulty_level_id")
        private val _difficultyLevelId: Int,

        @Field("questions_types")
        private val _questionsTypes: List<Data>,
        @Field("topics_ids")
        private val _topicsIds: List<Int>,

        @Field("title")
        private val _title: OptionalVariable<String> = OptionalVariable()
    ) : IRequest {
        val title = loggableProperty(_title)

        data class Data(
            @Field("type_id")
            private val _typeId: Int,
            @Field("questions_count")
            private val _questionsCount: Int
        )

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
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

        data class Data(
            @Field("id")
            val id: Int = 0
        ) : IResponse

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

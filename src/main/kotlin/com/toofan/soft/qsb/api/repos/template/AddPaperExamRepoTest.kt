package com.toofan.soft.qsb.api.repos.template

import com.toofan.soft.qsb.api.*

object AddPaperExamRepoTest {
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
            { typeId, questionsTypes ->
                val _questionsTypes: ArrayList<Request.Data> = arrayListOf()

                questionsTypes.invoke { typeId, questionsCount, questionScore ->
                    println("typeId: $typeId, questionsCount: $questionsCount, questionScore: $questionScore")
                    _questionsTypes.add(Request.Data(typeId, questionsCount, questionScore))
                }
                request = Request(typeId, _questionsTypes)
            },
            { request!!.optional(it) }
        )

        println(request!!.parameters)

//        request?.let {
//            runBlocking {
//                ApiExecutor.execute(
//                    route = Route.Question.Add,
//                    request = it
//                ) {
//                    val response = Response.map(it)
//                    onComplete(response)
//                }
//            }
//        }
    }

    fun interface Mandatory {
        operator fun invoke(
            typeId: Int,

            questionsTypes: (Data) -> Unit
        )

        fun interface Data {
            operator fun invoke(
                typeId: Int,
                questionsCount: Int,
                questionScore: Float
            )
        }
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("type_id")
        private val _typeId: Int,
        @Field("questions_types")
        private val _questionsTypes: List<Data>,
        @Field("special_note")
        private val _specialNote: OptionalVariable<String> = OptionalVariable()
    ) : IRequest {
        val specialNote = loggableProperty(_specialNote)

        data class Data(
            @Field("type_id")
            private val _typeId: Int,
            @Field("questions_count")
            private val _questionsCount: Int,
            @Field("question_score")
            private val _questionScore: Float,
        ) {
//            fun Mandatory.Data.toData(): Request.Data {
//                return Request.Data(
//                    _typeId = this.typeId,
//                    _questionsCount = this.questionsCount,
//                    _questionScore = this.questionScore
//                )
//            }
        }

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}

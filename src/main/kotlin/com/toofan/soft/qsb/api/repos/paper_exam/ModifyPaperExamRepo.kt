package com.toofan.soft.qsb.api.repos.paper_exam

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object ModifyPaperExamRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        var request: Request? = null

        data.invoke(
            { id ->
                request = Request(id)
            },
            { request!!.optional(it) }
        )

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.PaperExam.Modify,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            id: Int
        )
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("id")
        private val _id: Int,

        @Field("type_id")
        private val _typeId: OptionalVariable<Int> = OptionalVariable(),
        @Field("datetime")
        private val _datetime: OptionalVariable<Long> = OptionalVariable(),
        @Field("lecturer_name")
        private val _lecturerName: OptionalVariable<String> = OptionalVariable(),

        @Field("form_name_method_id")
        private val _formNameMethodId: OptionalVariable<Int> = OptionalVariable(),

        @Field("special_note")
        private val _specialNote: OptionalVariable<String> = OptionalVariable()
    ) : IRequest {
        val typeId = loggableProperty(_typeId)
        val datetime = loggableProperty(_datetime)
        val lecturerName = loggableProperty(_lecturerName)

        val formNameMethodId = loggableProperty(_formNameMethodId)

        val specialNote = loggableProperty(_specialNote)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}

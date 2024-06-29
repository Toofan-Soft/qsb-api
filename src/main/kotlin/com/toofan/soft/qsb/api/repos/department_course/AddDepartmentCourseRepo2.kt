//package com.toofan.soft.qsb.api.repos.department_course
//
//import com.toofan.soft.qsb.api.*
//
//object AddDepartmentCourseRepo2 {
//    @JvmStatic
//    suspend fun execute(
//        data: (
//            mandatory: Mandatory,
//            optional: Optional
//        ) -> Unit,
//        onComplete: (Resource<Boolean>) -> Unit
//    ) {
//        Coroutine.launch {
//            var request: Request? = null
//            var hasError = false
//
//            data.invoke(
//                { departmentId, levelId, semesterId, courseId ->
//                    request = Request(departmentId, levelId, semesterId, courseId)
//                },
//                { part ->
//                    val parts: ArrayList<Request.Data> = arrayListOf()
//
//                    part.invoke {
//                        Request.Data().also {
//                            parts.add(it)
//                        }.let(it)
//                    }
//
//                    hasError = parts.any { it._id.value == null } ||
//                            parts.groupBy { it._id.value }
//                                .any { it.value.size > 1 }
//
//                    if (!hasError && parts.isNotEmpty()) {
//                        request!!.parts(parts)
//                    }
//                }
//            )
//
//            if (!hasError) {
//                request?.let {
//                    ApiExecutor.execute(
//                        route = Route.DepartmentCourse.Add,
//                        request = it
//                    ) {
//                        onComplete(Response.map(it).getResource() as Resource<Boolean>)
//                    }
//                }
//            } else {
//                onComplete(Resource.Error("Parts Error!"))
//            }
//        }
//    }
//
//    fun interface Mandatory {
//        operator fun invoke(
//            departmentId: Int,
//            levelId: Int,
//            semesterId: Int,
//            courseId: Int
//        )
//    }
//
//    fun interface Optional {
//        operator fun invoke(
//            part: (
//                optional: com.toofan.soft.qsb.api.Optional<Request.Data>
//            ) -> Unit
//        )
//    }
//
//    data class Request(
//        @Field("department_id")
//        private val _departmentId: Int,
//        @Field("level_id")
//        private val _levelId: Int,
//        @Field("semester_id")
//        private val _semesterId: Int,
//        @Field("course_id")
//        private val _courseId: Int,
//        @Field("parts")
//        private val _parts: OptionalVariable<List<Data>> = OptionalVariable()
//    ) : IRequest {
//        internal val parts = loggableProperty(_parts)
//
//        data class Data(
//            @Field("id")
//            internal val _id: OptionalVariable<Int> = OptionalVariable(),
//            @Field("score")
//            internal val _score: OptionalVariable<Int> = OptionalVariable(),
//            @Field("lectures_count")
//            internal val _lecturesCount: OptionalVariable<Int> = OptionalVariable(),
//            @Field("lecture_duration")
//            internal val _lectureDuration: OptionalVariable<Int> = OptionalVariable(),
//            @Field("note")
//            internal val _note: OptionalVariable<String> = OptionalVariable()
//        ) : IRequest {
//            val id = loggableProperty(_id)
//            val score = loggableProperty(_score)
//            val lecturesCount = loggableProperty(_lecturesCount)
//            val lectureDuration = loggableProperty(_lectureDuration)
//            val note = loggableProperty(_note)
//
//            fun optional(block: Data.() -> Unit): Data {
//                return build(block)
//            }
//        }
//    }
//}

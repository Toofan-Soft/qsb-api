package com.toofan.soft.qsb.api

import com.toofan.soft.qsb.api.repos.paper_exam.AddPaperExamRepo
import com.toofan.soft.qsb.api.repos.question.AddQuestionRepo
import com.toofan.soft.qsb.api.repos.question.ModifyQuestionRepo
import com.toofan.soft.qsb.api.repos.user.LoginRepo
import kotlinx.coroutines.runBlocking

fun main2() {
    runBlocking {
        Api.init("192.168.1.15")

        LoginRepo.execute(
            data = {
//                it.invoke("user9@gmail.com", "123456aa")
                it.invoke("llll123456@gmail.com", "llll123456")
            },
            onComplete = {
                println("complete")

                runBlocking {
                    AddQuestionRepo.execute(
                        data = { mandatory, optional ->
//                mandatory.invoke(
//                    1,
//                    0,
//                    1,
//                    1,
//                    1,
//                    20,
//                    "content",
//                    choice = {
//                        it.invoke(-1, "", false, null)
//                        it.invoke(-2, "", true, null)
//                        it.invoke(1, "", true, null)
//                    }
//                )
//                            val htmlContent = "<html dir=\"rtl\"><head></head><body contenteditable=\"true\"><p><span style=\"font-family: &quot;&quot;;\">Are You Male?</span></p></body></html>"
//                                .trimIndent()
//                            val encodedContent = URLEncoder.encode(htmlContent, "UTF-8")


                            mandatory.invoke(
                                1,
                                1,
                                1,
                                1,
                                1,
                                20,
//                                "content111",
                                "<html dir=\"rtl\"><head></head><body contenteditable=\"true\"><p><span style=\"font-family: &quot;&quot;;\">Are You Male?</span></p></body></html>",
//                                encodedContent,
                                choice = { mandatory, optional ->
                                    mandatory.invoke(
                                        null,
                                        "<html dir=\"rtl\"><head></head><body contenteditable=\"true\"><p><span style=\"font-family: &quot;&quot;;\">content 1</span></p></body></html>",
                                        true
                                    )
                                    mandatory.invoke(
                                        null,
                                        "<html dir=\"rtl\"><head></head><body contenteditable=\"true\"><p><span style=\"font-family: &quot;&quot;;\">content 2</span></p></body></html>",
                                        true
                                    )
//                        optional.invoke {
//                            attachment(ByteArray(5))
//                        }
                                    mandatory.invoke(
                                        null,
                                        "<html dir=\"rtl\"><head></head><body contenteditable=\"true\"><p><span style=\"font-family: &quot;&quot;;\">content 3</span></p></body></html>",
                                        false
                                    )
                                }
                            )
//                optional.invoke {
//                    title("title")
//                }
                        },
                        onComplete = {
                            when (it) {
                                is Resource.Success -> {
                                    println("Success :)")
                                }
                                is Resource.Error -> {
                                    println("Error: " + it.message)
                                }
                            }
                        }
                    )
                }
            }
        )
    }
}

fun main3() {
    runBlocking {
//        Api.init("192.168.1.18")
        ModifyQuestionRepo.execute(
            data = { optional ->
                optional.invoke(
                    {
                        id(2)
                    },
                    choice = { optional ->
                        optional.invoke {
                            id(5)
                            content("content")
                        }
                        optional.invoke {
                            id(6)
                            content("content6")
                        }
                        optional.invoke {
                            id(-1)
                            content("content6")
                        }
                    }
                )
//                optional.invoke {
//                    choices.invoke()
//                }
//                optional.invoke {
//                    title("title")
//                }
            },
            onComplete = {
                when (it) {
                    is Resource.Success -> {
                        println("Success :)")
                    }
                    is Resource.Error -> {
                        println("Error: " + it.message)
                    }
                }
            }
        )
    }
}

fun main4() {
    runBlocking {
        Api.init("192.168.1.18")
//        AddDepartmentCourseRepo2.execute(
//            data = { mandatory, optional ->
//                mandatory.invoke(
//                    1,
//                    1,
//                    1,
//                    1,
//                )
//                optional.invoke {
//                    it.invoke {
//                        id(5)
//                        lecturesCount(8)
//                    }
//
//                    it.invoke {
//                        id(3)
//                        lecturesCount(2)
//                        lectureDuration(7)
//                    }
//                }
//            },
//            onComplete = {
//                when (it) {
//                    is Resource.Success -> {
//                        println("Success :)")
//                    }
//                    is Resource.Error -> {
//                        println("Error: " + it.message)
//                    }
//                }
//            }
//        )
    }
}

fun main(args: Array<String>) {
    println("Hello World!")

    println("Program arguments: ${args.joinToString()}")

    runBlocking {
        Api.init("192.168.1.15")

        LoginRepo.execute(
            data = {
//                it.invoke("user9@gmail.com", "123456aa")
                it.invoke("llll123456@gmail.com", "llll123456")
//                it.invoke("ssss1234@gmail.com", "ssss1234")
            },
            onComplete = {
                println("complete")
                runBlocking {

//                    RetrieveOnlineExamsAndroidRepo.execute(
//                        data = {
////                            it.invoke
//                        },
//                        onComplete = {
//                            println("complete...")
//                            println(it.data)
//                        }
//                    )

//                    AddOnlineExamRepo.execute(
//                        data = { mandatory, optional ->
//                            mandatory.invoke(
//                                2,
//                                0,
//                                0,
//                                1720126800000,
//                                100,
//                                1720126800000,
//                                1720126800000,
//                                0,
//                                1,
//                                1,
//                                1,
//                                1,
//                                {
//                                    it.invoke(1, 5, 1f)
//                                },
//                                listOf(3)
//                            )
//                        },
//                        onComplete = {
//                            println("complete...")
//                            println(it.data)
//                        }
//                    )

                    AddPaperExamRepo.execute(
                        data = { mandatory, optional ->
                            mandatory.invoke(
                                2,
                                0,
                                1720126800000,
                                900,
                                0,
                                1,
                                "asdsa",
                                1,
                                0,
                                0,
                                {
                                    it.invoke(1, 10, 10f)
                                },
                                listOf(3)
                            )
                        },
                        onComplete = {
                            println("complete...")
                            println(it.data)
                        }
                    )

//        Api.init("127.0.0.1")
//        LoginRepo.execute(
//            data = {
////                it.invoke("user9@gmail.com", "123456aa")
//                it.invoke("llll123456@gmail.com", "llll123456")
//            },
//            onComplete = {
//                println("complete")
//                runBlocking {
//                    RetrieveProfileRepo.execute {
//                        println("complete")
//                    }
////                    RetrievePracticeExamsAndroidRepo.execute(
////                        data = {},
////                        onComplete = {
////
////                        }
////                    )
//                }
//            }
//        )

//        AddCollegeRepo.execute(
//            data = { mandatory, optional ->
//                mandatory.invoke("كلية 2", "College 2")
//                optional.invoke {
//                    email("college2@gmail.com")
//                }
//            },
//            onComplete = {
//                when (it) {
//                    is Resource.Success -> {
//                        println("Success#")
//                    }
//                    is Resource.Error -> {
//                        println("Error: ${it.message}")
//                    }
//                }
//            }
//        )

//        RetrieveCollegeRepo.execute(
//            data = { mandatory ->
//                mandatory.invoke(19)
//            },
//            onComplete = {
//                when (it) {
//                    is Resource.Success -> {
//                        println("Success: ${it.data}")
//                    }
//                    is Resource.Error -> {
//                        println("Error: ${it.message}")
//                    }
//                }
//            }
//        )

//        AddGuestRepo.execute(
//            data = { mandatory, optional ->
//                mandatory.invoke("user5", 1, "user5@gmail.com", "123456aa")
//            },
//            onComplete = {
//                println("complete")
//            }
//        )

//        RetrieveBasicDepartmentsInfoRepo.execute(
////        RetrieveDepartmentsRepo.execute(
//            data = { mandatory ->
//                mandatory.invoke(1)
//            },
//            onComplete = {
//                println("complete")
//                it.data?.forEach {
//                    println(it.name)
//                }
//            }
//        )
                }
            }
        )
    }


//    RetrieveGendersRepo2.execute {
//    runBlocking {
//    withContext(Dispatchers.IO) {
//        RetrieveGendersRepo.execute {
//            when (it) {
//                is Resource.Success -> {
//                    println("sdads")
//                    println(it.data)
//                }
//                is Resource.Error -> {
//                    println(it.message)
//                }
//            }
//        }
//    }
//    }


//    runBlocking {
//        AddGuestRepo.execute(
//            data = { mandatory, optional ->
//                mandatory.invoke("m7devoo", 1, "m7devoo12345678@gmail.com", "m7devoo123")
//            },
//            onComplete = {
//                when (it) {
//                    is Resource.Success -> {
//                        println(it.data)
//                    }
//                    is Resource.Error -> {
//                        println(it.message)
//                    }
//                }
//            }
//        )
//    }

//    RetrieveCollegeRepo.execute(
//        data = {
//            it.invoke(2)
//        },
//        onComplete = {
//            println(it)
//        }
//    )

//    RetrieveCollegesRepo.execute {
//        println(it)
//    }

//    ModifyCollegeRepo.execute(
//        data = { mandatory, optional ->
//            mandatory.invoke(4)
//            optional.invoke {
//                arabicName("Ali")
//            }
//        },
//        onComplete = {
//            println("onComplete: $it")
//        }
//    )

//    DeleteCollegeRepo.execute(
//        data = { mandatory ->
//            mandatory.invoke(3)
//        },
//        onComplete = {
//            println("onComplete: $it")
//        }
//    )

//    RegisterRepo.execute("test1234", "test1234@gmail.com", "test1234") {
//        println("onComplete: $it")
//    }

//    LoginRepo.execute("test123@gmail.com", "test1234")
//    UserInfoRepo.execute {
//        println("info: $it")
//    }

//    LoginRepo.execute("test12345@gmail.com", "test12345")
//    UserInfoRepo.execute {  }


//    RetrieveCollegeRepo.execute(
//        data = { mandatory ->
//            mandatory.invoke(2)
//        },
//        onComplete = {
//            println("OnComplete: $it")
//        }
//    )

//    RetrieveCollegesRepo.execute(
//        onComplete = {
//            println("OnComplete: $it")
//        }
//    )

//    RetrieveCollegesRepo.execute {
//        println("OnComplete: $it")
//    }

//    RetrieveCollegeDepartmentsRepo.execute(
//        data = { mandatory ->
//               mandatory.invoke(1)
//        },
//        onComplete = {
//            println("OnComplete: $it")
//        }
//    )

//    SavePracticeExamQuestionAnswerRepo.execute(
//        data = { mandatory ->
//            mandatory.invoke(1, 1, answer = { trueFalse, multiChoice ->
//                trueFalse.invoke(true)
////                trueFalse.invoke(false)
////                multiChoice.invoke(5)
//            })
//        },
//        onComplete = {
//
//        }
//    )

//    AddPaperExamRepoTest.execute(
//        data = { mandatory, optional ->
//            mandatory.invoke(1, questionsTypes = {
//                it.invoke(1, 5, 2f)
//                it.invoke(2, 10, 1.5f)
//            })
//            optional.invoke {
//                specialNote("hello")
//            }
//        },
//        onComplete = {
//
//        }
//    )


//    ConfigureUniversityDataRepo.execute(
//        data = { mandatory, optional ->
//            mandatory.invoke("علي", "Ali", ByteArray(5))
//            optional.invoke {
//                email("ali@gmail.com")
////                getEmails()("5")
////                this phone1 4
//            }
//        },
//        onComplete = {
//
//        }
//    )

//    RegisterRepo.execute("test17", "test17@gmail.com", "test1234") {
//        println(it)
//    }
//    LoginRepo.execute("test@gmail.com", "test1234")
//    UserInfoRepo.execute {  }

//    StudentsRepo.execute {
//        it.forEach {
//            println(it)
//        }
//    }
}
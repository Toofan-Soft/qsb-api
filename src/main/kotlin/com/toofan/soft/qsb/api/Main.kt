package com.toofan.soft.qsb.api

import com.toofan.soft.qsb.api.repos.enums.RetrieveGendersRepo

suspend fun main(args: Array<String>) {
    println("Hello World!")

    println("Program arguments: ${args.joinToString()}")

//    LoginRepo.execute(
//        data = {
//            it.invoke("user@gmail.com", "123456kk")
//        },
//        onComplete = {
//            println("complete")
//        }
//    )

//    RetrieveGendersRepo2.execute {
//    runBlocking {
        RetrieveGendersRepo.execute {
            when (it) {
                is Resource.Success -> {
                    println("sdads")
                    println(it.data)
                }
                is Resource.Error -> {
                    println(it.message)
                }
            }
        }
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
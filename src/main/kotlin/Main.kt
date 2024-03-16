import com.toofan.soft.qsb.api.repos.practice_exam.SavePracticeExamQuestionAnswerRepo

fun main(args: Array<String>) {
    println("Hello World!")

    println("Program arguments: ${args.joinToString()}")

    SavePracticeExamQuestionAnswerRepo.execute(
        data = { mandatory ->
            mandatory.invoke(1, 1, answer = { trueFalse, multiChoice ->
                trueFalse.invoke(true)
//                trueFalse.invoke(false)
//                multiChoice.invoke(5)
            })
        },
        onComplete = {

        }
    )

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
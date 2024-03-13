import com.toofan.soft.qsb.api.repos.university.ConfigureUniversityDataRepo

fun main(args: Array<String>) {
    println("Hello World!")

    println("Program arguments: ${args.joinToString()}")


    ConfigureUniversityDataRepo.execute(
        data = { mandatory, optional ->
            mandatory.invoke("علي", "Ali")
            optional.invoke {
                email("ali@gmail.com")
//                getEmails()("5")
//                this phone1 4
            }
        },
        onComplete = {

        }
    )

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
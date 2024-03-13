import com.toofan.soft.qsb.api.repos.LoginRepo
import com.toofan.soft.qsb.api.repos.RegisterRepo
import com.toofan.soft.qsb.api.repos.StudentsRepo
import com.toofan.soft.qsb.api.repos.UserInfoRepo
import com.toofan.soft.qsb.api.repos.university.ConfigureUniversityDataRepo

fun main(args: Array<String>) {
    println("Hello World!")

    println("Program arguments: ${args.joinToString()}")


    ConfigureUniversityDataRepo.execute(
        data = { mandatory, optional ->
            mandatory.invoke("علي", "Ali")
            optional.invoke {
                email("ali@gmail.com")
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
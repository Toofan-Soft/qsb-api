package com.toofan.soft.qsb.api

import com.toofan.soft.qsb.api.image.fileToByteArray
import com.toofan.soft.qsb.api.repos.college.AddCollegeRepo
import com.toofan.soft.qsb.api.repos.college.RetrieveCollegeRepo
import com.toofan.soft.qsb.api.repos.department.RetrieveDepartmentRepo
import com.toofan.soft.qsb.api.repos.department_course.RetrieveDepartmentCourseRepo
import com.toofan.soft.qsb.api.repos.filter.RetrieveDepartmentLecturerCurrentCoursesRepo
import com.toofan.soft.qsb.api.repos.lecturer_online_exam.AddOnlineExamRepo
import com.toofan.soft.qsb.api.repos.lecturer_online_exam.RetrieveEditableOnlineExamRepo
import com.toofan.soft.qsb.api.repos.lecturer_online_exam.RetrieveOnlineExamFormQuestionsRepo
import com.toofan.soft.qsb.api.repos.lecturer_online_exam.RetrieveOnlineExamFormsRepo
import com.toofan.soft.qsb.api.repos.paper_exam.ModifyPaperExamRepo
import com.toofan.soft.qsb.api.repos.paper_exam.RetrievePaperExamFormsRepo
import com.toofan.soft.qsb.api.repos.practice_exam.RetrievePracticeExamQuestionsRepo
import com.toofan.soft.qsb.api.repos.practice_exam.RetrievePracticeExamRepo
import com.toofan.soft.qsb.api.repos.practice_exam.RetrievePracticeExamsAndroidRepo
import com.toofan.soft.qsb.api.repos.practice_exam.RetrievePracticeExamsRepo
import com.toofan.soft.qsb.api.repos.student.RetrieveStudentRepo
import com.toofan.soft.qsb.api.repos.user.LoginRepo
import com.toofan.soft.qsb.api.repos.user.RetrieveProfileRepo
import kotlinx.coroutines.runBlocking
import java.io.File
import java.time.LocalDateTime

private suspend fun retrievePractice() {
    RetrievePracticeExamRepo.execute(
        data = {
            it.invoke(
                23
            )
        },
        onComplete = {
            println("complete...")
            println(it.data)
        }
    )
}


private suspend fun retrieveLecturerOnlineForms() {
    RetrieveOnlineExamFormsRepo.execute(
        data = {
            it.invoke(
                109
            )
        },
        onComplete = {
            println("complete...")
            println(it.data)
        }
    )
}

private suspend fun retrieveDepartment() {
    RetrieveDepartmentRepo.execute(
        data = {
            it.invoke(
                10
            )
        },
        onComplete = {
            println("complete...")
            println(it.data)
        }
    )
}

private suspend fun modifyPaperExam() {
    ModifyPaperExamRepo.execute(
        data = { mandatory, optional ->
            mandatory.invoke(
                100
            )

//            Modify Paper Exam:
//            http://192.168.1.15:8000/api/paper-exam/modify:
//            request-parameters: id=100&type_id=2&form_name_method_id=2&special_note=Notes
//            server error 500

            optional.invoke {
                typeId(2)
                formNameMethodId(2)
                specialNote("Notes")
            }
        },
        onComplete = {
            println("complete...")
            println(it.data)
        }
    )
}

private suspend fun retrievePaperForms() {
    RetrievePaperExamFormsRepo.execute(
        data = {
            it.invoke(
                110
            )
        },
        onComplete = {
            println("complete...")
            println(it.data)
        }
    )
}


private suspend fun retrieveLecturerExamFormQuestions() {
    RetrieveOnlineExamFormQuestionsRepo.execute(
        data = {
            it.invoke(
                83
            )
        },
        onComplete = {
            println("complete...")
            println(it.data)
        }
    )
}

private suspend fun retrievePaperExamFormQuestions() {
    RetrieveOnlineExamFormQuestionsRepo.execute(
        data = {
            it.invoke(
                85
            )
        },
        onComplete = {
            println("complete...")
            println(it.data)
        }
    )
}

private suspend fun retrieveDepartmentCourse() {
    RetrieveDepartmentCourseRepo.execute(
        data = {
            it.invoke(
                10
            )
        },
        onComplete = {
            println("complete...")
            println(it.data)
        }
    )
}

private suspend fun retrieveProfile() {
    RetrieveProfileRepo.execute(
        onComplete = {
            println("complete...")
            println(it.data)
        }
    )
}

private suspend fun retrieveDepartmentLecturerCurrentCourses() {
    RetrieveDepartmentLecturerCurrentCoursesRepo.execute(
        data = {
            it.invoke(1)
        },
        onComplete = {
            println("complete...")
            println(it.data)
        }
    )
}

private suspend fun retrieveEditableOnlineExam() {
    RetrieveEditableOnlineExamRepo.execute(
        data = {
            it.invoke(8)
        },
        onComplete = {
            println("complete...")
            println(it.data)
        }
    )
}

private suspend fun retrievePracticeExamsAndroid() {
    RetrievePracticeExamsAndroidRepo.execute(
        data = {
//            it.invoke()
        },
        onComplete = {
            println("complete...")
            println(it.data)
        }
    )
}

private suspend fun retrievePracticeExamQuestions() {
    RetrievePracticeExamQuestionsRepo.execute(
        data = {
            it.invoke(1)
        },
        onComplete = {
            println("complete...")
            println(it.data)

            it.data?.let {
                it.filter { it.getChoices().any { it.isSelected == true } }.let {
                    println()
                    println()
                    println(".....................")
                    println(it)
                }
            }
        }
    )
}

private suspend fun retrievePracticeExams() {
    RetrievePracticeExamsRepo.execute(
        data = { mandatory, optional ->
            mandatory.invoke(2)
            optional.invoke {
                statusId(0)
            }
        },
        onComplete = {
            println("complete...")
            println(it.data)
        }
    )
}


private suspend fun addOnlineExam() {
    AddOnlineExamRepo.execute(
        data = { mandatory, optional ->
            mandatory.invoke(
                2,
                0,
                1,
//                1720126800000,
//                1721768400000,
                LocalDateTime.now(),
                120,
//                1720126800000,
//                1720126800000,
//                1723582800000,
//                1721768400000,
                LocalDateTime.now(),
                LocalDateTime.now(),
                0,
//                1,
                2,
                1,
//                1,
//                1,
//                0,
                {
                    it.invoke(1, 20, 10.0f)
                },
                listOf(3)
            )

            optional.invoke {
//                formConfigurationMethodId(1)
//                formNameMethodId(1)
            }
        },
        onComplete = {
            println("complete...")
            println(it)
        }
    )
}

private suspend fun addCollege() {
    val file = File("E:\\f\\ToofanSoft\\QsB\\coding\\github\\qsb-api\\src\\main\\kotlin\\com\\toofan\\soft\\qsb\\api\\image/home.png") // Replace with your image file path
    val logo = fileToByteArray(file)

    AddCollegeRepo.execute(
        data = { mandatory, optional ->
            mandatory.invoke(
                "كلية 4",
                "college 4"
            )

            optional.invoke {
                phone(777777777)
                email("example@gmail.com")
                description("desc...")
                logo(logo)
            }
        },
        onComplete = {
            println("complete...")
            println(it.data)
        }
    )
}

private suspend fun retrieveCollege() {
    RetrieveCollegeRepo.execute(
        data = {
            it.invoke(6)
        },
        onComplete = {
            println("complete...")
            println(it.data)
        }
    )
}



private suspend fun retrieveStudent() {
    RetrieveStudentRepo.execute(
        data = {
            it.invoke(6)
        },
        onComplete = {
            println("complete...")
            println(it.data)
        }
    )
}

fun main() {
    runBlocking {
        Api.init("192.168.1.15")
        LoginRepo.execute(
            data = {
//                it.invoke("llll123456@gmail.com", "llll123456")
//                it.invoke("ssss1234@gmail.com", "ssss1234")
                it.invoke("fadi@gmail.com", "fadi1234")
            },
            onComplete = {
                println("complete")
                runBlocking {
//                    retrievePractice()
//                    retrieveLecturerOnlineForms()
//                    retrieveDepartment()
//                    modifyPaperExam()
//                    retrievePaperForms()
//                    retrieveLecturerExamFormQuestions()
//                    retrievePaperExamFormQuestions()
//                    retrieveDepartmentCourse()
//                    retrieveProfile()
//                    retrieveDepartmentLecturerCurrentCourses()

//                    addOnlineExam()
//                    retrieveEditableOnlineExam()

//                    retrievePracticeExamsAndroid()
//                    retrievePracticeExamQuestions()
//                    retrievePracticeExams()

//                    addCollege()
//                    retrieveCollege()

                    retrieveStudent()
                }
            }
        )
    }
}
package com.toofan.soft.qsb.api

import com.toofan.soft.qsb.api.repos.department.RetrieveDepartmentRepo
import com.toofan.soft.qsb.api.repos.department_course.RetrieveDepartmentCourseRepo
import com.toofan.soft.qsb.api.repos.filter.RetrieveDepartmentLecturerCurrentCoursesRepo
import com.toofan.soft.qsb.api.repos.lecturer_online_exam.RetrieveOnlineExamFormQuestionsRepo
import com.toofan.soft.qsb.api.repos.lecturer_online_exam.RetrieveOnlineExamFormsRepo
import com.toofan.soft.qsb.api.repos.paper_exam.ModifyPaperExamRepo
import com.toofan.soft.qsb.api.repos.paper_exam.RetrievePaperExamFormsRepo
import com.toofan.soft.qsb.api.repos.practice_exam.RetrievePracticeExamRepo
import com.toofan.soft.qsb.api.repos.user.LoginRepo
import com.toofan.soft.qsb.api.repos.user.RetrieveProfileRepo
import kotlinx.coroutines.runBlocking

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

fun main() {
    runBlocking {
        Api.init("192.168.1.15")
        LoginRepo.execute(
            data = {
                it.invoke("llll123456@gmail.com", "llll123456")
//                it.invoke("ssss1234@gmail.com", "ssss1234")
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
                    retrieveDepartmentLecturerCurrentCourses()
                }
            }
        )
    }
}
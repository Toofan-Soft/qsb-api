package com.toofan.soft.qsb.api

import com.toofan.soft.qsb.api.services.changeKey

private object Constant {
    private const val URL = "http://localhost:8000/"
    const val HOME = URL + "api"
}

object Api {
    internal var HOME = Constant.HOME
    internal var IMAGES = Constant.HOME

    @JvmStatic
    fun init(ip: String) {
        HOME = "http://$ip:8000/api"
        IMAGES = "http://$ip:8000/images"
    }

    @JvmStatic
    fun init(ip: String, type: Type) {
        HOME = "http://$ip:8000/api"
        IMAGES = "http://$ip:8000/images"

        changeKey(type)
    }

    enum class Type {
        FADI,
        M7DEVOO;
    }
}

sealed class Route(
    private val _path: String,
    private val _method: Method,
    private val _isAuthorized: Boolean
) {
//    internal val url get() = "${Constant.HOME}/$_path"
    internal val url get() = "${Api.HOME}/$_path"
    internal val method get() = _method.value
    internal val isAuthorized get() = _isAuthorized

    object Test: Route("test", Method.POST, false)


    sealed class User(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("user/$name", method, isAuthorized) {
        // check for method
        object Login: User("login", Method.POST, false)
        object Verify: User("verify", Method.POST, false)
        object RequestAccountRecovery: User("request-account-recovery", Method.PUT, false)
        object ResendCode: User("resend-code", Method.PUT, false)
        object VerifyAccountAfterRecovery: User("verify-account-after-recovery", Method.POST, false)
        object ChangePasswordAfterAccountRecovery: User("change-password-after-account-recovery", Method.PUT)
        object RetrieveProfile: User("retrieve-profile", Method.GET)
        object ChangePassword: User("change-password", Method.PUT)
        object ChangeLanguage: User("change-language", Method.PUT)
        object Logout: User("logout", Method.POST)
    }

    sealed class University(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("university/$name", method, isAuthorized) {
        object Configure: University("configure", Method.POST)
        object Modify: University("modify", Method.PUT)
        object Retrieve: University("retrieve", Method.GET)
        object RetrieveBasicInfo: University("retrieve-basic-info", Method.GET)
    }

    sealed class College(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("college/$name", method, isAuthorized) {
        object Add: College("add", Method.POST)
        object Modify: College("modify", Method.PUT)
        object Delete: College("delete", Method.DELETE)
        object Retrieve: College("retrieve", Method.GET)
        object RetrieveEditable: College("retrieve-editable", Method.GET)
        object RetrieveList: College("retrieve-list", Method.GET)
        object RetrieveBasicInfoList: College("retrieve-basic-info-list", Method.GET)
    }

    sealed class Department(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("department/$name", method, isAuthorized) {
        object Add: Department("add", Method.POST)
        object Modify: Department("modify", Method.PUT)
        object Delete: Department("delete", Method.DELETE)
        object Retrieve: Department("retrieve", Method.GET)
        object RetrieveEditable: Department("retrieve-editable", Method.GET)
        object RetrieveList: Department("retrieve-list", Method.GET)
        object RetrieveBasicInfoList: Department("retrieve-basic-info-list", Method.GET)
    }

    sealed class Course(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("course/$name", method, isAuthorized) {
        object Add: Course("add", Method.POST)
        object Modify: Course("modify", Method.PUT)
        object Retrieve: Course("retrieve", Method.GET)
        object Delete: Course("delete", Method.DELETE)
        object RetrieveEditable: Course("retrieve-editable", Method.GET)
        object RetrieveList: Course("retrieve-list", Method.GET)
    }
    
    sealed class CoursePart(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("course-part/$name", method, isAuthorized) {
        object Add: CoursePart("add", Method.POST)
        object Modify: CoursePart("modify", Method.PUT)
        object Delete: CoursePart("delete", Method.DELETE)
        object RetrieveEditable: CoursePart("retrieve-editable", Method.GET)
        object RetrieveList: CoursePart("retrieve-list", Method.GET)
    }

    sealed class Chapter(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("chapter/$name", method, isAuthorized) {
        object Add: Chapter("add", Method.POST)
        object Modify: Chapter("modify", Method.PUT)
        object Delete: Chapter("delete", Method.DELETE)
        object Retrieve: Chapter("retrieve", Method.GET)
        object RetrieveEditable: Chapter("retrieve-editable", Method.GET)
        object RetrieveList: Chapter("retrieve-list", Method.GET)
        object RetrieveAvailableList: Chapter("retrieve-available-list", Method.GET)
        object RetrieveDescription: Chapter("retrieve-description", Method.GET)
    }

    sealed class Topic(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("topic/$name", method, isAuthorized) {
        object Add: Topic("add", Method.POST)
        object Modify: Topic("modify", Method.PUT)
        object Delete: Topic("delete", Method.DELETE)
        object Retrieve: Topic("retrieve", Method.GET)
        object RetrieveEditable: Topic("retrieve-editable", Method.GET)
        object RetrieveList: Topic("retrieve-list", Method.GET)
        object RetrieveAvailableList: Topic("retrieve-available-list", Method.GET)
        object RetrieveDescription: Topic("retrieve-description", Method.GET)
    }

    sealed class Question(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("question/$name", method, isAuthorized) {
        object Add: Question("add", Method.POST)
        object Modify: Question("modify", Method.PUT)
        object Delete: Question("delete", Method.DELETE)
        object Retrieve: Question("retrieve", Method.GET)
        object RetrieveEditable: Question("retrieve-editable", Method.GET)
        object RetrieveList: Question("retrieve-list", Method.GET)
        object Submit: Question("submit", Method.PUT)
        object WithdrawSubmit: Question("withdraw-submit", Method.PUT)
        object Accept: Question("accept", Method.PUT)
        object Reject: Question("reject", Method.PUT)
    }

    sealed class QuestionChoice(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("question-choice/$name", method, isAuthorized) {
        object Add: QuestionChoice("add", Method.POST)
        object Modify: QuestionChoice("modify", Method.PUT)
        object Delete: QuestionChoice("delete", Method.DELETE)
        object RetrieveEditable: QuestionChoice("retrieve-editable", Method.GET)
    }

    sealed class DepartmentCourse(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("department-course/$name", method, isAuthorized) {
        object Add: DepartmentCourse("add", Method.POST)
        object Modify: DepartmentCourse("modify", Method.PUT)
        object Delete: DepartmentCourse("delete", Method.DELETE)
        object Retrieve: DepartmentCourse("retrieve", Method.GET)
        object RetrieveEditable: DepartmentCourse("retrieve-editable", Method.GET)
        object RetrieveList: DepartmentCourse("retrieve-list", Method.GET)
        object RetrieveCourseDepartmentList: DepartmentCourse("retrieve-course-department-list", Method.GET)
        object RetrieveLevelCourseList: DepartmentCourse("retrieve-level-course-list", Method.GET)
    }

    sealed class DepartmentCoursePart(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("department-course-part/$name", method, isAuthorized) {
        object Add: DepartmentCoursePart("add", Method.POST)
        object Modify: DepartmentCoursePart("modify", Method.PUT)
        object Delete: DepartmentCoursePart("delete", Method.DELETE)
        object RetrieveEditable: DepartmentCoursePart("retrieve-editable", Method.GET)
    }

    sealed class DepartmentCoursePartChapterAndTopic(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("department-course-part-chapter-and-topic/$name", method, isAuthorized) {
        object ModifyTopicList: DepartmentCoursePartChapterAndTopic("modify-topic-list", Method.POST)
        object RetrieveChapterList: DepartmentCoursePartChapterAndTopic("retrieve-chapter-list", Method.GET)
        object RetrieveTopicList: DepartmentCoursePartChapterAndTopic("retrieve-topic-list", Method.GET)
        object RetrieveEditableChapterList: DepartmentCoursePartChapterAndTopic("retrieve-editable-chapter-list", Method.GET)
        object RetrieveEditableTopicList: DepartmentCoursePartChapterAndTopic("retrieve-editable-topic-list", Method.GET)
    }

    sealed class Employee(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("employee/$name", method, isAuthorized) {
        object Add: Employee("add", Method.POST)
        object Modify: Employee("modify", Method.PUT)
        object Delete: Employee("delete", Method.DELETE)
        object Retrieve: Employee("retrieve", Method.GET)
        object RetrieveEditable: Employee("retrieve-editable", Method.GET)
        object RetrieveList: Employee("retrieve-list", Method.GET)
    }

    sealed class CourseLecturer(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("course-lecturer/$name", method, isAuthorized) {
        object Add: CourseLecturer("add", Method.POST)
        object Delete: CourseLecturer("delete", Method.DELETE)
        object Retrieve: CourseLecturer("retrieve", Method.GET)
        object RetrieveList: CourseLecturer("retrieve-list", Method.GET)
        object RetrieveLecturerCourseList: CourseLecturer("retrieve-lecturer-course-list", Method.GET)
    }

    sealed class Student(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("student/$name", method, isAuthorized) {
        object Add: Student("add", Method.POST)
        object Modify: Student("modify", Method.PUT)
        object Delete: Student("delete", Method.DELETE)
        object Retrieve: Student("retrieve", Method.GET)
        object RetrieveEditable: Student("retrieve-editable", Method.GET)
        object RetrieveList: Student("retrieve-list", Method.GET)
    }

    sealed class CourseStudent(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("course-student/$name", method, isAuthorized) {
        object AddList: CourseStudent("add-list", Method.POST)
        object Delete: CourseStudent("delete", Method.DELETE)
        object RetrieveList: CourseStudent("retrieve-list", Method.GET)
        object RetrieveUnlinkList: CourseStudent("retrieve-unlink-list", Method.GET)
        object Pass: CourseStudent("pass", Method.PUT)
        object Suspend: CourseStudent("suspend", Method.PUT)
        object Unsuspend: CourseStudent("unsuspend", Method.PUT)
    }

    sealed class Guest(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("guest/$name", method, isAuthorized) {
        object Add: Guest("add", Method.POST)
        object Modify: Guest("modify", Method.PUT)
        object RetrieveEditable: Guest("retrieve-editable", Method.GET)
    }

    sealed class UserManagement(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("user-management/$name", method, isAuthorized) {
        object Add: UserManagement("add", Method.POST)
        object ModifyRoleList: UserManagement("modify-role-list", Method.PUT)
        object Delete: UserManagement("delete", Method.DELETE)
        object Retrieve: UserManagement("retrieve", Method.GET)
        object RetrieveList: UserManagement("retrieve-list", Method.GET)
        object RetrieveOwnerRoleList: UserManagement("retrieve-owner-role-list", Method.GET)
        object ChangeStatus: UserManagement("change-status", Method.PUT)
    }

    sealed class LecturerOnlineExam(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("lecturer-online-exam/$name", method, isAuthorized) {
        object Add: LecturerOnlineExam("add", Method.POST)
        object Modify: LecturerOnlineExam("modify", Method.PUT)
        object Delete: LecturerOnlineExam("delete", Method.DELETE)
        object Retrieve: LecturerOnlineExam("retrieve", Method.GET)
        object RetrieveEditable: LecturerOnlineExam("retrieve-editable", Method.GET)
        object RetrieveList: LecturerOnlineExam("retrieve-list", Method.GET)
        object RetrieveAndroidList: LecturerOnlineExam("retrieve-android-list", Method.GET)
        object RetrieveChapterList: LecturerOnlineExam("retrieve-chapter-list", Method.GET)
        object RetrieveChapterTopicList: LecturerOnlineExam("retrieve-chapter-topic-list", Method.GET)
        object RetrieveFormList: LecturerOnlineExam("retrieve-form-list", Method.GET)
        object RetrieveFormQuestionList: LecturerOnlineExam("retrieve-form-question-list", Method.GET)
        object ChangeStatus: LecturerOnlineExam("change-status", Method.PUT)
    }

    sealed class StudentOnlineExam(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("student-online-exam/$name", method, isAuthorized) {
        object SaveQuestionAnswer: StudentOnlineExam("save-question-answer", Method.POST)
        // check if its method post
        object Finish: StudentOnlineExam("finish", Method.PUT)
        // check if its method put
        object Retrieve: StudentOnlineExam("retrieve", Method.GET)
        object RetrieveList: StudentOnlineExam("retrieve-list", Method.GET)
        object RetrieveQuestionList: StudentOnlineExam("retrieve-question-list", Method.GET)
    }

    sealed class ProctorOnlineExam(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("proctor-online-exam/$name", method, isAuthorized) {
        object StartStudent: ProctorOnlineExam("start-student", Method.POST)
        // check for its method
        object SuspendStudent: ProctorOnlineExam("suspend-student", Method.PUT)
        // check for its method
        object ContinueStudent: ProctorOnlineExam("continue-student", Method.PUT)
        // check for its method
        object FinishStudent: ProctorOnlineExam("finish-student", Method.PUT)
        object Finish: ProctorOnlineExam("finish", Method.PUT)
        // check for its method
        object Retrieve: ProctorOnlineExam("retrieve", Method.GET)
        object RetrieveList: ProctorOnlineExam("retrieve-list", Method.GET)
        object RetrieveStudentList: ProctorOnlineExam("retrieve-student-list", Method.GET)
        object RefreshStudentList: ProctorOnlineExam("refresh-student-list", Method.GET)
    }

    sealed class PaperExam(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("paper-exam/$name", method, isAuthorized) {
        object Add: PaperExam("add", Method.POST)
        object Modify: PaperExam("modify", Method.PUT)
        object Delete: PaperExam("delete", Method.DELETE)
        object Retrieve: PaperExam("retrieve", Method.GET)
        object RetrieveEditable: PaperExam("retrieve-editable", Method.GET)
        object RetrieveList: PaperExam("retrieve-list", Method.GET)
        object RetrieveAndroidList: PaperExam("retrieve-android-list", Method.GET)
        object RetrieveChapterList: PaperExam("retrieve-chapter-list", Method.GET)
        object RetrieveChapterTopicList: PaperExam("retrieve-chapter-topic-list", Method.GET)
        object RetrieveFormList: PaperExam("retrieve-form-list", Method.GET)
        object RetrieveFormQuestionList: PaperExam("retrieve-form-question-list", Method.GET)
        object Export: PaperExam("export", Method.GET)
        // check for its method
    }

    sealed class PracticeOnlineExam(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("practice-exam/$name", method, isAuthorized) {
        object Add: PracticeOnlineExam("add", Method.POST)
        object Modify: PracticeOnlineExam("modify", Method.PUT)
        object Delete: PracticeOnlineExam("delete", Method.DELETE)
        object Retrieve: PracticeOnlineExam("retrieve", Method.GET)
        object RetrieveEditable: PracticeOnlineExam("retrieve-editable", Method.GET)
        object RetrieveResult: PracticeOnlineExam("retrieve-result", Method.GET)
        object RetrieveList: PracticeOnlineExam("retrieve-list", Method.GET)
        object RetrieveAndroidList: PracticeOnlineExam("retrieve-android-list", Method.GET)
        object RetrieveQuestionList: PracticeOnlineExam("retrieve-question-list", Method.GET)
        object SaveQuestionAnswer: PracticeOnlineExam("save-question-answer", Method.PUT)
        object Start: PracticeOnlineExam("start", Method.PUT)
        object Suspend: PracticeOnlineExam("suspend", Method.PUT)
        object Continue: PracticeOnlineExam("continue", Method.PUT)
        object Finish: PracticeOnlineExam("finish", Method.PUT)
    }

    sealed class FavoriteQuestion(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("favorite-question/$name", method, isAuthorized) {
        object Add: FavoriteQuestion("add", Method.POST)
        object Delete: FavoriteQuestion("delete", Method.DELETE)
        object Retrieve: FavoriteQuestion("retrieve", Method.GET)
        object Check: FavoriteQuestion("check", Method.GET)
        object RetrieveList: FavoriteQuestion("retrieve-list", Method.GET)
    }

    sealed class Enum(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("enum/$name", method, isAuthorized) {
        object RetrieveCoursePartStatusList: Enum("retrieve-course-part-status-list", Method.GET)
        object RetrieveChapterStatusList: Enum("retrieve-chapter-status-list", Method.GET)
        object RetrieveLevelsCountList: Enum("retrieve-levels-count-list", Method.GET)
        object RetrieveCoursePartList: Enum("retrieve-course-part-list", Method.GET)
        object RetrieveLanguageList: Enum("retrieve-language-list", Method.GET)
        object RetrieveDifficultyLevelList: Enum("retrieve-difficulty-level-list", Method.GET)
        object RetrieveQuestionTypeList: Enum("retrieve-question-type-list", Method.GET)
        object RetrieveQuestionStatusList: Enum("retrieve-question-status-list", Method.GET)
        object RetrieveAcceptanceStatusList: Enum("retrieve-acceptance-status-list", Method.GET)
        object RetrieveAccessibilityStatusList: Enum("retrieve-accessibility-status-list", Method.GET)
        object RetrieveSemesterList: Enum("retrieve-semester-list", Method.GET)
        object RetrieveJobTypeList: Enum("retrieve-job-type-list", Method.GET)
        object RetrieveQualificationList: Enum("retrieve-qualification-list", Method.GET)
        object RetrieveGenderList: Enum("retrieve-gender-list", Method.GET)
        object RetrieveRegisterGenderList: Enum("retrieve-register-gender-list", Method.GET, false)
        object RetrieveCourseStudentStatusList: Enum("retrieve-course-student-status-list", Method.GET)
        object RetrieveOwnerTypeList: Enum("retrieve-owner-type-list", Method.GET)
        object RetrieveUserStatusList: Enum("retrieve-user-status-list", Method.GET)
        object RetrieveConductMethodList: Enum("retrieve-conduct-method-list", Method.GET)
        object RetrieveExamTypeList: Enum("retrieve-exam-type-list", Method.GET)
        object RetrieveFormConfigurationMethodList: Enum("retrieve-form-configuration-method-list", Method.GET)
        object RetrieveFormNameMethodList: Enum("retrieve-form-name-method-list", Method.GET)
        object RetrieveOnlineExamStatusList: Enum("retrieve-online-exam-status-list", Method.GET)
        object RetrievePracticeExamStatusList: Enum("retrieve-practice-exam-status-list", Method.GET)
        object RetrieveStudentOnlineExamStatusList: Enum("retrieve-student-online-exam-status-list", Method.GET)
        object RetrieveOnlineExamTakingStatusList: Enum("retrieve-online-exam-taking-status-list", Method.GET)
    }

    sealed class Filter(
        name: String,
        method: Method,
        isAuthorized: Boolean = true
    ): Route("filter/$name", method, isAuthorized) {
        object RetrieveCourseList: Filter("retrieve-course-list", Method.GET)
        object RetrieveCoursePartList: Filter("retrieve-course-part-list", Method.GET)
        object RetrieveChapterList: Filter("retrieve-chapter-list", Method.GET)
        object RetrieveTopicList: Filter("retrieve-topic-list", Method.GET)
        object RetrieveCollegeList: Filter("retrieve-college-list", Method.GET)
        object RetrieveLecturerCollegeList: Filter("retrieve-lecturer-college-list", Method.GET)
        object RetrieveLecturerCurrentCollegeList: Filter("retrieve-lecturer-current-college-list", Method.GET)
        object RetrieveDepartmentList: Filter("retrieve-department-list", Method.GET)
        object RetrieveLecturerDepartmentList: Filter("retrieve-lecturer-department-list", Method.GET)
        object RetrieveLecturerCurrentDepartmentList: Filter("retrieve-lecturer-current-department-list", Method.GET)
        object RetrieveDepartmentLevelList: Filter("retrieve-department-level-list", Method.GET)
        object RetrieveDepartmentCourseList: Filter("retrieve-department-course-list", Method.GET)
        object RetrieveDepartmentLevelCourseList: Filter("retrieve-department-level-course-list", Method.GET)
        object RetrieveDepartmentLevelSemesterCourseList: Filter("retrieve-department-level-semester-course-list", Method.GET)
        object RetrieveDepartmentCoursePartList: Filter("retrieve-department-course-part-list", Method.GET)
        object RetrieveDepartmentLecturerCourseList: Filter("retrieve-department-lecturer-course-list", Method.GET)
        object RetrieveDepartmentLecturerCurrentCourseList: Filter("retrieve-department-lecturer-current-course-list", Method.GET)
        object RetrieveDepartmentLecturerCoursePartList: Filter("retrieve-department-lecturer-course-part-list", Method.GET)
        object RetrieveDepartmentLecturerCurrentCoursePartList: Filter("retrieve-department-lecturer-current-course-part-list", Method.GET)
        object RetrieveEmployeeList: Filter("retrieve-employee-list", Method.GET)
        object RetrieveLecturerList: Filter("retrieve-lecturer-list", Method.GET)
        object RetrieveEmployeeOfJobList: Filter("retrieve-employee-of-job-list", Method.GET)
        object RetrieveAcademicYearList: Filter("retrieve-academic-year-list", Method.GET)
        object RetrieveOwnerList: Filter("retrieve-owner-list", Method.GET)
        object RetrieveNonOwnerEmployeeList: Filter("retrieve-non-owner-employee-list", Method.GET)
        object RetrieveNonOwnerStudentList: Filter("retrieve-non-owner-student-list", Method.GET)
        object RetrieveRoleList: Filter("retrieve-role-list", Method.GET)
        object RetrieveProctorList: Filter("retrieve-proctor-list", Method.GET)
    }
}

internal enum class Method(internal val value: String) {
    POST("POST"),
    GET("GET"),
    PUT("PUT"),
    DELETE("DELETE");
}

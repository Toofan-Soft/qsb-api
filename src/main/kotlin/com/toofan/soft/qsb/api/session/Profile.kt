package com.toofan.soft.qsb.api.session

sealed interface Profile {
    data class Guest(
        val name: String,
        val email: String,
        val genderName: String,
        val phone: Long?,
        val imageUrl: String?
    ) : Profile

    data class Employee (
        val arabicName: String,
        val englishName: String,
        val email: String,
        val genderName: String,
        val qualificationName: String,
        val jobTypeName: String,
        val phone: Long?,
        val imageUrl: String?,
        val specialization: String?
    ) : Profile

    data class Student(
        val arabicName: String,
        val englishName: String,
        val email: String,
        val genderName: String,
        val phone: Long?,
        val imageUrl: String?,
        val birthdate: Long?
    ) : Profile
}
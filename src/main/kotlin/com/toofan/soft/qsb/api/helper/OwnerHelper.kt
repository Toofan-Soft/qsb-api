package com.toofan.soft.qsb.api.helper

object OwnerHelper {
    @JvmStatic
    fun getOwnerType(id: Int): OwnerType? {
        return OwnerType.of(id)
    }

    enum class OwnerType(
        private val id: Int
    ) {
        STUDENT(1),
        EMPLOYEE(2);

        companion object {
            fun of(id: Int): OwnerType? {
                return values().find { it.id == id }
            }
        }
    }
}
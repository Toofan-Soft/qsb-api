package com.toofan.soft.qsb.api.image

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

fun main() {
//    val file = File("E:\\f\\ToofanSoft\\QsB\\coding\\github\\qsb-api\\src\\main\\kotlin\\com\\toofan\\soft\\qsb\\api\\image/1713466605.jpg") // Replace with your image file path
    val file = File("E:\\f\\ToofanSoft\\QsB\\coding\\github\\qsb-api\\src\\main\\kotlin\\com\\toofan\\soft\\qsb\\api\\image/home.png") // Replace with your image file path
//    val file = File("/1713466605.jpg") // Replace with your image file path
    val byteArray = fileToByteArray(file)

    // Printing the size of the byte array
    println("Byte array size: ${byteArray.size} bytes")
    println()

    println(byteArray.toList())
    println()

    println(byteArray.toList().min())
    println(byteArray.toList().max())
}

fun fileToByteArray(file: File): ByteArray {
    val inputStream = FileInputStream(file)
    val outputStream = ByteArrayOutputStream()

    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } != -1) {
        outputStream.write(buffer, 0, length)
    }

    inputStream.close()
    outputStream.close()

    return outputStream.toByteArray()
}

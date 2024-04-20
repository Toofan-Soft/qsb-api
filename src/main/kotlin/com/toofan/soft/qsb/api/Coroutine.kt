package com.toofan.soft.qsb.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal object Coroutine {
    suspend fun launch(
        block: suspend CoroutineScope.() -> Unit
    ) {
        val isDesktop = isDesktop()
        println("isDesktop: $isDesktop")

//        if (isDesktop()) CoroutineScope(Dispatchers.IO).launch(block = block)
        if (isDesktop) CoroutineScope(Dispatchers.IO).launch(block = block)
        else withContext(Dispatchers.IO, block)
    }

    private fun isDesktop(): Boolean {
        val os = System.getProperty("os.name")
        println("os: $os")

        return System.getProperty("os.name").lowercase().contains("windows") ||
                System.getProperty("os.name").lowercase().contains("mac") ||
                System.getProperty("os.name").lowercase().contains("linux")
    }
}

fun main() {
}

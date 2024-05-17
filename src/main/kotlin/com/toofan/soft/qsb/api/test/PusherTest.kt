package com.toofan.soft.qsb.api.test

import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import com.toofan.soft.qsb.api.repos.college.ModifyCollegeRepo
import kotlinx.coroutines.runBlocking

fun main() {
    listen()
    modify()
}

fun modify() {
//    CoroutineScope(Dispatchers.IO).launch {
    runBlocking {
        ModifyCollegeRepo.execute(
            data = { mandatory, optional ->
                mandatory.invoke(1)
                optional.invoke {
//                arabicName("علي")
                    englishName("Ali5")
                }
            },
            onComplete = {
                println("Complete")
            }
        )
    }
}

private fun listen() {
    val options = PusherOptions()
    options.setCluster("ap2");

    val pusher = Pusher("de8d63746cfbf321b300", options)

    pusher.connect(object : ConnectionEventListener {
        override fun onConnectionStateChange(change: ConnectionStateChange) {
            println("Pusher: State changed from ${change.previousState} to ${change.currentState}")
        }

        override fun onError(
            message: String,
            code: String,
            e: Exception
        ) {
            println("Pusher: There was a problem connecting! code ($code), message ($message), exception($e)")
        }
    }, ConnectionState.ALL)

    val channel = pusher.subscribe("qb_server")
    channel.bind("college.updated") { event ->
        println("Pusher: Received event with data: $event")

        println("Pusher.Data: ${event.data}")
    }
}
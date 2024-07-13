//package com.toofan.soft.qsb.api.test
//
//import com.pusher.client.Pusher
//import com.pusher.client.PusherOptions
//import com.pusher.client.connection.ConnectionEventListener
//import com.pusher.client.connection.ConnectionState
//import com.pusher.client.connection.ConnectionStateChange
//import com.toofan.soft.qsb.api.Api
//import com.toofan.soft.qsb.api.repos.college.ModifyCollegeRepo
//import com.toofan.soft.qsb.api.repos.user.LoginRepo
//import kotlinx.coroutines.runBlocking
//
//fun main() {
//    listen()
//    login()
////    modify()
//}
//
//fun modify() {
////    CoroutineScope(Dispatchers.IO).launch {
//    runBlocking {
//        ModifyCollegeRepo.execute(
//            data = { mandatory, optional ->
//                mandatory.invoke(1)
//                optional.invoke {
////                arabicName("علي")
//                    englishName("Ali5")
//                }
//            },
//            onComplete = {
//                println("Complete")
//            }
//        )
//    }
//}
//
//fun login() {
//    runBlocking {
//        Api.init("192.168.1.15")
//        LoginRepo.execute(
//            data = {
//                it.invoke("m7devoo.99@gmail.com", "m7devoo.99")
//            },
//            onComplete = {
//                println("Complete")
////                println(Session.token)
//            }
//        )
//    }
//}
//
//private fun listen() {
//    val options = PusherOptions()
//    options.setCluster("ap2");
//
//    val pusher = Pusher("8121ea8a2d9d2a6055c9", options)
//
//    pusher.connect(object : ConnectionEventListener {
//        override fun onConnectionStateChange(change: ConnectionStateChange) {
//            println("Pusher: State changed from ${change.previousState} to ${change.currentState}")
//        }
//
//        override fun onError(
//            message: String,
//            code: String,
//            e: Exception
//        ) {
//            println("Pusher: There was a problem connecting! code ($code), message ($message), exception($e)")
//        }
//    }, ConnectionState.ALL)
//
//    val channel = pusher.subscribe("qb_server")
//    channel.bind("college.updated") { event ->
//        println("Pusher: Received event with data: $event")
//
//        println("Pusher.Data: ${event.data}")
//    }
//}
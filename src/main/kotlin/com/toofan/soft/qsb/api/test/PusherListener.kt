package com.toofan.soft.qsb.api.test

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import com.toofan.soft.qsb.api.Api
import com.toofan.soft.qsb.api.Field
import com.toofan.soft.qsb.api.IResponse
import com.toofan.soft.qsb.api.session.Session
import java.time.LocalTime

fun changeKey(type: Api.Type) {
    PusherConstant.API_KEY = when (type) {
        Api.Type.M7DEVOO -> "5c2ed29f204a9a3539c2"
        Api.Type.FADI -> "8121ea8a2d9d2a6055c9"
    }
}

private object PusherConstant {
//    const val API_KEY = "8121ea8a2d9d2a6055c9"
    var API_KEY = "8121ea8a2d9d2a6055c9"
    const val CHANNEL = "qb_server"
    const val CLUSTER = "ap2"

    val STUDENT_EVENT = "student.refresh.${Session.uid}"
    val PROCTOR_EVENT = "proctor.refresh.${Session.uid}"
}

internal object StudentPusherListener {
    fun addListener(
        onComplete: (Data) -> Unit
    ) {
        Pusher(
            PusherConstant.API_KEY,
            PusherOptions()
                .setCluster(PusherConstant.CLUSTER)
        ).also {
            it.connect(
                object : ConnectionEventListener {
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
                },
                ConnectionState.ALL
            )

            it.subscribe(PusherConstant.CHANNEL)
                .bind(PusherConstant.STUDENT_EVENT) { event ->
                    println("Pusher: Received event with data: $event")
                    println("Pusher.Data: ${event.data}")

                    val data = Gson().fromJson(event.data, JsonObject::class.java)

                    onComplete(Data.map(data))
                }
        }
    }

    data class Data(
        @Field("is_takable")
        val isTakable: Boolean = false,
        @Field("is_suspended")
        val isSuspended: Boolean = false,
        @Field("is_canceled")
        val isCanceled: Boolean = false,
        @Field("is_complete")
        val isComplete: Boolean = false
    ) : IResponse {
        companion object {
            private fun getInstance(): Data {
                return Data()
            }

            fun map(data: JsonObject): Data {
                return getInstance().getResponse(data) as Data
            }
        }
    }
}

internal object ProctorPusherListener {
    fun addListener(
        onComplete: (Data) -> Unit
    ) {
        Pusher(
            PusherConstant.API_KEY,
            PusherOptions()
                .setCluster(PusherConstant.CLUSTER)
        ).also {
            it.connect(
                object : ConnectionEventListener {
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
                },
                ConnectionState.ALL
            )

            it.subscribe(PusherConstant.CHANNEL)
                .bind(PusherConstant.PROCTOR_EVENT) { event ->
                    println("Pusher: Received event with data: $event")
                    println("Pusher.Data: ${event.data}")

                    val data = Gson().fromJson(event.data, JsonObject::class.java)

                    onComplete(Data.map(data))
                }
        }
    }

    data class Data(
        @Field("id")
        val id: Int = 0,
        @Field("form_name")
        val formName: String? = null,
        @Field("status_name")
        val statusName: String? = null,
        @Field("start_time")
        val startTime: LocalTime? = null,
        @Field("end_time")
        val endTime: LocalTime? = null,
        @Field("answered_questions_count")
        val answeredQuestionsCount: Int? = null,

        @Field("is_suspended")
        val isSuspended: Boolean? = null
    ) : IResponse {

        companion object {
            private fun getInstance(): Data {
                return Data()
            }

            fun map(data: JsonObject): Data {
                return getInstance().getResponse(data) as Data
            }
        }
    }
}

fun main() {
    login()

    StudentPusherListener.addListener {

    }

    ProctorPusherListener.addListener {

    }
}
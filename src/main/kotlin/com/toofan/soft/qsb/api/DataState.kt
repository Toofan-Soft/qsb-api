import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

object DataState {
    @JvmStatic
    fun main(args: Array<String>) {
        println("start")

        val options = IO.Options()
        options.path = "/socket.io/"
        val socket = IO.socket("http://127.0.0.1:8000/api", options)

        socket.on(Socket.EVENT_CONNECT) { args ->
            socket.emit("subscribe", "data-state")
        }.on("dataStateChanged") { args ->
            println("asdadsa")
            val data = args[0] as JSONObject
            val newState = data.getString("newState")
            println(newState)
            // Handle the new state received from the server
        }.on(Socket.EVENT_DISCONNECT) { args ->
            // Handle disconnect event
        }

        socket.connect()

        println("end")
    }
}

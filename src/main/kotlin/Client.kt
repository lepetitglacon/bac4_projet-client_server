import java.io.*
import java.net.Socket

fun main() {
    val socket = Socket("localhost", 8080)
    val input = socket.getInputStream()
    val output = socket.getOutputStream()
    val writer = PrintWriter(output, true)
    val reader = BufferedReader(InputStreamReader(input))
    val objWriter = ObjectOutputStream(socket.getOutputStream())
    val objReader = ObjectInputStream(socket.getInputStream())

    val player = Player()
    objWriter.writeObject(player)
    objWriter.flush()

    // send request to server and handle response
    Thread {
        while (true) {
            writer.println("test")

            val message = reader.readLine()
            println(message)
        }
    }.start()

}
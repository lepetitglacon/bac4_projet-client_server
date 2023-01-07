import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

fun main() {
    val socket = Socket("localhost", 8080)
    val input = socket.getInputStream()
    val output = socket.getOutputStream()
    val writer = PrintWriter(output, true)
    val reader = BufferedReader(InputStreamReader(input))
    // send request to server and handle response

    Thread {
        while (true) {
            writer.println("test")

            val message = reader.readLine()
            println(message)
        }
    }.start()

}
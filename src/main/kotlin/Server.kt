import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

fun main() {
    val serverSocket = ServerSocket(8080)
    while (true) {
        val clientSocket = serverSocket.accept()
        println("client connected")
        Thread {
            val input = clientSocket.getInputStream()
            val output = clientSocket.getOutputStream()
            val writer = PrintWriter(output, true)
            val reader = BufferedReader(InputStreamReader(input))
            // handle client request and send response

            while (true) {
                if (clientSocket.isClosed) {
                    clientSocket.close()
                }

                val request = reader.readLine()
                val command = request.split(" ")[0]

                when (command) {
                    "test" -> {
                        println("client sent a test command")
                        writer.println("test command")
                    }
                }

//                writer.println("Server sent data")
//                Thread.sleep(16)
            }
        }.start()
    }
}
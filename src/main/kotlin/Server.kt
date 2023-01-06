import client.entitiy.Player
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.random.Random

class Server {
    private val executor: ExecutorService = Executors.newCachedThreadPool()
    private val clients = mutableListOf<ClientHandler>()
    private val socketPlayers = mutableMapOf<Socket, Player>()
    private val playerPositions = mutableMapOf<String, Pair<Int, Int>>()

    fun start(port: Int) {
        println("Starting server on port $port")

        val serverSocket = ServerSocket(port)
        while (true) {
            try {
                val clientSocket = serverSocket.accept()
                println("Accepted connection from ${clientSocket.inetAddress.hostAddress}")

                val clientHandler = ClientHandler(clientSocket)
                clients.add(clientHandler)
                executor.execute(clientHandler)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private inner class ClientHandler(private val socket: Socket) : Runnable {
        private val inReader = BufferedReader(InputStreamReader(socket.getInputStream()))
        private val outWriter = PrintWriter(socket.getOutputStream(), true)

        override fun run() {
            try {
                while (true) {
                    val input = inReader.readLine() ?: break

                    println("Received message from client: $input")

                    // Split the input into command and parameters
                    val tokens = input.split(" ")
                    val command = tokens[0]
                    val params = tokens.drop(1)

                    when (command) {
                        "login" -> {
                            // Associate the socket with the player name
                            val playerName = params[0]
                            val p = Player(playerName, Random.nextInt(0,300), Random.nextInt(0,300))
                            socketPlayers[socket] = p
                            println("Logged in as $playerName")

                            outWriter.println("${p.name} ${p.x} ${p.y}")
                        }
                        "move" -> {
                            // Update the position of the player associated with the socket
                            val playerName = socketPlayers[socket]
                            if (playerName != null) {
                                val x = params[0].toInt()
                                val y = params[1].toInt()
                                // Update the position of the player in the game state
                                // ...
                                outWriter.println("Moved $playerName to ($x, $y)")
                            } else {
                                outWriter.println("Not logged in")
                            }
                        }
                        "players" -> {
                            // Serialize the list of players and their positions as JSON and send it to the client
                            val playersData = playerPositions.map { (name, position) ->
                                mapOf(
                                    "name" to name,
                                    "x" to position.first,
                                    "y" to position.second
                                )
                            }
                            val jsonData = Gson().toJson(playersData)
                            outWriter.println("players\n$jsonData")
                        }
                        else -> {
                            // Unknown command
                            outWriter.println("Invalid command")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                // Remove the client from the list and close the socket
                clients.remove(this)
                socketPlayers.remove(socket)
                socket.close()
                println("Closed connection with ${socket.inetAddress.hostAddress}")
            }
        }

        fun send(message: String) {
            outWriter.println(message)
        }
    }
}

fun main() {
    val server = Server()
    server.start(8081)
}
import common.Player
import java.io.BufferedReader
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.io.PrintWriter
import java.io.Serializable
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

class Server {
    private val port = 8080
    private val serverSocket = ServerSocket(port)
    val id = AtomicInteger(0)
    private val clients = mutableMapOf<Socket, ObjectOutputStream>()
    val players = Collections.synchronizedList(arrayListOf<Player>())

    init {
        start()
    }

    fun start() {
        println("Server's running on port")
        while (true) {
            val socket = serverSocket.accept()
            val objectWriter = ObjectOutputStream(socket.getOutputStream())
            synchronized(clients) {
                clients[socket] = objectWriter
            }

            try {
                val playerId = id.getAndIncrement()
                println("New player connected : $playerId")
                val player = Player(playerId, "Joueur $playerId", Random.nextInt(200), Random.nextInt(200))
                synchronized(players) {
                    players.add(player)
                    println("added player $players")
                }

                objectWriter.writeUnshared(player)
                objectWriter.flush()

                clients.forEach {
                    println("writing data to client ${it.key}")
                    println(players)
                    it.value.writeUnshared(players)
                    it.value.flush()
                }

//                    while(socket.isConnected) {
//                        val message = reader.readLine()
//
//                        when(message) {
//
//                        }
//                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun sendToAll() {
        synchronized(players) {

        }

    }
}
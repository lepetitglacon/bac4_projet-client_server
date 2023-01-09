package server

import common.Player
import common.Request
import common.RequestType
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.PrintWriter
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.LinkedHashSet

class Server() : Thread() {
    var running = true
    val socket = ServerSocket(8080)
    val clientSockets: MutableSet<Socket> = mutableSetOf()

    val playersSync = Any()

    companion object {
        var players = Collections.synchronizedSet<Player>(LinkedHashSet())
        var id = AtomicInteger(1)
    }

    override fun run() {
        println("Server listening at $socket")

        while(running) {
            val clientSocket = socket.accept()
            clientSockets.add(clientSocket)

            // get id
            val id = Companion.id.getAndIncrement()
            println("New connection [$id] at $clientSocket")

            // IO
            val ins = clientSocket.getInputStream()
            val ous = clientSocket.getOutputStream()
            val reader = BufferedReader(InputStreamReader(ins))
            val writer = PrintWriter(ous, true)
            val objectWriter = ObjectOutputStream(ous)
            val objectReader = ObjectInputStream(ins)

            // client Thread
            Thread {
                println("Thread for player $id running")

                try {
                    // handle client request
                    while (clientSocket.isConnected) {
                        players = getAllConnectedPlayers()
                        val request = objectReader.readObject() as Request
                        when (request.type) {
                            RequestType.LOGIN -> {
                                val p = Player(id, "joueur $id")
                                addPlayerToPlayers(p)
                                println("Player $id connected to the game")
                                objectWriter.writeObject(p)
                            }
                            RequestType.PLAYERS -> {
                                val data = getAllConnectedPlayers()
                                println("reader $id : ${data}")
                                objectWriter.writeObject(data)
                            }
                        }
                    }
                } catch (e: Exception) {
                    // delete player
                    removePlayerFromPlayers(id)

                    // clean connection
                    clientSocket.close()
                    clientSockets.remove(clientSocket)
                }

            }.start()
        }

        // close clients connection and stop server socket
        clientSockets.forEach { it.close() }
        socket.close()
    }

    @Synchronized
    fun addPlayerToPlayers(p: Player) {
        Server.players.add(p)
    }

    @Synchronized
    fun removePlayerFromPlayers(id: Int) {
        Server.players.removeIf { it.id == id }
    }

    @Synchronized
    fun getAllConnectedPlayers(): Set<Player> {
        return Server.players
    }
}
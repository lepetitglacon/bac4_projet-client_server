package server

import common.Player
import common.Request
import common.RequestType
import common.Vec2
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger


class Server() : Thread() {
    var running = true
    val socket = ServerSocket(8080)
    val clientSockets: MutableSet<Socket> = mutableSetOf()

    val playersSync = Any()

    companion object {
        private val players = CopyOnWriteArrayList<Player>()
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
            val objectWriter = ObjectOutputStream(clientSocket.getOutputStream())
            val objectReader = ObjectInputStream(clientSocket.getInputStream())

            // client Thread
            Thread {
                println("Thread for player $id running")

                try {
                    // handle client request
                    while (clientSocket.isConnected) {
                        val request = objectReader.readObject() as Request
                        when (request.type) {
                            RequestType.LOGIN -> {
                                val p = Player(id, "joueur $id")
                                addPlayerToPlayers(p)
                                println("Player $id connected to the game")
                                objectWriter.writeObject(p)
                            }
                            RequestType.PLAYERS -> {
                                synchronized(players) {
                                    println(getAllConnectedPlayers())
                                    objectWriter.writeUnshared(getAllConnectedPlayers())
                                }
                            }
                            RequestType.MOVE -> {
                                val vec = objectReader.readObject() as Vec2
                                val p = players.find { it.id == id }
                                p?.x = (p?.x ?: 0) + vec.x.toInt()
                                p?.y = (p?.y ?: 0) + vec.y.toInt()
                                replacePlayerToPlayers(p)
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
        synchronized(players) {
            Server.players.add(p)
        }
    }

    @Synchronized
    fun replacePlayerToPlayers(p: Player?) {
        if (p != null)
        synchronized(players) {
            Server.players.removeIf { it.id == p.id }
            Server.players.add(p)
        }
    }

    @Synchronized
    fun removePlayerFromPlayers(id: Int) {
        synchronized(players) {
            Server.players.removeIf { it.id == id }
        }
    }

    @Synchronized
    fun getAllConnectedPlayers(): CopyOnWriteArrayList<Player> {
        synchronized(players) {
            return Server.players
        }
    }
}
package client

import common.Player
import common.Request
import common.RequestType
import common.Vec2
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.io.*
import java.lang.Exception
import java.net.Socket
import java.util.concurrent.CopyOnWriteArrayList
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.Timer

object Client : JFrame() {
    // Connect to the server
    val socket = Socket("localhost", 8080)

    // IO
    val objectWriter = ObjectOutputStream(socket.getOutputStream())
    val objectReader = ObjectInputStream(socket.getInputStream())

    val WIDTH = 480
    val HEIGHT = 720

    var up = false
    var left = false
    var down = false
    var right = false
    var keyboardMovementVector = Vec2()

    val game = Game()
    var state = ClientState.LOGIN

    init {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT)

        game.setSize(WIDTH, HEIGHT)
        game.isVisible = true
        add(game)

        // input event
        addKeyListener(object : KeyAdapter() {
            override fun keyTyped(e: KeyEvent?) {
            }

            override fun keyPressed(e: KeyEvent) {
                when (e.keyCode)
                {
                    KeyEvent.VK_Z, KeyEvent.VK_UP -> {
                        up = true
                    }
                    KeyEvent.VK_Q, KeyEvent.VK_LEFT -> {
                        left = true
                    }
                    KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                        down = true
                    }
                    KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                        right = true
                    }
                }
            }

            override fun keyReleased(e: KeyEvent) {
                when (e.keyCode)
                {
                    KeyEvent.VK_Z, KeyEvent.VK_UP -> {
                        up = false
                    }
                    KeyEvent.VK_Q, KeyEvent.VK_LEFT -> {
                        left = false
                    }
                    KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                        down = false
                    }
                    KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                        right = false
                    }
                }
            }
        })

        isVisible = true

        run()
    }

    fun run() {
        Thread {
            while (true) {

                val serverMessage = objectReader.readObject() as Request

                when (serverMessage.type) {
                    RequestType.LOGIN -> {
                        synchronized(game.player) {
                            game.player = serverMessage.data!! as Player
                        }
                    }

                    RequestType.PLAYERS -> TODO()
                    RequestType.MOVE -> TODO()
                }


//                when (state) {
//
//                    ClientState.LOGIN -> {
//                        println("logging in")
//                        objectWriter.writeObject(Request(RequestType.LOGIN))
//                        try { game.player = objectReader.readObject() as Player } catch(e: Exception) {e.printStackTrace()}
//                        println("client received id : ${game.player.id} ${game.player}")
//                        title += " ${game.player.id}"
//                        state = ClientState.GAME
//                    }
//
//                    ClientState.GAME -> {
//                        objectWriter.writeObject(Request(RequestType.MOVE))
//                        try {// move
//                            objectWriter.writeUnshared(keyboardMovementVector)
//                        } catch (e: Exception) { }
//
//                        objectWriter.writeObject(Request(RequestType.PLAYERS))
//                        try {
//                            synchronized(game.players) {
//                                game.players = objectReader.readObject() as CopyOnWriteArrayList<Player>
//                                println(game.players)
//                            }
//                        } catch (e: Exception) { }
//                    }
//
//                }
//                Thread.sleep(100)
            }
        }.start()
    }

    fun getPlayer(): Player? {
        return game.players.find { it.id == game.player.id }
    }

    fun getMovementByInput() {
        if (up && left) {
            keyboardMovementVector.x = -8.0
            keyboardMovementVector.y = -8.0
        } else if (up && right) {
            keyboardMovementVector.x = 8.0
            keyboardMovementVector.y = -8.0
        } else if (down && left) {
            keyboardMovementVector.x = -8.0
            keyboardMovementVector.y = 8.0
        } else if (down && right) {
            keyboardMovementVector.x = 8.0
            keyboardMovementVector.y = 8.0
        } else if (up) {
            keyboardMovementVector.x = 0.0
            keyboardMovementVector.y = -8.0
        } else if (down) {
            keyboardMovementVector.x = 0.0
            keyboardMovementVector.y = 8.0
        } else if (left) {
            keyboardMovementVector.x = -8.0
            keyboardMovementVector.y = 0.0
        } else if (right) {
            keyboardMovementVector.x = 8.0
            keyboardMovementVector.y = 0.0
        } else {
            keyboardMovementVector.x = 0.0
            keyboardMovementVector.y = 0.0
        }
        Client.objectWriter.writeUnshared(keyboardMovementVector)
    }

}

class Game() :JPanel() {
    var player = Player()
    var players = CopyOnWriteArrayList<Player>()

    val timer = Timer(1) { run() }.start()

    fun run() {
        Client.getMovementByInput()
        repaint()
    }

    override fun paintComponent(gg: Graphics) {
        val g = gg as Graphics2D
        g.color = Color.BLACK

        players.forEach {
            if (it.id != Client.getPlayer()?.id)
            {
                g.drawOval(getPositionFromPlayer(it).x, getPositionFromPlayer(it).y, 64, 64)
                g.drawString(it.id.toString(), getPositionFromPlayer(it).x + 32, getPositionFromPlayer(it).y + 32)
            }
        }

        val p = Client.getPlayer()
        if (p !== null) g.drawOval(Client.WIDTH/2 - 64/2, Client.HEIGHT/2 - 64/2, 64, 64)
    }

    fun getPositionFromPlayer(p: Player): Point {
        val width = 64
        val height = 64

        return Point(
            p.x - width/2 - Client.getPlayer()?.x!! + Client.WIDTH / 2,
            p.y - height/2 - Client.getPlayer()?.y!! + Client.HEIGHT / 2
        )
    }
}


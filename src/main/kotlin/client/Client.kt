package client

import Window
import common.Player
import java.awt.Graphics
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.PrintWriter
import java.net.Socket
import javax.swing.JPanel
import javax.swing.Timer

object Client : JPanel() {
    private val socket = Socket("localhost", 8080)
    val input = socket.getInputStream()
    val output = socket.getOutputStream()
    val writer = PrintWriter(socket.getOutputStream())
    val reader = socket.getInputStream().bufferedReader()
    val objectWriter = ObjectOutputStream(socket.getOutputStream())
    val objectReader = ObjectInputStream(socket.getInputStream())

    var player: Player = Player()
    val players = mutableSetOf<Player>()
    val timer = Timer(1) { run() }
    val ticks = 0

    val window = Window()

    init {
        init(objectReader.readUnshared() as Player)
        updatePlayersPosition(objectReader.readUnshared() as List<Player>)
        Thread {
            println("listening thread is running")
            while (true) {
                updatePlayersPosition(objectReader.readObject() as List<Player>)
            }
        }.start()
    }

    fun init(p: Player) {
        player = p
        window.title = "Player ${player.id}"
    }

    fun run() {
        repaint()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)

        players.filter { it.id != player?.id }.forEach {
            g.color = it.color
            g.fillOval(it.xFromHero(player), it.yFromHero(player), 32, 32)
            g.drawString("${it.id}", it.xFromHero(player), it.yFromHero(player) - 10)
        }

        g.color = player?.color
        g.fillOval(400/2 - 16, 400/2 - 16, 32, 32)

        g.drawString("$ticks", 0, 0)
    }

    fun updatePlayersPosition(ps : List<Player>) {
        println("received players $players")
        ps.forEach {
            players.add(it)
        }
    }
}
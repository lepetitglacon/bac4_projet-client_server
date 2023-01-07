package client

//import client.entitiy.Player
import client.game.Game
import client.window.Window
import java.awt.Graphics
import java.awt.Graphics2D
import java.io.*
import java.net.Socket
import java.util.*
import javax.swing.JPanel
import javax.swing.SwingUtilities
import javax.swing.Timer

object Client : JPanel() {
    var socket: Socket? = null
    var inReader: BufferedReader? = null
    var outWriter: PrintWriter? = null

    val scanner = Scanner(System.`in`)

    val timer = Timer(1) { run() }

    var state = ClientState.MENU
    var window: Window = Window()
    var game: Game? = null

//    var pPlayer: Player? = null
    var playerName: String = ""

    init {
        SwingUtilities.invokeLater {
            window.init()
            timer.start()

            println("engine running")
        }
    }

    fun run() {
        when(state) {
            ClientState.MENU -> {  }
            ClientState.PLAYER_NAME -> {  }
            ClientState.CONNECTING -> {
                connect()
                login()
            }
            ClientState.GAME -> {
                game?.update()
                repaint()
            }
        }
        repaint()
    }

    private fun connect() {
        try {
            // Connect to the server
            socket = Socket("localhost", 8081)
            println("Connected to server")

            // Set I/O
            inReader = BufferedReader(InputStreamReader(socket!!.getInputStream()))
            outWriter = PrintWriter(socket!!.getOutputStream(), true)
        } catch (e: IOException) {
            e.printStackTrace()
            state = ClientState.MENU
        }
    }

    fun login() {
        // Login to server
        outWriter?.println("login $playerName")

        // Read and process the response from the server
        val response = inReader?.readLine()?.split(" ")
        println(response)
//        pPlayer = Player(response!![0], response[1].toInt(), response[2].toInt())

        // Start the game
        game = Game()
        state = ClientState.GAME
    }

    override fun paint(gg: Graphics) {
        super.paint(gg)
        val g = gg as Graphics2D
        when(state) {
            ClientState.MENU -> g.drawString("press enter to connect", 150, 150)
            ClientState.PLAYER_NAME -> {
                g.drawString("Enter a name", 150, 100)
                g.drawString(playerName, 150, 150)
            }
            ClientState.CONNECTING -> g.drawString("Connecting to server", 150, 150)
            ClientState.GAME -> game?.draw(g)
        }
    }
}
package client

import common.Player
import common.Request
import common.RequestType
import java.io.*
import java.lang.Exception
import java.net.Socket
import javax.swing.JFrame
import javax.swing.JPanel

class Client() : JFrame() {
    // Connect to the server
    val socket = Socket("localhost", 8080)

    // IO
//    val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
//    val writer = PrintWriter(socket.getOutputStream(), true)
    val objectWriter = ObjectOutputStream(socket.getOutputStream())
    var objectReader = ObjectInputStream(socket.getInputStream())

    var player = Player()
    var state = ClientState.LOGIN


    init {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        val panel = JPanel()
        panel.setSize(400, 400)
        setSize(400, 400)
        add(panel)
        pack()
        isVisible = true

        run()
    }

    fun run() {
        Thread {
            var players = setOf<Player>()
            while (true) {
                when (state) {

                    ClientState.LOGIN -> {
                        println("logging in")
                        objectWriter.writeObject(Request(RequestType.LOGIN))
                        player = objectReader.readObject() as Player
                        println("client received id : ${player.id}")
                        state = ClientState.GAME
                    }

                    ClientState.GAME -> {
                        objectWriter.writeObject(Request(RequestType.PLAYERS))
                        try {
                            players = objectReader.readObject() as Set<Player>
                            println("${player.id} : ${players}")
                        } catch (e: Exception) {

                        }

//                        writer.println("message")
//                        objectWriter.writeObject(players)
                    }
                }
                Thread.sleep(100)
            }
        }.start()
    }

}
package client.window

import client.Client
import client.ClientState
import java.awt.Dimension
import java.awt.Point
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JFrame

class Window : JFrame() {
    val WIDTH = 400
    val HEIGHT = 300
    fun center() = Point(WIDTH/2, HEIGHT/2)

    fun init() {
        // Set up the frame
        title = "Client"
        size = Dimension(WIDTH, HEIGHT)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        isVisible = true

        add(Client)

        addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent) {
                when(e.keyCode) {
                    KeyEvent.VK_ENTER -> {
                        when(Client.state) {
                            ClientState.MENU -> Client.state = ClientState.PLAYER_NAME
                            ClientState.CONNECTING -> {}
                            ClientState.PLAYER_NAME -> Client.state = ClientState.CONNECTING
                            ClientState.GAME -> {}
                        }
                    }
                    else -> { if (Client.state == ClientState.PLAYER_NAME) Client.playerName += (e.keyChar) }
                }
            }
        })
    }
}
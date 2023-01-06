package client.game

import client.Client
import client.ClientState
import java.awt.Graphics2D

class Game {

    fun update() {
        when(Client.state) {
            ClientState.MENU -> {}
            ClientState.CONNECTING -> {}
            ClientState.PLAYER_NAME -> {}
            ClientState.GAME -> {

                Client.outWriter?.println("players")

                val response = Client.inReader?.readLine()
                println(response)

//                println("Enter a player name or 'exit' to quit:")
//                val input = scanner.nextLine()
//                if (input == "exit") {
//
//                }
//
//                // Send a request for the player position to the server
//                Client.outWriter?.println("position $input")
//
//                // Read and process the response from the server
//                val response = Client.inReader?.readLine()
//                println(response)
//                val pos = Client.inReader?.readLine()?.split(" ")
//                println(pos)
//
//                Client.pPlayer.x = (pos?.get(0) ?: 0) as Int
//                Client.pPlayer.y = (pos?.get(1) ?: 0) as Int
            }
        }

    }

    fun draw(g: Graphics2D) {
        val radius = 16
        g.drawOval(Client.window.center().x,Client.window.center().y, radius, radius)
    }
}
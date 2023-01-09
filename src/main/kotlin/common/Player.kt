package common

import java.awt.Graphics2D
import java.io.Serializable

class Player(val id: Int = 0, val name: String = "esteban", var x: Int = 0, var y: Int = 0) : Serializable {

    fun draw(g: Graphics2D) {
        g.drawOval(x, y, 16, 16)
    }

    override fun toString(): String {
        return "Player[$id] \"$name\" at $x, $y"
    }
}
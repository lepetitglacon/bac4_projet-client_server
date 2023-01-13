package common

import java.awt.Color
import java.awt.Graphics2D
import java.io.Serializable
import kotlin.random.Random

class Player(var id: Int = 0, val name: String = "esteban", var x: Int = 0, var y: Int = 0) : Serializable {
    val color = Color(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))

    fun draw(g: Graphics2D) {
        g.drawOval(x, y, 16, 16)
    }

    fun xFromHero(player: Player): Int {
        return x - 16 - player.x!! + 200
    }

    fun yFromHero(player: Player): Int {
        return y - 16 - player.y!! + 200
    }

    override fun toString(): String {
        return "Player[$id] \"$name\" at $x, $y"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Player

        if (id != other.id) return false
        if (name != other.name) return false
        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + x
        result = 31 * result + y
        return result
    }


}
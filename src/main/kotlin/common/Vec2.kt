package common

import java.awt.Point
import java.io.Serializable
import kotlin.math.pow
import kotlin.math.sqrt

class Vec2(var x: Double = 0.0, var y: Double = 0.0) : Serializable {
    constructor(point: Point) : this(point.x.toDouble(), point.y.toDouble())
    constructor(x: Int, y: Int) : this(x.toDouble(), y.toDouble())

    fun length(): Double = sqrt((x * x + y * y))
    fun distance(v: Vec2): Double = minus(v).length()
    fun dotProduct(v1: Vec2, v2: Vec2): Vec2 {
        v1.x * v2.x + v1.y * v2.y; return this
    }

    fun normalize() {
        if (length() != 0.0)
            div(length())
    }

    fun normalized(): Vec2 {
        if (length() == 0.0) return Vec2()
        div(length())
        return this
    }

    operator fun plus(v: Vec2): Vec2 {
        x += v.x; y += v.y; return this
    }
    operator fun plus(int: Int): Vec2 {
        x += int
        y += int
        return this
    }
    operator fun plus(int: Double): Vec2 {
        x += int
        y += int
        return this
    }

    operator fun minus(v: Vec2): Vec2 {
        x += -v.x
        y += -v.y
        return this
    }
    operator fun minus(int: Int): Vec2 {
        x -= int
        y -= int
        return this
    }
    operator fun minus(int: Double): Vec2 {
        x -= int
        y -= int
        return this
    }

    operator fun div(v: Vec2): Vec2 {
        x /= v.x
        y /= v.y
        return this
    }

    operator fun div(int: Int): Vec2 {
        x /= int; y /= int; return this
    }

    operator fun div(int: Double): Vec2 {
        x /= int; y /= int; return this
    }

    operator fun times(v: Vec2): Vec2 {
        x *= v.x; y *= v.y; return this
    }
    operator fun times(int: Int): Vec2 {
        x *= int; y *= int; return this
    }
    operator fun times(int: Double): Vec2 {
        x *= int; y *= int; return this
    }

    operator fun not(): Vec2 {
        x = -x
        y = -y
        return this
    }
    operator fun minusAssign(d: Double) {
        x += -d
        y += -d
    }

    operator fun unaryPlus() = this + 1
    operator fun unaryMinus() = this - 1

    override fun equals(other: Any?): Boolean {
        if (other !is Vec2) return false
        return x == other.x && y == other.y
    }

    fun translateTo(v: Vec2, speed: Int) {
        this + translatedTo(v, speed)
    }

    fun translatedTo(v: Vec2, speed: Int): Vec2 {
        val newPos = Vec2()
        val posMinusHero = Vec2()
        posMinusHero.x = v.x - x
        posMinusHero.y = v.y - y
        newPos.x = (x + posMinusHero.x / posMinusHero.length()) * speed
        newPos.y = (y + posMinusHero.y / posMinusHero.length()) * speed
        return Vec2(x + newPos.x, y + newPos.y)
    }

    fun clone() = Vec2(x, y)
    override fun toString() = "$x $y"
    override fun hashCode(): Int = 31 * x.hashCode() + y.hashCode()
}
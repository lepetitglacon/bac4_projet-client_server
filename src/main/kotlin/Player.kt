import java.io.Serializable
import kotlin.random.Random

class Player(val x: Int = 0, val y: Int = 0) : Serializable {
    val id: String = getRandomId()

    private fun getRandomId(): String {
        return Random.Default.nextBytes(10).toString()
    }
}
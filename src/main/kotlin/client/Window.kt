import client.Client
import javax.swing.JFrame

class Window: JFrame() {

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(400, 400)
        add(Client)
        isVisible = true
    }
}
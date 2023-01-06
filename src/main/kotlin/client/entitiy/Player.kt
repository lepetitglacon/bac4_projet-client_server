package client.entitiy

abstract class Entity(var x: Int = 0, var y: Int = 0)
class Player(val name: String, x: Int, y: Int) : Entity(x, y)
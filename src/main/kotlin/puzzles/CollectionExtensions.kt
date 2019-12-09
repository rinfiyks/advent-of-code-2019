package puzzles

fun <T> List<T>.updated(index: Int, value: T): List<T> {
    val m = this.toMutableList()
    m[index] = value
    return m.toList()
}

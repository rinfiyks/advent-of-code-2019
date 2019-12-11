package puzzles

fun <T> List<T>.updated(index: Int, value: T): List<T> {
    val m = this.toMutableList()
    m[index] = value
    return m.toList()
}

fun <K, V> Map<K, V>.updated(index: K, value: V): Map<K, V> {
    val m = this.toMutableMap()
    m[index] = value
    return m.toMap()
}

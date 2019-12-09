package puzzles

fun <T> List<T>.updated(index: Int, value: T): List<T> {
    val m = this.toMutableList()
    m[index] = value
    return m.toList()
}

fun <K, V> Map<K, V>.updated(key: K, value: V): Map<K, V> {
    val m = this.toMutableMap()
    m[key] = value
    return m.toMap()
}

package puzzles

object MathUtil {

    tailrec fun gcd(a: Int, b: Int): Int =
        if (b == 0) a else gcd(b, a % b)

    tailrec fun gcd(a: Long, b: Long): Long =
        if (b == 0L) a else gcd(b, a % b)

    fun lcm(a: Long, b: Long): Long =
        a * (b / gcd(a, b))

    fun lcm(l: List<Long>): Long =
        l.reduce { a, b -> lcm(a, b) }

}

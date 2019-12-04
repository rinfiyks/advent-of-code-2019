package puzzles

class Day4(rawInput: List<String>) {

    private val range = rawInput[0].split("-").map { it.toInt() }
    private val min = range[0]
    private val max = range[1]
    private val intRange = min..max

    fun part1(): Int =
        intRange.filter { part1Criteria(it) }.size


    fun part2(): Int =
        intRange.filter { part2Criteria(it) }.size

    companion object {
        fun part1Criteria(it: Int) = (adjacentDigitsSame(it.toString())
                && monotonicallyIncreasingDigits(it.toString()))

        fun part2Criteria(it: Int) = (adjacentDigitsSameAtMost2(it.toString())
                && monotonicallyIncreasingDigits(it.toString()))

        private fun adjacentDigitsSame(n: String): Boolean =
            n.windowed(2).any { it[0] == it[1] }

        private fun adjacentDigitsSameAtMost2(n: String): Boolean =
            n.split("(?<=(.))(?!\\1)".toRegex()).any { it.length == 2 }

        private fun monotonicallyIncreasingDigits(n: String): Boolean =
            n.windowed(2).all { it[0].toString().toInt() <= it[1].toString().toInt() }
    }

}

fun main() {
    val day4 = Day4(Resources.input(4))
    println(day4.part1())
    println(day4.part2())
}

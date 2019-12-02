package puzzles

class Day1(rawInput: List<String>) {

    private val input: List<Int> = rawInput.map { it.toInt() }

    fun part1(): Int {
        return input.map { it / 3 - 2 }.sum()
    }

    fun part2(): Int {
        return input.map { calculateFuel(it) }.sum()
    }

    private tailrec fun calculateFuel(mass: Int, acc: Int = 0): Int {
        val fuel = mass / 3 - 2
        return if (fuel <= 0) acc
        else calculateFuel(fuel, acc + fuel)
    }

}

fun main() {
    val day1 = Day1(Resources.input(1))
    println(day1.part1())
    println(day1.part2())
}

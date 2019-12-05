package puzzles

class Day5(rawInput: List<String>) {

    private val input: List<Int> =
        rawInput.first().split(",").map { it.toInt() }

    fun part1(): Int {
        return IntcodeComputer.runProgram(input, 1).output.last()
    }

    fun part2(): Int {
        return IntcodeComputer.runProgram(input, 5).output.last()
    }

}

fun main() {
    val day5 = Day5(Resources.input(5))
    println(day5.part1())
    println(day5.part2())
}

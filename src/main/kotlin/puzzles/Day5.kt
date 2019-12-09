package puzzles

class Day5(rawInput: List<String>) {

    private val originalInput: Map<Long, Long> = IntcodeComputer.parseInput(rawInput)

    fun part1(): Long {
        return IntcodeComputer.runProgram(
            IntcodeComputer.State(originalInput.toMutableMap(), listOf(1L))
        ).output.last()
    }

    fun part2(): Long {
        return IntcodeComputer.runProgram(
            IntcodeComputer.State(originalInput.toMutableMap(), listOf(5L))
        ).output.last()
    }

}

fun main() {
    val day5 = Day5(Resources.input(5))
    println(day5.part1())
    println(day5.part2())
}

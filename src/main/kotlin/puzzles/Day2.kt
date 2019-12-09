package puzzles

class Day2(rawInput: List<String>) {

    private val originalInput: Map<Long, Long> = IntcodeComputer.parseInput(rawInput)

    fun part1(): Long {
        val input = originalInput.toMutableMap()
        input[1] = 12L
        input[2] = 2L
        return IntcodeComputer.runProgram(
            IntcodeComputer.State(input)
        ).program[0] ?: 0
    }

    fun part2(): Long {
        for (i in 0..99) {
            for (j in 0..99) {
                val input = originalInput.toMutableMap()
                input[1] = i.toLong()
                input[2] = j.toLong()
                val result = IntcodeComputer.runProgram(
                    IntcodeComputer.State(input)
                ).program
                if ((result[0L] ?: 0L) == 19690720L) return 100 * (result[1L] ?: 0L) + (result[2L] ?: 0)
            }
        }
        return 0
    }

}

fun main() {
    val day2 = Day2(Resources.input(2))
    println(day2.part1())
    println(day2.part2())
}

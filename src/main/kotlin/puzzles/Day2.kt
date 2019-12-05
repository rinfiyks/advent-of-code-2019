package puzzles

class Day2(rawInput: List<String>) {

    private val input: List<Int> =
        rawInput.first().split(",").map { it.toInt() }

    fun part1(): Int {
        return IntcodeComputer.runProgram(input.updated(1, 12).updated(2, 2))[0]
    }

    fun part2(): Int {
        for (i in 0..99) {
            for (j in 0..99) {
                val result = IntcodeComputer.runProgram(input.updated(1, i).updated(2, j))
                if (result[0] == 19690720) return 100 * result[1] + result[2]
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

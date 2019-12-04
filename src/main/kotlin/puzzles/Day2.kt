package puzzles

class Day2(rawInput: List<String>) {

    private val input: List<Int> =
        rawInput.first().split(",").map { it.toInt() }

    fun part1(): Int {
        return runProgram(input.updated(1, 12).updated(2, 2))[0]
    }

    fun part2(): Int {
        for (i in 0..99) {
            for (j in 0..99) {
                val result = runProgram(input.updated(1, i).updated(2, j))
                if (result[0] == 19690720) return 100 * result[1] + result[2]
            }
        }
        return 0
    }

    companion object {
        tailrec fun runProgram(program: List<Int>, pointer: Int = 0): List<Int> {
            return when (program[pointer]) {
                1 -> {
                    val updatedProgram = applyOpcode(program, pointer, Int::plus)
                    runProgram(updatedProgram, pointer + 4)
                }
                2 -> {
                    val updatedProgram = applyOpcode(program, pointer, Int::times)
                    runProgram(updatedProgram, pointer + 4)
                }
                else -> program
            }
        }

        private fun applyOpcode(program: List<Int>, index: Int, op: (Int, Int) -> Int): List<Int> {
            val x = program[program[index + 1]]
            val y = program[program[index + 2]]
            return program.updated(program[index + 3], op(x, y))
        }

        private fun <T> List<T>.updated(index: Int, value: T): List<T> {
            val m = this.toMutableList()
            m[index] = value
            return m.toList()
        }
    }

}

fun main() {
    val day2 = Day2(Resources.input(2))
    println(day2.part1())
    println(day2.part2())
}

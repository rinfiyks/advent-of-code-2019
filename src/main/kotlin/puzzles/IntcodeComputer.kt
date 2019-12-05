package puzzles

object IntcodeComputer {

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

}

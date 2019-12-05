package puzzles

object IntcodeComputer {

    data class ProgramResult(val program: List<Int>, val output: List<Int>)

    tailrec fun runProgram(
        program: List<Int>,
        input: Int = 0,
        pointer: Int = 0,
        output: List<Int> = emptyList()
    ): ProgramResult {
        val instruction = program[pointer].toString()
        val opcode = instruction.takeLast(1)
        val modes = instruction.dropLast(2).takeLast(2).padStart(2, '0')
        return when (opcode) {
            "1" -> {
                val updatedProgram = applyOpcode(program, pointer, modes, Int::plus)
                runProgram(updatedProgram, input, pointer + 4, output)
            }
            "2" -> {
                val updatedProgram = applyOpcode(program, pointer, modes, Int::times)
                runProgram(updatedProgram, input, pointer + 4, output)
            }
            "3" -> {
                val updatedProgram = program.updated(program[pointer + 1], input)
                runProgram(updatedProgram, input, pointer + 2, output)
            }
            "4" -> {
                val nextOutput = getValue(program, pointer + 1, modes[1] == '0')
                runProgram(program, input, pointer + 2, output + nextOutput)
            }
            "5" -> {
                val nextPointer = applyJumpOpcode(program, pointer, modes, true)
                runProgram(program, input, nextPointer, output)
            }
            "6" -> {
                val nextPointer = applyJumpOpcode(program, pointer, modes, false)
                runProgram(program, input, nextPointer, output)
            }
            "7" -> {
                val updatedProgram = applyOpcode(program, pointer, modes) { x, y -> if (x < y) 1 else 0 }
                runProgram(updatedProgram, input, pointer + 4, output)
            }
            "8" -> {
                val updatedProgram = applyOpcode(program, pointer, modes) { x, y -> if (x == y) 1 else 0 }
                runProgram(updatedProgram, input, pointer + 4, output)
            }
            else -> ProgramResult(program, output)
        }
    }

    private fun applyOpcode(program: List<Int>, index: Int, modes: String, op: (Int, Int) -> Int): List<Int> {
        val x = getValue(program, index + 1, modes[1] == '0')
        val y = getValue(program, index + 2, modes[0] == '0')
        return program.updated(program[index + 3], op(x, y))
    }

    private fun applyJumpOpcode(program: List<Int>, index: Int, modes: String, ifTrue: Boolean): Int {
        val x = getValue(program, index + 1, modes[1] == '0')
        val y = getValue(program, index + 2, modes[0] == '0')
        return if ((ifTrue && x != 0) || (!ifTrue && x == 0)) y else index + 3
    }

    private fun getValue(program: List<Int>, index: Int, parameterMode: Boolean): Int =
        if (parameterMode) program[program[index]]
        else program[index]

}

package puzzles

object IntcodeComputer {

    enum class Status {
        RUNNING, PAUSED, HALTED
    }

    data class State(
        val program: List<Int>,
        val input: List<Int> = listOf(0),
        val pointer: Int = 0,
        val output: List<Int> = emptyList(),
        val status: Status = Status.RUNNING
    ) {
        private val instructionCode = program[pointer].toString()
        val modes = instructionCode.dropLast(2).takeLast(2).padStart(2, '0')
    }

    tailrec fun runProgram(state: State): State {
        val instructionCode = state.program[state.pointer].toString()
        val opcode = instructionCode.takeLast(1)
        val nextState = when (opcode) {
            "1" -> Instruction.Add
            "2" -> Instruction.Times
            "3" -> Instruction.Update
            "4" -> Instruction.Output
            "5" -> Instruction.JumpIfTrue
            "6" -> Instruction.JumpIfFalse
            "7" -> Instruction.LessThan
            "8" -> Instruction.Equals
            else -> Instruction.Halt
        }.execute(state)

        return when (nextState.status) {
            Status.RUNNING -> runProgram(nextState)
            else -> nextState
        }
    }

    sealed class Instruction {
        abstract fun execute(state: State): State

        object Add : Instruction() {
            override fun execute(state: State): State {
                val updatedProgram = applyOpcode(state.program, state.pointer, state.modes, Int::plus)
                return state.copy(program = updatedProgram, pointer = state.pointer + 4)
            }
        }

        object Times : Instruction() {
            override fun execute(state: State): State {
                val updatedProgram = applyOpcode(state.program, state.pointer, state.modes, Int::times)
                return state.copy(program = updatedProgram, pointer = state.pointer + 4)
            }
        }

        object Update : Instruction() {
            override fun execute(state: State): State {
                return if (state.input.isEmpty()) state.copy(status = Status.PAUSED)
                else {
                    val updatedProgram = state.program.updated(state.program[state.pointer + 1], state.input[0])
                    state.copy(program = updatedProgram, input = state.input.drop(1), pointer = state.pointer + 2)
                }
            }
        }

        object Output : Instruction() {
            override fun execute(state: State): State {
                val nextOutput = getValue(state.program, state.pointer + 1, state.modes[1] == '0')
                return state.copy(pointer = state.pointer + 2, output = state.output + nextOutput)
            }
        }

        object JumpIfTrue : Instruction() {
            override fun execute(state: State): State {
                val nextPointer = applyJumpOpcode(state.program, state.pointer, state.modes, true)
                return state.copy(pointer = nextPointer)
            }
        }

        object JumpIfFalse : Instruction() {
            override fun execute(state: State): State {
                val nextPointer = applyJumpOpcode(state.program, state.pointer, state.modes, false)
                return state.copy(pointer = nextPointer)
            }
        }

        object LessThan : Instruction() {
            override fun execute(state: State): State {
                val updatedProgram =
                    applyOpcode(state.program, state.pointer, state.modes) { x, y -> if (x < y) 1 else 0 }
                return state.copy(program = updatedProgram, pointer = state.pointer + 4)
            }
        }

        object Equals : Instruction() {
            override fun execute(state: State): State {
                val updatedProgram =
                    applyOpcode(state.program, state.pointer, state.modes) { x, y -> if (x == y) 1 else 0 }
                return state.copy(program = updatedProgram, pointer = state.pointer + 4)
            }
        }

        object Halt : Instruction() {
            override fun execute(state: State): State {
                return state.copy(status = Status.HALTED)
            }
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

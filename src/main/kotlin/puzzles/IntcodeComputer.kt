package puzzles

import java.lang.IllegalArgumentException

object IntcodeComputer {

    enum class Status {
        RUNNING, PAUSED, HALTED
    }

    enum class ParameterMode {
        POSITION, IMMEDIATE, RELATIVE
    }

    data class State(
        val program: List<Int>,
        val input: List<Int> = listOf(0),
        val pointer: Int = 0,
        val output: List<Int> = emptyList(),
        val relativeBase: Int = 0,
        val status: Status = Status.RUNNING
    ) {
        private val instructionCode = program[pointer].toString()
        private val modes = instructionCode.dropLast(2).takeLast(2).padStart(2, '0').reversed()

        fun parseMode(index: Int): ParameterMode = when (modes[index]) {
            '0' -> ParameterMode.POSITION
            '1' -> ParameterMode.IMMEDIATE
            '2' -> ParameterMode.RELATIVE
            else -> throw IllegalArgumentException("Unknown mode ${modes[index]}")
        }
    }

    tailrec fun runProgram(state: State): State {
        println(state)
        val instructionCode = state.program[state.pointer].toString()
        val opcode = instructionCode.takeLast(2).padStart(2, '0')
        val nextState = when (opcode) {
            "01" -> Instruction.Add
            "02" -> Instruction.Times
            "03" -> Instruction.Update
            "04" -> Instruction.Output
            "05" -> Instruction.JumpIfTrue
            "06" -> Instruction.JumpIfFalse
            "07" -> Instruction.LessThan
            "08" -> Instruction.Equals
            "09" -> Instruction.AdjustRelativeBase
            "99" -> Instruction.Halt
            else -> throw IllegalArgumentException("Unknown opcode $opcode")
        }.execute(state)

        println(nextState)
        println()
        return when (nextState.status) {
            Status.RUNNING -> runProgram(nextState)
            else -> nextState
        }
    }

    sealed class Instruction {
        abstract fun execute(state: State): State

        object Add : Instruction() {
            override fun execute(state: State): State {
                val updatedProgram = applyOpcode(state, Int::plus)
                return state.copy(program = updatedProgram, pointer = state.pointer + 4)
            }
        }

        object Times : Instruction() {
            override fun execute(state: State): State {
                val updatedProgram = applyOpcode(state, Int::times)
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
                val nextOutput = getValue(state.program, state.pointer + 1, state.parseMode(0))
                return state.copy(pointer = state.pointer + 2, output = state.output + nextOutput)
            }
        }

        object JumpIfTrue : Instruction() {
            override fun execute(state: State): State {
                val nextPointer = applyJumpOpcode(state, true)
                return state.copy(pointer = nextPointer)
            }
        }

        object JumpIfFalse : Instruction() {
            override fun execute(state: State): State {
                val nextPointer = applyJumpOpcode(state, false)
                return state.copy(pointer = nextPointer)
            }
        }

        object LessThan : Instruction() {
            override fun execute(state: State): State {
                val updatedProgram =
                    applyOpcode(state) { x, y -> if (x < y) 1 else 0 }
                return state.copy(program = updatedProgram, pointer = state.pointer + 4)
            }
        }

        object Equals : Instruction() {
            override fun execute(state: State): State {
                val updatedProgram =
                    applyOpcode(state) { x, y -> if (x == y) 1 else 0 }
                return state.copy(program = updatedProgram, pointer = state.pointer + 4)
            }
        }

        object AdjustRelativeBase : Instruction() {
            override fun execute(state: State): State {
                val relativeBaseOffset = getValue(state.program, state.pointer + 1, state.parseMode(0))
                return state.copy(pointer = state.pointer + 2, relativeBase = state.relativeBase + relativeBaseOffset)
            }
        }

        object Halt : Instruction() {
            override fun execute(state: State): State {
                return state.copy(status = Status.HALTED)
            }
        }
    }

    private fun applyOpcode(state: State, op: (Int, Int) -> Int): List<Int> {
        val x = getValue(state.program, state.pointer + 1, state.parseMode(0), state.relativeBase)
        val y = getValue(state.program, state.pointer + 2, state.parseMode(1), state.relativeBase)
        return state.program.updated(state.program[state.pointer + 3], op(x, y))
    }

    private fun applyJumpOpcode(state: State, ifTrue: Boolean): Int {
        val x = getValue(state.program, state.pointer + 1, state.parseMode(0), state.relativeBase)
        val y = getValue(state.program, state.pointer + 2, state.parseMode(1), state.relativeBase)
        return if ((ifTrue && x != 0) || (!ifTrue && x == 0)) y else state.pointer + 3
    }

    private fun getValue(program: List<Int>, index: Int, parameterMode: ParameterMode, relativeBase: Int = 0): Int =
        when (parameterMode) {
            ParameterMode.POSITION -> program[program[index]]
            ParameterMode.IMMEDIATE -> program[index]
            ParameterMode.RELATIVE -> program[relativeBase + program[index]]
        }

}

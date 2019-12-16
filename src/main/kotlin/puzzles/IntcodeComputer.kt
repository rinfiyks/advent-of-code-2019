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
        val program: MutableMap<Long, Long>,
        val input: List<Long> = listOf(0),
        val pointer: Long = 0,
        val output: List<Long> = emptyList(),
        val relativeBase: Long = 0,
        val status: Status = Status.RUNNING
    ) {
        private val instructionCode = program[pointer].toString()
        private val modes = instructionCode.dropLast(2).takeLast(3).padStart(3, '0').reversed()

        fun parseMode(index: Int): ParameterMode = when (modes[index]) {
            '0' -> ParameterMode.POSITION
            '1' -> ParameterMode.IMMEDIATE
            '2' -> ParameterMode.RELATIVE
            else -> throw IllegalArgumentException("Unknown mode ${modes[index]}")
        }
    }

    fun parseInput(input: List<String>): Map<Long, Long> =
        input.first().split(",").map { it.toLong() }
            .withIndex().map { it.index.toLong() to it.value }.toMap()

    tailrec fun runProgram(state: State): State {
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

        return when (nextState.status) {
            Status.RUNNING -> runProgram(nextState)
            else -> nextState
        }
    }

    sealed class Instruction {
        abstract fun execute(state: State): State

        object Add : Instruction() {
            override fun execute(state: State): State {
                applyOpcode(state, Long::plus)
                return state.copy(pointer = state.pointer + 4)
            }
        }

        object Times : Instruction() {
            override fun execute(state: State): State {
                applyOpcode(state, Long::times)
                return state.copy(pointer = state.pointer + 4)
            }
        }

        object Update : Instruction() {
            override fun execute(state: State): State {
                return if (state.input.isEmpty()) state.copy(status = Status.PAUSED)
                else {
                    val writeAddress = getWriteAddress(
                        state.program,
                        state.pointer + 1,
                        state.parseMode(0),
                        state.relativeBase
                    )
                    state.program[writeAddress] = state.input[0]
                    state.copy(input = state.input.drop(1), pointer = state.pointer + 2)
                }
            }
        }

        object Output : Instruction() {
            override fun execute(state: State): State {
                val nextOutput = getValue(state.program, state.pointer + 1, state.parseMode(0), state.relativeBase)
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
                applyOpcode(state) { x, y -> if (x < y) 1 else 0 }
                return state.copy(pointer = state.pointer + 4)
            }
        }

        object Equals : Instruction() {
            override fun execute(state: State): State {
                applyOpcode(state) { x, y -> if (x == y) 1 else 0 }
                return state.copy(pointer = state.pointer + 4)
            }
        }

        object AdjustRelativeBase : Instruction() {
            override fun execute(state: State): State {
                val relativeBaseOffset =
                    getValue(state.program, state.pointer + 1, state.parseMode(0), state.relativeBase)
                return state.copy(pointer = state.pointer + 2, relativeBase = state.relativeBase + relativeBaseOffset)
            }
        }

        object Halt : Instruction() {
            override fun execute(state: State): State {
                return state.copy(status = Status.HALTED)
            }
        }
    }

    private fun applyOpcode(state: State, op: (Long, Long) -> Long): Unit {
        val x = getValue(state.program, state.pointer + 1, state.parseMode(0), state.relativeBase)
        val y = getValue(state.program, state.pointer + 2, state.parseMode(1), state.relativeBase)
        val writeAddress = getWriteAddress(state.program, state.pointer + 3, state.parseMode(2), state.relativeBase)
        state.program[writeAddress] = op(x, y)
    }

    private fun applyJumpOpcode(state: State, ifTrue: Boolean): Long {
        val x = getValue(state.program, state.pointer + 1, state.parseMode(0), state.relativeBase)
        val y = getValue(state.program, state.pointer + 2, state.parseMode(1), state.relativeBase)
        return if ((ifTrue && x != 0L) || (!ifTrue && x == 0L)) y else state.pointer + 3
    }

    private fun getValue(program: Map<Long, Long>, index: Long, mode: ParameterMode, relativeBase: Long): Long =
        when (mode) {
            ParameterMode.POSITION -> program[program[index]] ?: 0L
            ParameterMode.IMMEDIATE -> program[index] ?: 0L
            ParameterMode.RELATIVE -> program[relativeBase + (program[index] ?: 0L)] ?: 0L
        }

    private fun getWriteAddress(program: Map<Long, Long>, index: Long, mode: ParameterMode, relativeBase: Long): Long =
        when (mode) {
            ParameterMode.POSITION -> program[index] ?: 0L
            ParameterMode.IMMEDIATE -> throw IllegalArgumentException("Not allowed immediate mode for writes")
            ParameterMode.RELATIVE -> relativeBase + (program[index] ?: 0L)
        }

}

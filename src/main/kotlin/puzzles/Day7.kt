package puzzles

class Day7(rawInput: List<String>) {

    private val input: Map<Long, Long> = IntcodeComputer.parseInput(rawInput)

    fun part1(): Long {
        return permute(listOf(0L, 1L, 2L, 3L, 4L)).map { phases ->
            thrusterSignal(input, phases)
        }.max() ?: 0
    }

    fun part2(): Long {
        return permute(listOf(5L, 6L, 7L, 8L, 9L)).map { phases ->
            loopThrusterSignal(List(5) { input }, phases, listOf(0L, 0L, 0L, 0L, 0L))
        }.max() ?: 0
    }

    companion object {
        tailrec fun thrusterSignal(program: Map<Long, Long>, phases: List<Long>, input: Long = 0L): Long {
            return if (phases.isEmpty()) input
            else {
                val output = IntcodeComputer.runProgram(
                    IntcodeComputer.State(program, listOf(phases[0], input))
                ).output.last()
                thrusterSignal(program, phases.drop(1), output)
            }
        }

        tailrec fun loopThrusterSignal(
            programs: List<Map<Long, Long>>,
            phases: List<Long>,
            pointers: List<Long>,
            iteration: Int = 0,
            input: List<Long> = listOf(0L)
        ): Long {
            val amplifier = iteration % (phases.size)
            val program = programs[amplifier]
            val inputWithPhase = if (iteration < phases.size) listOf(phases[amplifier]) + input else input

            val programResult = IntcodeComputer.runProgram(
                IntcodeComputer.State(
                    program,
                    inputWithPhase,
                    pointers[amplifier],
                    status = IntcodeComputer.Status.RUNNING
                )
            )
            val output = programResult.output

            return if (amplifier == phases.size - 1 && programResult.status == IntcodeComputer.Status.HALTED)
                output.last()
            else loopThrusterSignal(
                programs.updated(amplifier, programResult.program),
                phases,
                pointers.updated(amplifier, programResult.pointer),
                iteration + 1, output
            )
        }
    }

    private fun <T> permute(input: List<T>): List<List<T>> {
        if (input.size == 1) return listOf(input)
        val perms = mutableListOf<List<T>>()
        val toInsert = input[0]
        for (perm in permute(input.drop(1))) {
            for (i in 0..perm.size) {
                val newPerm = perm.toMutableList()
                newPerm.add(i, toInsert)
                perms.add(newPerm)
            }
        }
        return perms
    }

}


fun main() {
    val day7 = Day7(Resources.input(7))
    println(day7.part1())
    println(day7.part2())
}

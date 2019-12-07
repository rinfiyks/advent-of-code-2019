package puzzles

class Day7(rawInput: List<String>) {

    private val input: List<Int> =
        rawInput.first().split(",").map { it.toInt() }

    fun part1(): Int {
        return permute(listOf(0, 1, 2, 3, 4)).map { phases ->
            thrusterSignal(input, phases)
        }.max() ?: 0
    }

    fun part2(): Int {
        return permute(listOf(5, 6, 7, 8, 9)).map { phases ->
            loopThrusterSignal(List(5) { input }, phases, listOf(0, 0, 0, 0, 0))
        }.max() ?: 0
    }

    companion object {
        tailrec fun thrusterSignal(program: List<Int>, phases: List<Int>, input: Int = 0): Int {
            return if (phases.isEmpty()) input
            else {
                val output = IntcodeComputer.runProgram(
                    IntcodeComputer.State(program, listOf(phases[0], input))
                ).output.last()
                thrusterSignal(program, phases.drop(1), output)
            }
        }

        tailrec fun loopThrusterSignal(
            programs: List<List<Int>>,
            phases: List<Int>,
            pointers: List<Int>,
            iteration: Int = 0,
            input: List<Int> = listOf(0)
        ): Int {
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

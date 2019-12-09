package puzzles

import kotlin.system.measureNanoTime

class Day9(rawInput: List<String>) {

    private val input: Map<Long, Long> = IntcodeComputer.parseInput(rawInput)

    fun part1(): Long {
        return IntcodeComputer.runProgram(IntcodeComputer.State(input.toMutableMap(), listOf(1))).output.last()
    }

    fun part2(): Long {
        return IntcodeComputer.runProgram(IntcodeComputer.State(input.toMutableMap(), listOf(2))).output.last()
    }

}

fun main() {
    val day9 = Day9(Resources.input(9))
    println(day9.part1())
    val time = measureNanoTime { println(day9.part2()) }
    println("$time ns (${time / 1000000} ms)")
}

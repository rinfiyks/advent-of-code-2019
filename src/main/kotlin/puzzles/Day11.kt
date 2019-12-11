package puzzles

import java.lang.IllegalArgumentException
import java.lang.StringBuilder

class Day11(rawInput: List<String>) {

    enum class Direction {
        LEFT, UP, RIGHT, DOWN
    }

    data class Robot(val position: Pair<Int, Int>, val direction: Direction) {
        fun rotateLeft() =
            this.copy(direction = Direction.values()[(direction.ordinal + 3) % 4])

        fun rotateRight() =
            this.copy(direction = Direction.values()[(direction.ordinal + 1) % 4])

        fun move() = this.copy(
            position = when (direction) {
                Direction.LEFT -> position.copy(first = position.first - 1)
                Direction.UP -> position.copy(second = position.second + 1)
                Direction.RIGHT -> position.copy(first = position.first + 1)
                Direction.DOWN -> position.copy(second = position.second - 1)
            }
        )
    }

    private val input: Map<Long, Long> = IntcodeComputer.parseInput(rawInput)

    fun part1(): Int {
        val result = runRobot(
            IntcodeComputer.State(input.toMutableMap()),
            Robot(Pair(0, 0), Direction.UP), emptyMap()
        )
        return result.size
    }

    fun part2(): String {
        val result = runRobot(
            IntcodeComputer.State(input.toMutableMap()),
            Robot(Pair(0, 0), Direction.UP), mapOf(Pair(Pair(0, 0), 1))
        )
        return displayHull(result)
    }

    private tailrec fun runRobot(
        state: IntcodeComputer.State,
        robot: Robot,
        hull: Map<Pair<Int, Int>, Int>
    ): Map<Pair<Int, Int>, Int> {
        val input = listOf((hull[robot.position] ?: 0).toLong())
        val programResult = IntcodeComputer.runProgram(state.copy(input = input))
        return when (programResult.status) {
            IntcodeComputer.Status.HALTED -> hull
            else -> {
                val paintColour = programResult.output[0].toInt()
                val turn = programResult.output[1].toInt()
                val nextHull = hull.updated(robot.position, paintColour)
                val nextRobot = when (turn) {
                    0 -> robot.rotateLeft()
                    1 -> robot.rotateRight()
                    else -> throw IllegalArgumentException("Turn code $turn is not valid")
                }.move()

                val nextState = programResult.copy(
                    output = emptyList(),
                    status = IntcodeComputer.Status.RUNNING
                )
                runRobot(nextState, nextRobot, nextHull)
            }
        }
    }

    private fun displayHull(hull: Map<Pair<Int, Int>, Int>): String {
        val minX = hull.keys.map { it.first }.min()!!
        val maxX = hull.keys.map { it.first }.max()!!
        val minY = hull.keys.map { it.second }.min()!!
        val maxY = hull.keys.map { it.second }.max()!!
        val something = List(maxY - minY + 1) { column ->
            List(maxX - minX + 1) { row ->
                hull[Pair(row + minX, column + minY)] ?: 0
            }
        }

        val result = StringBuilder()
        something.reversed().forEach { row ->
            row.forEach { pixel ->
                when (pixel) {
                    0 -> result.append(" ")
                    else -> result.append("#")
                }
            }
            result.append("\n")
        }
        return result.toString()
    }
}

fun main() {
    val day11 = Day11(Resources.input(11))
    println(day11.part1())
    println(day11.part2())
}

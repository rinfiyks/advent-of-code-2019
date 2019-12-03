package puzzles

import java.lang.IllegalArgumentException
import kotlin.math.abs

class Day3(rawInput: List<String>) {

    data class Step(val direction: String, val distance: Int)

    data class Position(val x: Int, val y: Int) {

        fun addStep(step: Step): Position = when (step.direction) {
            "L" -> Position(x - step.distance, y)
            "R" -> Position(x + step.distance, y)
            "U" -> Position(x, y + step.distance)
            "D" -> Position(x, y - step.distance)
            else -> throw IllegalArgumentException("Invalid direction: ${step.direction}")
        }

        fun manhattanFromOrigin() = abs(x) + abs(y)
        fun manhattan(p2: Position) = abs(x - p2.x) + abs(y - p2.y)

    }

    data class Segment(val p1: Position, val p2: Position, val previousDistance: Int)

    private val input: List<List<Step>> = rawInput.map { it.split(",") }
        .map { line -> line.map { Step(it.substring(0, 1), it.substring(1).toInt()) } }

    private val steps1: List<Step> = input[0]
    private val steps2: List<Step> = input[1]
    private val segments1 = stepsToSegments(steps1)
    private val segments2 = stepsToSegments(steps2)

    fun part1(): Int {
        val intersections = getIntersectionPositions(segments1, segments2)
        return intersections.map { it.manhattanFromOrigin() }.sorted()[1]
    }

    fun part2(): Int {
        val intersections = getIntersectionDistances(segments1, segments2)
        return intersections.sorted()[1]
    }

    private fun stepsToSegments(
        steps: List<Step>,
        segments: List<Segment> = emptyList(),
        position: Position = Position(0, 0)
    ): List<Segment> {
        return if (steps.isEmpty()) segments
        else {
            val newPosition = position.addStep(steps[0])
            val s = segments.lastOrNull()
            val previousDistance = if (s != null) (s.previousDistance + s.p1.manhattan(s.p2))
            else 0
            val nextSegment = Segment(position, newPosition, previousDistance)
            stepsToSegments(steps.drop(1), segments + nextSegment, newPosition)
        }
    }

    private fun getIntersectionPositions(segments1: List<Segment>, segments2: List<Segment>): List<Position> {
        return segments1.flatMap { s1 ->
            segments2.map { s2 ->
                intersectionPosition(s1, s2)
            }
        }.filterNotNull()
    }

    private fun intersectionPosition(s1: Segment, s2: Segment): Position? {
        return if (intersects(s1, s2) || intersects(s2, s1)) {
            val xPositions = listOf(s1.p1.x, s1.p2.x, s2.p1.x, s2.p2.x).sorted()
            val yPositions = listOf(s1.p1.y, s1.p2.y, s2.p1.y, s2.p2.y).sorted()
            Position(xPositions[1], yPositions[1])
        } else null
    }

    private fun getIntersectionDistances(segments1: List<Segment>, segments2: List<Segment>): List<Int> {
        return segments1.flatMap { s1 ->
            segments2.map { s2 ->
                intersectionDistance(s1, s2)
            }
        }.filterNotNull()
    }

    private fun intersectionDistance(s1: Segment, s2: Segment): Int? {
        return if (intersects(s1, s2) || intersects(s2, s1)) {
            val xPositions = listOf(s1.p1.x, s1.p2.x, s2.p1.x, s2.p2.x).sorted()
            val yPositions = listOf(s1.p1.y, s1.p2.y, s2.p1.y, s2.p2.y).sorted()
            val extraXDistance = Math.max(abs(s1.p1.x - xPositions[1]), abs(s2.p1.x - xPositions[1]))
            val extraYDistance = Math.max(abs(s1.p1.y - yPositions[1]), abs(s2.p1.y - yPositions[1]))
            s1.previousDistance + s2.previousDistance + extraXDistance + extraYDistance
        } else null
    }

    private fun intersects(s1: Segment, s2: Segment): Boolean {
        return (Math.min(s1.p1.x, s1.p2.x) <= s2.p1.x
                && Math.max(s1.p1.x, s1.p2.x) >= s2.p1.x
                && Math.min(s2.p1.y, s2.p2.y) <= s1.p1.y
                && Math.max(s2.p1.y, s2.p2.y) >= s1.p1.y)
    }

}

fun main() {
    val day3 = Day3(Resources.input(3))
    println(day3.part1())
    println(day3.part2())
}

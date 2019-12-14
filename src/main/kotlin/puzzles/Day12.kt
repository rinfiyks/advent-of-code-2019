package puzzles

import puzzles.MathUtil.lcm
import kotlin.math.abs

class Day12(rawInput: List<String>) {

    data class Moon(val position: List<Int>, val velocity: List<Int>)

    private val input: List<Moon> = parseInput(rawInput)

    fun part1(): Int {
        val result = simulate(input, 1000)
        return totalEnergy(result)
    }

    fun part2(): Long {
        return stepsToRepeat(input)
    }

    companion object {
        fun parseInput(input: List<String>): List<Moon> = input.map { line ->
            val s = line.split("[^-0-9]+".toRegex())
            Moon(listOf(s[1].toInt(), s[2].toInt(), s[3].toInt()), listOf(0, 0, 0))
        }

        fun simulate(moons: List<Moon>, steps: Int): List<Moon> {
            return if (steps == 0) moons
            else {
                // apply gravity to update velocities
                val updatedVelocities = moons.map { moon ->
                    val velocityChange = moons.map { m ->
                        m.position.zip(moon.position) { a, b ->
                            (a - b).coerceIn(-1, 1)
                        }
                    }.reduce { l1, l2 -> l1.add(l2) }
                    moon.copy(velocity = moon.velocity.add(velocityChange))
                }
                // apply velocity to update positions
                val updatedPositions = updatedVelocities.map { moon ->
                    moon.copy(position = moon.position.add(moon.velocity))
                }
                simulate(updatedPositions, steps - 1)
            }
        }

        fun totalEnergy(moons: List<Moon>): Int = moons.sumBy { moon ->
            moon.position.map { abs(it) }.sum() *
                    moon.velocity.map { abs(it) }.sum()
        }

        fun stepsToRepeat(moons: List<Moon>): Long {
            // split into x y z components
            val separatedComponents = (moons[0].position.indices).map { component ->
                moons.map { moon ->
                    Moon(listOf(moon.position[component]), listOf(moon.velocity[component]))
                }
            }
            val stepsToRepeatPerComponent = separatedComponents.map {
                countSteps(it)
            }
             return lcm(stepsToRepeatPerComponent)
        }

        private tailrec fun countSteps(
            moons: List<Moon>,
            seen: MutableSet<List<Moon>> = mutableSetOf(),
            count: Long = 1
        ): Long {
            val nextState = simulate(moons, 1)
            return if (seen.contains(nextState)) count
            else {
                seen.add(moons)
                countSteps(nextState, seen, count + 1)
            }
        }

        private fun List<Int>.add(list: List<Int>): List<Int> =
            this.zip(list) { a, b -> a + b }
    }

}

fun main() {
    val day12 = Day12(Resources.input(12))
    println(day12.part1())
    println(day12.part2())
}

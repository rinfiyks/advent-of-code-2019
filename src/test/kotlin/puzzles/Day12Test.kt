package puzzles

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day12Test {

    @Test
    fun testPart1() {
        val input = """
            <x=-1, y=0, z=2>
            <x=2, y=-10, z=-7>
            <x=4, y=-8, z=8>
            <x=3, y=5, z=-1>
        """.trimIndent().split("\n")
        val moons = Day12.parseInput(input)
        assertThat(Day12.totalEnergy(Day12.simulate(moons, 10))).isEqualTo(179)
    }

    @Test
    fun testPart2() {
        val input = """
            <x=-8, y=-10, z=0>
            <x=5, y=5, z=10>
            <x=2, y=-7, z=3>
            <x=9, y=-8, z=-3>
        """.trimIndent().split("\n")
        val moons = Day12.parseInput(input)
        assertThat(Day12.stepsToRepeat(moons)).isEqualTo(4686774924L)
    }

}

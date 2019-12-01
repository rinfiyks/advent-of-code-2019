package puzzles

import org.assertj.core.api.Assertions.assertThat

internal class Day1Test {

    @org.junit.jupiter.api.Test
    fun part1() {
        assertThat(Day1(listOf(12, 14, 1969, 100756)).part1()).isEqualTo(34241)
    }

    @org.junit.jupiter.api.Test
    fun part2() {
        assertThat(Day1(listOf(1969)).part2()).isEqualTo(966)
        assertThat(Day1(listOf(100756)).part2()).isEqualTo(50346)
    }
}

package puzzles

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Day6Test {


    @Test
    fun testPart1() {
        val input = """
            COM)B
            B)C
            C)D
            D)E
            E)F
            B)G
            G)H
            D)I
            E)J
            J)K
            K)L
        """.trimIndent().split("\n")
        assertThat(Day6(input).part1()).isEqualTo(42)
    }

    @Test
    fun testPart2() {
        val input = """
            COM)B
            B)C
            C)D
            D)E
            E)F
            B)G
            G)H
            D)I
            E)J
            J)K
            K)L
            K)YOU
            I)SAN
        """.trimIndent().split("\n")
        assertThat(Day6(input).part2()).isEqualTo(4)
    }

}

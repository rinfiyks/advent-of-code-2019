package puzzles

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Day4Test {

    @Test
    fun testPart1Criteria() {
        assertThat(Day4.part1Criteria(111111)).isTrue()
        assertThat(Day4.part1Criteria(223450)).isFalse()
        assertThat(Day4.part1Criteria(123789)).isFalse()
    }

    @Test
    fun testPart2Criteria() {
        assertThat(Day4.part2Criteria(112233)).isTrue()
        assertThat(Day4.part2Criteria(123444)).isFalse()
        assertThat(Day4.part2Criteria(111122)).isTrue()
    }

}

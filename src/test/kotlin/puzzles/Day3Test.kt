package puzzles

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Day3Test {

    @Test
    fun testPart1() {
        assertThat(Day3(listOf("R8,U5,L5,D3", "U7,R6,D4,L4")).part1()).isEqualTo(6)
        assertThat(Day3(listOf("R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83")).part1()).isEqualTo(159)
        assertThat(Day3(listOf("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")).part1()).isEqualTo(135)
    }

    @Test
    fun testPart2() {
        assertThat(Day3(listOf("R8,U5,L5,D3", "U7,R6,D4,L4")).part2()).isEqualTo(30)
        assertThat(Day3(listOf("R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83")).part2()).isEqualTo(610)
        assertThat(Day3(listOf("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")).part2()).isEqualTo(410)
    }

}

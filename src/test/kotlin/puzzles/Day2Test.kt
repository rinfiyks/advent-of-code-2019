package puzzles

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Day2Test {

    @Test
    fun testRunProgram() {
        assertThat(IntcodeComputer.runProgram(listOf(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50)))
            .isEqualTo(listOf(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50))

        assertThat(IntcodeComputer.runProgram(listOf(1, 0, 0, 0, 99)))
            .isEqualTo(listOf(2, 0, 0, 0, 99))

        assertThat(IntcodeComputer.runProgram(listOf(2, 3, 0, 3, 99)))
            .isEqualTo(listOf(2, 3, 0, 6, 99))

        assertThat(IntcodeComputer.runProgram(listOf(2, 4, 4, 5, 99, 0)))
            .isEqualTo(listOf(2, 4, 4, 5, 99, 9801))

        assertThat(IntcodeComputer.runProgram(listOf(1, 1, 1, 4, 99, 5, 6, 0, 99)))
            .isEqualTo(listOf(30, 1, 1, 4, 2, 5, 6, 0, 99))
    }

}

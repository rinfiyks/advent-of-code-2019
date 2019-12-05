package puzzles

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class IntcodeComputerTest {

    @Test
    fun testRunProgramDay2() {
        assertThat(IntcodeComputer.runProgram(listOf(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50)).program)
            .isEqualTo(listOf(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50))

        assertThat(IntcodeComputer.runProgram(listOf(1, 0, 0, 0, 99)).program)
            .isEqualTo(listOf(2, 0, 0, 0, 99))

        assertThat(IntcodeComputer.runProgram(listOf(2, 3, 0, 3, 99)).program)
            .isEqualTo(listOf(2, 3, 0, 6, 99))

        assertThat(IntcodeComputer.runProgram(listOf(2, 4, 4, 5, 99, 0)).program)
            .isEqualTo(listOf(2, 4, 4, 5, 99, 9801))

        assertThat(IntcodeComputer.runProgram(listOf(1, 1, 1, 4, 99, 5, 6, 0, 99)).program)
            .isEqualTo(listOf(30, 1, 1, 4, 2, 5, 6, 0, 99))
    }

    @Test
    fun testRunProgramDay5Part1() {
        assertThat(IntcodeComputer.runProgram(listOf(1002, 4, 3, 4, 33)).program)
            .isEqualTo(listOf(1002, 4, 3, 4, 99))

        assertThat(IntcodeComputer.runProgram(listOf(1101, 100, -1, 4, 0)).program)
            .isEqualTo(listOf(1101, 100, -1, 4, 99))
    }

    @Test
    fun testRunProgramDay5Part2() {
        assertThat(IntcodeComputer.runProgram(listOf(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8), 8).output.last())
            .isEqualTo(1)

        assertThat(IntcodeComputer.runProgram(listOf(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8), 7).output.last())
            .isEqualTo(0)

        assertThat(IntcodeComputer.runProgram(listOf(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8), 9).output.last())
            .isEqualTo(0)

        assertThat(IntcodeComputer.runProgram(listOf(3, 3, 1107, -1, 8, 3, 4, 3, 99), 8).output.last())
            .isEqualTo(0)

        assertThat(IntcodeComputer.runProgram(listOf(3, 3, 1107, -1, 8, 3, 4, 3, 99), 4).output.last())
            .isEqualTo(1)

        assertThat(IntcodeComputer.runProgram(listOf(3, 3, 1107, -1, 8, 3, 4, 3, 99), 9).output.last())
            .isEqualTo(0)

        val largerExample = listOf(
            3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
            1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
            999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99
        )

        assertThat(IntcodeComputer.runProgram(largerExample, 6).output.last())
            .isEqualTo(999)

        assertThat(IntcodeComputer.runProgram(largerExample, 8).output.last())
            .isEqualTo(1000)

        assertThat(IntcodeComputer.runProgram(largerExample, 10).output.last())
            .isEqualTo(1001)
    }

}

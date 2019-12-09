package puzzles

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class IntcodeComputerTest {

    @Test
    fun testRunProgramDay2() {
        assertThat(IntcodeComputer.runProgram(IntcodeComputer.State(IntcodeComputer.parseInput(listOf("1,9,10,3,2,3,11,0,99,30,40,50")).toMutableMap())).program.values.toList())
            .isEqualTo(listOf(3500L, 9L, 10L, 70L, 2L, 3L, 11L, 0L, 99L, 30L, 40L, 50L))

        assertThat(IntcodeComputer.runProgram(IntcodeComputer.State(IntcodeComputer.parseInput(listOf("1,0,0,0,99")).toMutableMap())).program.values.toList())
            .isEqualTo(listOf(2L, 0L, 0L, 0L, 99L))

        assertThat(IntcodeComputer.runProgram(IntcodeComputer.State(IntcodeComputer.parseInput(listOf("2,3,0,3,99")).toMutableMap())).program.values.toList())
            .isEqualTo(listOf(2L, 3L, 0L, 6L, 99L))

        assertThat(IntcodeComputer.runProgram(IntcodeComputer.State(IntcodeComputer.parseInput(listOf("2,4,4,5,99,0")).toMutableMap())).program.values.toList())
            .isEqualTo(listOf(2L, 4L, 4L, 5L, 99L, 9801L))

        assertThat(IntcodeComputer.runProgram(IntcodeComputer.State(IntcodeComputer.parseInput(listOf("1,1,1,4,99,5,6,0,99")).toMutableMap())).program.values.toList())
            .isEqualTo(listOf(30L, 1L, 1L, 4L, 2L, 5L, 6L, 0L, 99L))
    }

    @Test
    fun testRunProgramDay5Part1() {
        assertThat(IntcodeComputer.runProgram(IntcodeComputer.State(IntcodeComputer.parseInput(listOf("1002,4,3,4,33")).toMutableMap())).program.values.toList())
            .isEqualTo(listOf(1002L, 4L, 3L, 4L, 99L))

        assertThat(IntcodeComputer.runProgram(IntcodeComputer.State(IntcodeComputer.parseInput(listOf("1101,100,-1,4,0")).toMutableMap())).program.values.toList())
            .isEqualTo(listOf(1101L, 100L, -1L, 4L, 99L))
    }

    @Test
    fun testRunProgramDay5Part2() {
        assertThat(
            IntcodeComputer.runProgram(
                IntcodeComputer.State(
                    IntcodeComputer.parseInput(listOf("3,9,8,9,10,9,4,9,99,-1,8")).toMutableMap(),
                    listOf(8)
                )
            ).output.last()
        )
            .isEqualTo(1)

        assertThat(
            IntcodeComputer.runProgram(
                IntcodeComputer.State(
                    IntcodeComputer.parseInput(listOf("3,9,8,9,10,9,4,9,99,-1,8")).toMutableMap(),
                    listOf(7)
                )
            ).output.last()
        )
            .isEqualTo(0)

        assertThat(
            IntcodeComputer.runProgram(
                IntcodeComputer.State(
                    IntcodeComputer.parseInput(listOf("3,9,8,9,10,9,4,9,99,-1,8")).toMutableMap(),
                    listOf(9)
                )
            ).output.last()
        )
            .isEqualTo(0)

        assertThat(
            IntcodeComputer.runProgram(
                IntcodeComputer.State(
                    IntcodeComputer.parseInput(listOf("3,3,1107,-1,8,3,4,3,99")).toMutableMap(),
                    listOf(8)
                )
            ).output.last()
        )
            .isEqualTo(0)

        assertThat(
            IntcodeComputer.runProgram(
                IntcodeComputer.State(
                    IntcodeComputer.parseInput(listOf("3,3,1107,-1,8,3,4,3,99")).toMutableMap(),
                    listOf(4)
                )
            ).output.last()
        )
            .isEqualTo(1)

        assertThat(
            IntcodeComputer.runProgram(
                IntcodeComputer.State(
                    IntcodeComputer.parseInput(listOf("3,3,1107,-1,8,3,4,3,99")).toMutableMap(),
                    listOf(9)
                )
            ).output.last()
        )
            .isEqualTo(0)

        val largerExample =
            IntcodeComputer.parseInput(listOf("3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99")).toMutableMap()

        assertThat(IntcodeComputer.runProgram(IntcodeComputer.State(largerExample, listOf(6))).output.last())
            .isEqualTo(999)

        assertThat(IntcodeComputer.runProgram(IntcodeComputer.State(largerExample, listOf(8))).output.last())
            .isEqualTo(1000)

        assertThat(IntcodeComputer.runProgram(IntcodeComputer.State(largerExample, listOf(10))).output.last())
            .isEqualTo(1001)
    }

    @Test
    fun testRunProgramDay9Part1() {
        assertThat(IntcodeComputer.runProgram(IntcodeComputer.State(IntcodeComputer.parseInput(listOf("109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99")).toMutableMap())).output)
            .isEqualTo(listOf(109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99).map { it.toLong() })

        assertThat(IntcodeComputer.runProgram(IntcodeComputer.State(IntcodeComputer.parseInput(listOf("1102,34915192,34915192,7,4,7,99,0")).toMutableMap())).output)
            .isEqualTo(listOf(1219070632396864L))

        assertThat(IntcodeComputer.runProgram(IntcodeComputer.State(IntcodeComputer.parseInput(listOf("104,1125899906842624,99")).toMutableMap())).output)
            .isEqualTo(listOf(1125899906842624L))
    }

}

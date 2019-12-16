package puzzles

class Day13(rawInput: List<String>) {

    enum class Tile {
        EMPTY, WALL, BLOCK, PADDLE, BALL
    }

    private val input: Map<Long, Long> = IntcodeComputer.parseInput(rawInput)

    fun part1(): Int {
        val state = IntcodeComputer.State(input.toMutableMap())
        val output = IntcodeComputer.runProgram(state).output
        val tiles: MutableMap<Pair<Long, Long>, Tile> = mutableMapOf()
        updateTiles(tiles, output)
        return tiles.count { it.value == Tile.BLOCK }
    }

    fun part2(): Long {
        val program = input.toMutableMap()
        program[0] = 2
        return playGame(IntcodeComputer.State(program, listOf(0L)), mutableMapOf())
    }

    companion object {
        tailrec fun playGame(state: IntcodeComputer.State, tiles: MutableMap<Pair<Long, Long>, Tile>): Long {
            return when (state.status) {
                IntcodeComputer.Status.RUNNING -> playGame(IntcodeComputer.runProgram(state), tiles)
                IntcodeComputer.Status.PAUSED -> {
                    updateTiles(tiles, state.output)
                    val ballX = tiles.filterValues { it == Tile.BALL }.keys.first().first
                    val paddleX = tiles.filterValues { it == Tile.PADDLE }.keys.first().first
                    val joystick = (ballX - paddleX).coerceIn(-1, 1)
                    val updatedState = state.copy(
                        input = listOf(joystick), output = emptyList(),
                        status = IntcodeComputer.Status.RUNNING
                    )
                    playGame(IntcodeComputer.runProgram(updatedState), tiles)
                }
                IntcodeComputer.Status.HALTED -> score(state.output)
            }
        }

        fun updateTiles(tiles: MutableMap<Pair<Long, Long>, Tile>, programOutput: List<Long>) {
            programOutput.chunked(3).forEach { instructions ->
                if (instructions[0] != -1L) {
                    tiles[Pair(instructions[0], instructions[1])] =
                        Tile.values()[instructions[2].toInt()]
                }
            }
        }

        private fun score(programOutput: List<Long>): Long =
            programOutput.chunked(3).find { it[0] == -1L && it[1] == 0L }!![2]
    }

}

fun main() {
    val day13 = Day13(Resources.input(13))
    println(day13.part1())
    println(day13.part2())
}

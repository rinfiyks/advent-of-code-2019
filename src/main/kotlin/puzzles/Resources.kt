package puzzles

import java.io.File

object Resources {

    fun input(day: Int): List<String> =
        File(Resources::class.java.getResource("/puzzles/input/day${day}.txt").toURI())
            .readLines()

}

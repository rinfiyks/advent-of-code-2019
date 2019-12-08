package puzzles

import java.lang.StringBuilder

class Day8(rawInput: List<String>) {

    private val width = 25
    private val height = 6
    private val input =
        rawInput[0].map { it.toString().toInt() }
            .chunked(width * height)

    fun part1(): Int {
        val layerWithFewestDigits = input.minBy { layer ->
            layer.count { it == 0 }
        } ?: listOf()
        return layerWithFewestDigits.count { it == 1 } *
                layerWithFewestDigits.count { it == 2 }
    }

    fun part2(): String {
        val image = convertToImage(input, width, height)
        return imageToString(image, width, height)
    }

    companion object {
        fun convertToImage(input: List<List<Int>>, width: Int, height: Int): List<Int> {
            return List(width * height) { i ->
                val pixelWithLayers = List(input.size) { input[it][i] }
                pixelWithLayers.find { it != 2 } ?: 2
            }
        }

        fun imageToString(image: List<Int>, width: Int, height: Int): String {
            val result = StringBuilder()
            for (h in 0 until height) {
                for (w in 0 until width) {
                    when (image[w + (h * width)]) {
                        0 -> result.append(" ")
                        else -> result.append("#")
                    }
                }
                result.append("\n")
            }
            return result.toString()
        }
    }

}

fun main() {
    val day8 = Day8(Resources.input(8))
    println(day8.part1())
    println(day8.part2())
}

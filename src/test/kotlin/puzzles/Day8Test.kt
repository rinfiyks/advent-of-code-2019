package puzzles

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Day8Test {

    @Test
    fun testImageConversion() {
        val width = 2
        val height = 2
        val imageData = "0222112222120000".map { it.toString().toInt() }.chunked(width * height)
        val image = Day8.convertToImage(imageData, width, height)
        assertThat(Day8.imageToString(image, width, height)).isEqualTo(" #\n# \n")
    }

}

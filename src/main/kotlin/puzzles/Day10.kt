package puzzles

import kotlin.math.abs
import kotlin.math.atan2

class Day10(rawInput: List<String>) {

    data class Laser(val position: Pair<Int, Int>, val direction: Pair<Int, Int>)

    data class Asteroid(val position: Pair<Int, Int>, val angle: Double)

    private val input: List<Pair<Int, Int>> = rawInput.mapIndexed { y, s ->
        s.mapIndexed { x, c ->
            if (c == '#') Pair(x, y) else null
        }.filterNotNull()
    }.flatten()

    fun part1(): Int {
        return input.map {
            countVisibleAsteroids(it, input)
        }.max()!!
    }

    fun part2(): Int {
        val laserPosition = input.maxBy { countVisibleAsteroids(it, input) }!!
        val laser = Laser(laserPosition, Pair(0, -1))

        val asteroids: List<Asteroid> = getDeltas(laserPosition, input).map {
            val gcdPos = gcdReduce(it)
            val angle = atan2(gcdPos.first.toDouble(), gcdPos.second.toDouble())
            Asteroid(it, angle)
        }.filterNot { it.position == Pair(0, 0) }.sortedBy { -it.angle }

        val asteroid200 = vaporise(asteroids, laser)
        return (laserPosition.first + asteroid200.first) * 100 + (laserPosition.second + asteroid200.second)
    }

    private fun countVisibleAsteroids(asteroid: Pair<Int, Int>, allAsteroids: List<Pair<Int, Int>>): Int {
        return getDeltas(asteroid, allAsteroids).map { delta ->
            gcdReduce(delta)
        }.distinct().count() - 1
    }

    private fun gcdReduce(pair: Pair<Int, Int>): Pair<Int, Int> {
        val gcd = gcd(abs(pair.first), abs(pair.second)).coerceAtLeast(1)
        return Pair(pair.first / gcd, pair.second / gcd)
    }

    private fun getDeltas(asteroid: Pair<Int, Int>, allAsteroids: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
        return allAsteroids.map { a ->
            val xDiff = a.first - asteroid.first
            val yDiff = a.second - asteroid.second
            Pair(xDiff, yDiff)
        }
    }

    private tailrec fun vaporise(asteroidsWithDeltas: List<Asteroid>, laser: Laser, count: Int = 1): Pair<Int, Int> {
        // shoot the closest asteroid with same angle
        val sameAngle = asteroidsWithDeltas.filter { d -> gcdReduce(d.position) == laser.direction }
        val toShoot = sameAngle.minBy { d -> abs(d.position.first) + abs(d.position.second) }!!
        return if (count == 200) toShoot.position
        else {
            // remove the destroyed asteroid
            val nextAllDeltas = asteroidsWithDeltas.filterNot { it == toShoot }
            // rotate
            val nextAsteroid = asteroidsWithDeltas.find { it.angle < toShoot.angle }
                ?: asteroidsWithDeltas.maxBy { it.angle }!!
            val nextDirection = gcdReduce(nextAsteroid.position)
            vaporise(nextAllDeltas, laser.copy(direction = nextDirection), count + 1)
        }
    }

    private tailrec fun gcd(a: Int, b: Int): Int =
        if (b == 0) a else gcd(b, a % b)

}

fun main() {
    val day10 = Day10(Resources.input(10))
    println(day10.part1())
    println(day10.part2())
}

package puzzles

class Day6(rawInput: List<String>) {

    private val input: Map<String, List<String>> =
        parseInput(rawInput.map {
            val s = it.split(")")
            Pair(s[0], s[1])
        })

    fun part1(): Int {
        return countOrbits(input)
    }

    fun part2(): Int {
        val yourParents = getParents(input, "YOU")
        val santasParents = getParents(input, "SAN")
        return countTransfersRequired(yourParents, santasParents)
    }

    private tailrec fun parseInput(
        input: List<Pair<String, String>>,
        objectMap: MutableMap<String, List<String>> = mutableMapOf()
    ): Map<String, List<String>> {
        return if (input.isEmpty()) objectMap
        else {
            val orbitee = input[0].first
            val orbiter = input[0].second
            val orbiters = objectMap.getOrDefault(orbitee, emptyList()) + orbiter
            objectMap[orbitee] = orbiters
            if (!objectMap.containsKey(orbiter)) objectMap[orbiter] = emptyList()
            parseInput(input.drop(1), objectMap)
        }
    }

    private tailrec fun countOrbits(
        objectMap: Map<String, List<String>>,
        objects: List<Pair<String, Int>> = listOf(Pair("COM", 0)),
        count: Int = 0
    ): Int {
        return if (objects.isEmpty()) count
        else {
            val currentObject = objects[0].first
            val currentDepth = objects[0].second
            val children = objectMap.getOrDefault(currentObject, emptyList())
                .map { Pair(it, currentDepth + 1) }
            val newObjects: List<Pair<String, Int>> = objects.drop(1) + children
            countOrbits(objectMap, newObjects, count + currentDepth)
        }
    }

    private tailrec fun getParents(
        objectMap: Map<String, List<String>>,
        name: String,
        acc: List<String> = emptyList()
    ): List<String> {
        return if (name == "COM") acc
        else {
            val parent = objectMap.filterValues { it.contains(name) }.keys.toList()[0]
            getParents(objectMap, parent, acc + parent)
        }
    }

    private tailrec fun countTransfersRequired(p1: List<String>, p2: List<String>): Int {
        return if (p1.last() == p2.last()) countTransfersRequired(p1.dropLast(1), p2.dropLast(1))
        else p1.size + p2.size
    }

}

fun main() {
    val day6 = Day6(Resources.input(6))
    println(day6.part1())
    println(day6.part2())
}

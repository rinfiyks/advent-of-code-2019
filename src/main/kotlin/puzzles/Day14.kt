package puzzles

class Day14(rawInput: List<String>) {

    data class Chemical(val name: String, val quantity: Long)

    private val input: Map<Chemical, List<Chemical>> = parseInput(rawInput)

    fun part1(): Long {
        return recurseFormulas(input)
    }

    fun part2(): Long {
        return binarySearch(input, 0, 1000000000, 1000000000000)
    }

    companion object {
        fun parseInput(input: List<String>): Map<Chemical, List<Chemical>> {
            return input.map { line ->
                val split = line.split("=>")
                val output: List<String> = split[1].trim().split(" ")
                val outputChemical = Chemical(output[1], output[0].toLong())
                val inputChemicals = split[0].trim().split(", ").map {
                    val chemical = it.trim().split(" ")
                    Chemical(chemical[1], chemical[0].toLong())
                }
                outputChemical to inputChemicals
            }.toMap()
        }

        tailrec fun recurseFormulas(
            formulas: Map<Chemical, List<Chemical>>,
            chemicals: List<Chemical> = listOf(Chemical("FUEL", 1))
        ): Long {
            val (requiredChemicals, otherChemicals) = chemicals.partition { it.quantity > 0 && it.name != "ORE" }
            return if (requiredChemicals.isEmpty()) chemicals.find { it.name == "ORE" }!!.quantity
            else {
                val chemicalToReplace = requiredChemicals.first()
                val formula = formulas.filterKeys { it.name == chemicalToReplace.name }.entries.first()
                val scaledFormula = scaleFormula(formula, chemicalToReplace.quantity)
                val updatedChemical =
                    chemicalToReplace.copy(quantity = chemicalToReplace.quantity - scaledFormula.first.quantity)
                val nextChemicals =
                    balanceChemicals(listOf(updatedChemical) + scaledFormula.second + requiredChemicals.drop(1) + otherChemicals)
                recurseFormulas(formulas, nextChemicals)
            }
        }

        tailrec fun binarySearch(input: Map<Chemical, List<Chemical>>, low: Long, high: Long, target: Long): Long {
            val value = low + (high - low) / 2
            val res = recurseFormulas(input, listOf(Chemical("FUEL", value)))
            return if (low + 1 == high) low
            else if (res < target) binarySearch(input, value, high, target)
            else if (res > target) binarySearch(input, low, value, target)
            else value
        }

        private fun scaleFormula(
            formula: Map.Entry<Chemical, List<Chemical>>,
            quantity: Long
        ): Pair<Chemical, List<Chemical>> {
            val scale = quantity / formula.key.quantity + (quantity % formula.key.quantity).coerceAtMost(1)
            val scaledValues = formula.value.map { chemical -> chemical.copy(quantity = chemical.quantity * scale) }
            return Chemical(formula.key.name, formula.key.quantity * scale) to scaledValues
        }

        private fun balanceChemicals(chemicals: List<Chemical>): List<Chemical> =
            chemicals.groupBy { it.name }.entries.map { entry ->
                Chemical(entry.key, entry.value.map { it.quantity }.sum())
            }
    }
}


fun main() {
    val day14 = Day14(Resources.input(14))
    println(day14.part1())
    println(day14.part2())
}

const val BLANK = 0
const val WIDTH = 9
const val HEIGHT = 9

class Sudoku(vararg rows: Iterable<Int>) {

    private val mask = Array(WIDTH * HEIGHT) { 0 }
    val input = Array(WIDTH * HEIGHT) { BLANK }

    private val options = (1..9).toSet()

    private var solution = emptyArray<Int>()

    init {
        check(rows.count() == 9) { "Sudoku doesn't have 9 rows" }
        check(rows.all { it.count() == 9 }) { "Every row should be 9 input" }
        check(rows.all { it.all { c -> (BLANK..9).contains(c) } }) { "Cell values range from BLANK to 9" }
        for ((rowIndex, row) in rows.withIndex()) {
            for ((cellIndex, value) in row.withIndex()) {
                if (value == BLANK)
                    continue
                val index = WIDTH * rowIndex + cellIndex
                mask[index] = 1
                input[index] = value
            }
        }
    }

    fun solve(): Array<Int> {
        val mask = mask.mapIndexed { index, i -> if (i == 0) -1 else index }.filter { it != -1 }
        val indices = ((0 until WIDTH * HEIGHT) - mask)

        var i = 0
        val next = {
            i += 1
        }
        val previous = {
            i -= 1
        }
        solution = input.clone()

        do {
            if (i >= indices.size)
                break

            val index = indices[i]
            do {
                solution[index]++
                if (isValid(index)) {
                    next()
                    break
                } else if (solution[index] == 10) {
                    solution[index] = 0
                    previous()
                    break
                }
            } while (true)

        } while (true)

        return solution
    }

    private fun options(index: Int): Set<Int> {
        val offset = WIDTH * (index.y - index.y % 3) + (index.x - index.x % 3)
        val xrange = (0 until 9).filter { it != index.x }.map { WIDTH * index.y + it }
        val yrange = (0 until 9).filter { it != index.y }.map { WIDTH * it + index.x }
        val srange = (0..2).flatMap { y -> (0..2).map { x -> WIDTH * y + x + offset } }.filter { it != index }

        val xses = solution.slice(xrange)
        val yses = solution.slice(yrange)
        val square = solution.slice(srange)

        return options - (xses + yses + square)
    }

    private fun isValid(index: Int) = options(index).contains(solution[index])

    private val Int.x
        get() = this % WIDTH
    private val Int.y
        get() = (this - this.x) / HEIGHT
}

private fun Array<Int>.print() {
    for (i in 0 until this.size step HEIGHT)
        println(this.slice(i until (i + WIDTH)).joinToString())
    println()
}

const val B = BLANK
fun main(args: Array<String>) {

    val s1 = Sudoku(listOf(4, B, B, B, 1, B, B, 5, 6),
            listOf(6, B, B, B, 7, 2, B, B, B),
            listOf(B, B, 9, B, B, B, 4, B, B),
            listOf(B, 4, B, B, B, B, B, B, B),
            listOf(7, 6, B, B, 3, B, B, 1, 2),
            listOf(B, B, B, B, B, B, B, 7, B),
            listOf(B, B, 4, B, B, B, 8, B, B),
            listOf(B, B, B, 5, 4, B, B, B, 3),
            listOf(9, 8, B, B, 6, B, B, B, 1))

    val s2 = Sudoku(listOf(B, 6, B, B, B, B, B, B, B),
            listOf(B, B, 4, 5, B, B, 9, B, 2),
            listOf(B, 2, 8, 6, B, B, 1, 7, B),
            listOf(B, 5, 1, B, 6, B, 4, 8, B),
            listOf(B, B, B, B, 4, 3, B, 5, B),
            listOf(B, 7, 9, B, B, B, B, B, B),
            listOf(B, B, B, B, 3, B, B, 1, B),
            listOf(7, B, B, B, 5, 2, B, B, B),
            listOf(2, B, 6, 7, B, B, B, 3, 8))

    for (s in listOf(s1, s2)) {
        println("Input")
        s.input.print()
        println("Solution")
        s.solve().print()
    }
}
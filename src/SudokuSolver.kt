import kotlin.math.floor

const val BLANK = -1
const val B = BLANK

class Sudoku {

    class Cell {

        constructor(value: Int = BLANK) {
            IsChangeable = value == BLANK
            this.value = value
        }

        var IsChangeable:Boolean = false
        var history: MutableSet<Int> = mutableSetOf()
        var value: Int = 0
            set(value) {
                if(this.IsChangeable || field == 0) {
                    history.add(value)
                    field = value
                } else if(field != 0) {
                    println("TRYING TO CHANGE VALUE")
                }
            }
    }


    var cells = Array(9) {
        Array(9) {
            Cell()
        }
    }

    val isSolved:Boolean
        get() = cells.flatten().none { it.value == BLANK }

    constructor(vararg rows: Iterable<Int>) {
        check(rows.count() == 9) { "Sudoku doesn't have 9 rows" }
        check(rows.all { it.count() == 9 }) { "Every row should be 9 cells" }
        check(rows.all { it.all { c -> (BLANK..9).contains(c) } }) { "Cell values range from BLANK to 9" }


        for((rowIndex, row) in rows.withIndex()) {
            for((cellIndex, value) in row.withIndex()) {
                cells[rowIndex][cellIndex] = Cell(value)
            }
        }

    }

    fun solve()
    {

        var x = 0
        var y = 0

        val nextCell = {
            println("Next")
            x += 1
            if(x == 9)
            {
                x = 0
                y += 1
            }
        }

        val previousCell = {
            println("Previous")
            x -= 1
            if(x < 0) {
                x = 8
                y -= 1
            }

        }

        val resetCell = {
            println("Resetting cell")
            if(cells[y][x].IsChangeable)
                cells[y][x].value = -1
        }

        var tries = 0

        println("Start")
        cells.print()
        println()

        while (y != 9) {
            var cell = cells[y][x]

            if(cell.value == -1)
                cell.value = 1

            if(cell.value == 10) {

                do {
                    resetCell()
                    previousCell()
                    cell = cells[y][x]
                } while(cell.value == 9 || !cell.IsChangeable)
                cell.value += 1
                println("Incrementing")
            }

            if(isValid(x,y)) {
                do {
                    nextCell()
                    if(y == 9)
                        break
                    cell = cells[y][x]
                } while(!cell.IsChangeable)
            } else {
                println("Incrementing same cell")
                cell.value += 1
            }

            cells.print()
            println()

            tries++
        }

        println("Completed! It took $tries actions")
    }

    fun isValid(x: Int, y: Int) : Boolean {
        return options(x,y).contains(cells[y][x].value)
    }

    fun options(x: Int, y: Int): Set<Int> {
        var options = (1..9).toMutableSet()

        //check the x
        for(i in 0 until 9) {
            val v = cells[y][i].value
            if(v != BLANK && x != i)
            {
                options.remove(v)
            }
        }

        //check the y
        for(i in 0 until 9) {
            val v = cells[i][x].value

            if(v != BLANK && y != i)
            {
                options.remove(v)
            }
        }


        val offsetX = (floor(x / 3f) * 3).toInt()
        val offsetY = (floor(y / 3f) * 3).toInt()

        val xrange = offsetX..offsetX+2
        val yrange = offsetY..offsetY+2

        for(i in xrange) {
            for(j in yrange) {


                val v = cells[j][i].value
                if(v != BLANK && y != j && i != x)
                {
                    options.remove(v)
                }
            }
        }

        return options.toSet()
    }
}

private fun Array<Array<Sudoku.Cell>>.print() {
    this.forEach {
        kotlin.io.println(it.joinToString {
            if(it.value == BLANK) " " else it.value.toString()
        })
    }
}


fun main(args: Array<String>) {
    val row: List<Int> = listOf(1, 2, 3)


//    val sudoku = Sudoku(
//            listOf(BLANK,  6, BLANK, BLANK, BLANK, BLANK, BLANK, BLANK, BLANK),
//            listOf(BLANK, BLANK,  4,  5, BLANK, BLANK,  9, BLANK,  2),
//            listOf(BLANK,  2,  8,  6, BLANK, BLANK,  1,  7, BLANK),
//
//            listOf(BLANK, 5, 1, BLANK, 6, BLANK, 4, 8, BLANK),
//            listOf(BLANK, BLANK, BLANK, BLANK, 4, 3, BLANK, 5, BLANK),
//            listOf(BLANK, 7, 9, BLANK, BLANK, BLANK, BLANK, BLANK, BLANK),
//
//            listOf(BLANK, BLANK, BLANK, BLANK, 3, BLANK, BLANK, 1, BLANK),
//            listOf(7, BLANK, BLANK, BLANK, 5, 2, BLANK, BLANK, BLANK),
//            listOf(2, BLANK, 6, 7, BLANK, BLANK, BLANK, 3, 8)
//    )

    //http://www.extremesudoku.info/sudoku.html
//    val s = Sudoku(
//            listOf(4,B,B,B,1,B,B,5,6),
//            listOf(6,B,B,B,7,2,B,B,B),
//            listOf(B,B, 9,B,B,B,4,B,B),
//            listOf(B,4,B,B,B,B,B,B,B),
//            listOf(7,6,B,B,3,B,B,1,2),
//            listOf(B,B,B,B,B,B,B,7,B),
//            listOf(B,B,4,B,B,B,8,B,B),
//            listOf(B,B,B,5,4,B,B,B,3),
//            listOf(9,8,B,B,6,B,B,B,1)
//    )
//
//    s.solve()
//

    val s = Sudoku(
            listOf(3,4,B,B,B,B,B,B,5),
            listOf(8,B,B,B,B,2,B,B,B),
            listOf(B,B,B,B,5,7,3,B,B),
            listOf(B,8,B,B,6,B,5,B,B),
            listOf(B,9,B,B,B,B,B,3,B),
            listOf(B,B,4,B,2,B,B,9,B),
            listOf(B,B,6,2,8,B,B,B,B),
            listOf(B,B,B,5,B,B,B,B,9),
            listOf(1,B,B,B,B,B,B,2,4)
    )

    s.solve()
}
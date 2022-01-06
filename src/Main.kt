import kotlin.random.Random

fun main() {
    val fieldWidth = 12
    var minesQuantity : Int
    val minerList = Array(fieldWidth) { CharArray(fieldWidth) }
    var firstMove = true
    var firstFree = "mines"

//первоначальная инициализация пустого игрового поля
    for (i in 2 until fieldWidth - 1) {
        for (j in 2 until fieldWidth - 1)
            minerList[i][j] = 'F'
    }

// выбор количества мин
    do {print("How many mines do you want on the field? ")
        minesQuantity = readLine()!!.toInt()
    } while (minesQuantity <= 0 || minesQuantity > 80)

    printClosedField(minerList, fieldWidth)

// игрок делает первый ход
    while (firstFree != "free") {
        firstFree = newMove(minerList, fieldWidth, firstMove, minesQuantity)
    }
    firstMove  = false


// вычисление значений открытых игроком ячеек поля
    openCell(minerList, fieldWidth)

    getGameStatus(minerList, fieldWidth, minesQuantity)
    while (getGameStatus(minerList, fieldWidth, minesQuantity)) {
        newMove(minerList, fieldWidth, firstMove, minesQuantity)
        for (i in 2 until fieldWidth - 1) {
            for (j in 2 until fieldWidth - 1)
                if (minerList[i][j] == 'O')  cellCounter(minerList, i, j)
        }
    }

}

fun openCell(minerList: Array<CharArray>, fieldWidth: Int) {
    for (i in 2 until fieldWidth - 1) {
        for (j in 2 until fieldWidth - 1)
            when(minerList[i][j]) {
                'O' -> cellCounter(minerList, i, j)
            }
    }

    repeat(15) {
        for (i in 2 until fieldWidth - 2) {
            for (j in 2 until fieldWidth - 2)
                when (minerList[i][j]) {
                    '0' -> zeroCounter(minerList, i, j)
                }
        }
        for (i in fieldWidth - 2 downTo 1) {
            for (j in fieldWidth - 2 downTo 1)
                when (minerList[i][j]) {
                    '0' -> zeroCounter(minerList, i, j)
                }
        }
    }
}

fun zeroCounter(minerList: Array<CharArray>, i: Int, j: Int) {
    if (minerList[i - 1][j - 1] != 'X')  cellCounter(minerList,i - 1, j - 1)
    if (minerList[i + 1][j + 1] != 'X') cellCounter(minerList, i + 1, j + 1)
    if (minerList[i + 1][j] != 'X') cellCounter(minerList, i + 1, j)
    if (minerList[i + 1][j - 1] != 'X') cellCounter(minerList, i + 1, j - 1)
    if (minerList[i - 1][j + 1] != 'X') cellCounter(minerList, i - 1, j + 1)
    if (minerList[i - 1][j] != 'X') cellCounter(minerList, i - 1, j)
    if (minerList[i][j + 1] != 'X') cellCounter(minerList, i, j + 1)
    if (minerList[i][j - 1] != 'X') cellCounter(minerList, i, j - 1)
}

fun cellCounter(minerList: Array<CharArray>, i: Int, j: Int) {
    var cellCounter = 0

    if (i + 1 <= 10 && j + 1 <= 10  && minerList[i + 1][j + 1] == 'X' ) cellCounter++
    if (i - 1 >= 2 && j - 1 >= 2  && minerList[i - 1][j - 1] == 'X' ) cellCounter++
    if (i - 1 >= 2 && j + 1 <= 10  && minerList[i - 1][j + 1] == 'X' ) cellCounter++
    if (i + 1 <= 10 && j - 1 >= 2  && minerList[i + 1][j - 1] == 'X' ) cellCounter++
    if (j - 1 >= 2  && minerList[i][j - 1] == 'X' ) cellCounter++
    if (j + 1 <= 10  && minerList[i][j + 1] == 'X' ) cellCounter++
    if (i - 1 >= 2  && minerList[i - 1][j] == 'X' ) cellCounter++
    if (i + 1 <= 10  && minerList[i + 1][j] == 'X' ) cellCounter++

    minerList[i][j] = cellCounter.digitToChar()
}

fun borderSetter (minerList: Array<CharArray>, fieldWidth: Int) {
    for (j in 1 until fieldWidth - 1) {
        minerList[0][j] = (j-1).digitToChar()
    }
    for (i in 1 until fieldWidth - 1) {
        minerList[i][0] = (i-1).digitToChar()
    }

    for (i in 0 until fieldWidth) {
        for (j in 0 until fieldWidth)
            if (i == fieldWidth - 1 || i == 1)  minerList[i][j] = '—'
    }
    for (i in 0 until fieldWidth) {
        for (j in 0 until fieldWidth)
            if (j == fieldWidth - 1 || j == 1)  minerList[i][j] = '│'
    }
    minerList[0][0] = ' '
}

fun printClosedField(minerList: Array<CharArray>, fieldWidth: Int) {
    borderSetter(minerList, fieldWidth)
    for (j in 0 until fieldWidth) {
        for (i in 0 until fieldWidth) {
            when(minerList[j][i]) {
                'X' -> print ('.')
                'N' -> print ('*')
                'Z' -> print ('*')
                'O' -> print(cellCounter(minerList, i, j))
                'F' -> print('.')
                '0' -> print('/')
                'R' -> print ('X')
                else -> print (minerList[j][i])
            }
        }
        println()
    }
}

fun newMove(minerList: Array<CharArray>, fieldWidth: Int, firstMove: Boolean,minesQuantity: Int): String {
    print("Set/delete mine marks (x and y coordinates): ")
    val (y, x , act) = readLine()!!.split(" ")
    var x1 = minerList[x.toInt() + 1][y.toInt() + 1]
    if (firstMove) {
        minerList[x.toInt() + 1][y.toInt() + 1] = 'O'
        if (act == "free") minesSetting(minerList, fieldWidth, minesQuantity)
    }

    when(act) {
        "mine" -> {
            when (minerList[x.toInt() + 1][y.toInt() + 1]) {
                'N' -> {
                    minerList[x.toInt() + 1][y.toInt() + 1] = 'F'
                    printClosedField(minerList, fieldWidth)
                    return act
                }
                'F' -> {
                    minerList[x.toInt() + 1][y.toInt() + 1] = 'N'
                    printClosedField(minerList, fieldWidth)
                    return act
                }
                'X' -> {
                    minerList[x.toInt() + 1][y.toInt() + 1] = 'Z'
                    printClosedField(minerList, fieldWidth)
                    return act
                }
                'Z' -> {
                    minerList[x.toInt() + 1][y.toInt() + 1] = 'X'
                    printClosedField(minerList, fieldWidth)
                    return act
                }
                'O' -> {
                    minerList[x.toInt() + 1][y.toInt() + 1] = 'N'
                    if (x1 == 'N') minerList[x.toInt() + 1][y.toInt() + 1] = 'F'
                    printClosedField(minerList, fieldWidth)
                    return act
                }

                else -> {
                    println("There is a number here!")
                    return act
                }
            }
        }
        "free" -> {
            when(minerList[x.toInt() + 1][y.toInt() + 1]) {
                'X' -> {
                    minerList[x.toInt() + 1][y.toInt() + 1] = 'R'
                    printLostedField(minerList, fieldWidth)
                    return act
                }
                'Z' -> {
                    minerList[x.toInt() + 1][y.toInt() + 1] = 'R'
                    printLostedField(minerList, fieldWidth)
                    return act
                }
                'F' -> {
                    minerList[x.toInt() + 1][y.toInt() + 1] = 'O'
                    openCell(minerList,fieldWidth)
                    printClosedField(minerList, fieldWidth)
                    return act
                }
                'N' -> {
                    minerList[x.toInt() + 1][y.toInt() + 1] = 'F'
                    openCell(minerList,fieldWidth)
                    printClosedField(minerList, fieldWidth)
                    return act
                }
                'O' -> {
                    if (firstMove)  cellCounter(minerList,x.toInt() + 1,y.toInt() + 1)
                    openCell(minerList,fieldWidth)
                    printClosedField(minerList, fieldWidth)
                    return act
                }

                else -> {
                    //minerList[x.toInt() + 1][y.toInt() + 1]
                    println("There is a number here!")
                    return act
                }
            }

        }
    }
    return act
}

fun printLostedField(minerList: Array<CharArray>, fieldWidth: Int) {
    borderSetter(minerList, fieldWidth)
    for (j in 0 until fieldWidth) {
        for (i in 0 until fieldWidth) {
            when(minerList[j][i]) {
                'X' -> print ('X')
                'R' -> print ('X')
                'N' -> print('.')
                'Z' -> print ('X')
                'O' -> print(cellCounter(minerList, j, i))
                'F' -> print('.')
                '0' -> print('/')
                else -> print (minerList[j][i])

            }
        }
        println()
    }
}

fun getGameStatus(minerList: Array<CharArray> , fieldWidth: Int, minesQuantity: Int): Boolean {
    var minesCounter = 0
    var result = true
    for (j in 2 until fieldWidth - 1) {
        for (i in 2 until fieldWidth - 1) {
            when (minerList[j][i]) {
                'Z' -> {
                    minesCounter++
                    if (minesCounter == minesQuantity) {
                        println("Congratulations! You found all the mines!")
                        result = false
                        break
                    }
                }
                'R' -> {
                    println("You stepped on a mine and failed!")
                    result = false
                    break
                }
                'F' -> result = true
                'N' -> result = true
                'O' -> result = true

            }
            if (!result) break
        }
    }
    return result
}

fun minesSetting(minerList: Array<CharArray> , fieldWidth: Int, minesQuantity: Int) {
/* расстановка мин по полю */
    var mQty = minesQuantity
    for (k in 0 until mQty) {
        var i = Random.nextInt(2, fieldWidth - 1)
        var j = Random.nextInt(2, fieldWidth - 1)
        when (minerList[i][j]) {
            'O' -> mQty++
            'X' -> mQty++
            'N' -> mQty++
            else -> minerList[i][j] = 'X'
        }
    }
}

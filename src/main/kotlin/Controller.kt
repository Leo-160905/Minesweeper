package org.example

import java.awt.Color
import java.awt.Dimension
import java.util.Random

lateinit var frame: LFrame
var gameDifficulty = 3
val btnField = Dimension(35, 25)
var bombsLeft = 0

fun initGame() {
    frame = LFrame()
    frame.cp = frame.contentPane
    frame.initFrame()
}

fun startGame() {
    frame.loadScenes(frame.getGameScene())
    distributeBombsAndNumbers(frame.btnList)
}

fun setUpGame() {
    println("TODO") // Todo
}

fun getIndexes8(i: Int, x: Int, y: Int): Int {
    var xn = 0
    var yn = 0
    when (i) {
        0 -> {
            xn = x - 1
            yn = y - 1
        }

        1 -> {
            xn = x
            yn = y - 1
        }

        2 -> {
            xn = x + 1
            yn = y - 1
        }

        3 -> {
            xn = x - 1
            yn = y
        }

        4 -> {
            xn = x + 1
            yn = y
        }

        5 -> {
            xn = x - 1
            yn = y + 1
        }

        6 -> {
            xn = x
            yn = y + 1
        }

        7 -> {
            xn = x + 1
            yn = y + 1
        }
    }
    return yn * btnField.width + xn
}

fun getIndexes4(i: Int, x: Int, y: Int): Int {
    var retVal = 0
    when (i) {
        0 -> retVal = (y - 1) * btnField.width + x
        1 -> retVal = y * btnField.width + (x - 1)
        2 -> retVal = y * btnField.width + (x + 1)
        3 -> retVal = (y + 1) * btnField.width + x
    }
    return retVal
}

fun distributeBombsAndNumbers(list: ArrayList<SweeperTile>) {
    val rand = Random()
    list.forEach {
        if (rand.nextInt(5 * gameDifficulty) == 0) {
            it.setBomb()
            bombsLeft++
        }
    }

    list.forEachIndexed { i, it ->
        val x = i % btnField.width
        val y = i / btnField.width
        if (!it.isBomb()) {
            var count = 0
            for (j in 0..<8) {
                val n = getIndexes8(j, x, y)

                val xn = n % btnField.width
                val yn = n / btnField.width
                if (xn >= 0 && xn + 1 < btnField.width && yn >= 0 && yn + 1 < btnField.height) {
                    if (frame.btnList[n].isBomb()) count++
                }
            }
            frame.btnList[y * btnField.width + x].setTileValue(count.toString())
        }
    }
}

fun revealNumbers(list: ArrayList<SweeperTile>, n: Int, forceFlag: Boolean) {

    if(!forceFlag) {
        if (list[n].isChecked()) return
        else list[n].setChecked()
    }

    val x = n % btnField.width
    val y = n / btnField.width
    if (list[n].getTileValue() == "0" || forceFlag) {
        //uncover btn
        for (i in 0..<8) {
            val iNew = getIndexes8(i, x, y)
            if (iNew >= 0 && iNew < btnField.width * btnField.height && x - 1 >= 0 && x + 1 < btnField.width && !list[iNew].isBomb()) {
                list[iNew].background = Color(240, 240, 240)
                list[iNew].isOpaque = false
                list[iNew].foreground = Color.RED
                list[iNew].setNumberAsText()
                list[iNew].setRevealed()
            }
        }

        // move on to next btn to uncover new ones
        for (i in 0..<4) {
            val iNew = getIndexes4(i, x, y)
            if (iNew >= 0 && iNew < btnField.width * btnField.height && x - 1 >= 0 && x + 1 < btnField.width) revealNumbers(
                list,
                iNew,
                false
            )
        }
        return
    }
    return
}

// starts revealing when enough flags are set
fun revealCluster(list: ArrayList<SweeperTile>, n: Int) {
    val x = n % btnField.width
    val y = n / btnField.width
    var bombsCount = 0
    for (i in 0..8) {
        val iNew = getIndexes8(i, x, y)
        val xn = iNew % btnField.width
        val yn = iNew / btnField.width

        if (xn >= 0 && xn + 1 < btnField.width && yn >= 0 && yn + 1 < btnField.height) {
            if (list[iNew].text == "F") bombsCount++
        }
    }
    println("$bombsCount : ${list[n].getTileValue()}")
    if (bombsCount == list[n].getTileValue().toInt()) revealNumbers(list, n, true)
}
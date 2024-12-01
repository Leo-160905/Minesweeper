package org.example

import java.awt.Color
import java.awt.Dimension
import java.util.Random

lateinit var frame: LFrame
var gameDifficulty = 2
val btnField = Dimension(10, 10)

fun initGame() {
    frame = LFrame()
    frame.cp = frame.contentPane
    frame.initFrame()
}

fun startGame() {
    frame.loadScenes(frame.getGameScene())
    distributeBombsandNumbers(frame.btnList)
}

fun setUpGame() {
    println("TODO") // Todo
}

fun getIndexes(i: Int, x: Int, y: Int): Int {
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

fun distributeBombsandNumbers(list: ArrayList<SweeperTile>) {
    val rand = Random()
    list.forEach {
        if (rand.nextInt(5 * gameDifficulty) == 0) {
            it.setBomb()
        }
    }

    list.forEachIndexed { i, it ->
        val x = i % btnField.width
        val y = i / btnField.width
        if (!it.isBomb()) {
            var count = 0
            for (j in 0..<8) {
                val n = getIndexes(j, x, y)
                if (n >= 0 && n < btnField.width * btnField.height) {
                    if (frame.btnList[n].isBomb()) count++
                }
            }

            frame.btnList[y * btnField.width + x].setTileValue(count.toString())
        }
    }
}

fun revealNumbers(list: ArrayList<SweeperTile>, n: Int) {

    if(list[n].isRevealed()) return
    val x = n % btnField.width
    val y = n / btnField.width

    list[n].background = Color(240, 240, 240)
    list[n].isOpaque = false
    list[n].foreground = Color.RED
    list[n].setNumberAsText()
    list[n].setRevealed()

    if(list[n].getTileValue() == "0") {
        for (i in 0..<4) {
            var iNew = 0
            when(i) {
                0 -> iNew = (y - 1) * btnField.width + x
                1 -> iNew = y * btnField.width + (x - 1)
                2 -> iNew = y * btnField.width + (x + 1)
                3 -> iNew = (y + 1) * btnField.width + x
            }
            println(iNew)
            if(iNew >= 0 && iNew < btnField.width * btnField.height) revealNumbers(list, iNew)
            else return
        }
        return
    }
    return

}
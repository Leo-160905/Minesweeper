package org.example

import java.awt.Color
import java.awt.Dimension
import java.awt.Point
import java.util.Random
import javax.swing.JOptionPane
import kotlin.system.exitProcess

lateinit var frame: LFrame
var btnField = Dimension(34, 24)
var bombsCount = 100
var tilesCovered = btnField.height * btnField.width

fun initGame() {
    frame = LFrame()
    frame.cp = frame.contentPane
    frame.initFrame()
}

fun startGame() {
    frame.loadScenes(frame.getGameScene())
    frame.setLocationRelativeTo(null)
    distributeBombsAndNumbers(frame.btnList)
    checkGameWon()
}

fun setUpGame() {
    frame.loadScenes(frame.getSetupScene())
}

fun getIndexes8(i: Int, x: Int, y: Int): Point {
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
    return if (xn >= 0 && xn < btnField.width && yn >= 0 && yn < btnField.height) Point(xn, yn) else Point(-1, -1)
}

/*fun getIndexes4(i: Int, x: Int, y: Int): Point {
    var xn = x
    var yn = y
    when (i) {
        0 -> yn -= 1
        1 -> xn -= 1
        2 -> xn += 1
        3 -> yn += 1
    }
    return if (xn >= 0 && xn < btnField.width && yn >= 0 && yn < btnField.height) Point(xn, yn) else Point(-1, -1)
}*/

fun distributeBombsAndNumbers(list: ArrayList<ArrayList<SweeperTile>>) {
    val rand = Random()
    for (i in 0..<bombsCount) {
        var bombIsSet = false
        while (!bombIsSet){
            val x = rand.nextInt(btnField.width)
            val y = rand.nextInt(btnField.height)
            if(!list[y][x].isBomb()) {
                list[y][x].setBomb()
                bombIsSet = true
            }
        }
    }

    println("finished distributing")
    for (y in 0..<list.size) {
        list[y].forEachIndexed { x, it ->
            if (!it.isBomb()) {
                var count = 0
                for (j in 0..<8) {
                    val n = getIndexes8(j, x, y)
                    val xn = n.x
                    val yn = n.y

                    if (xn != -1) {
                        if (frame.btnList[yn][xn].isBomb()) count++
                    }
                }
                frame.btnList[y][x].setTileValue(count.toString())
            }
        }
    }
}

fun revealNumbers(list: ArrayList<ArrayList<SweeperTile>>, x: Int, y: Int) {

    if (list[y][x].isChecked()) return
    else {
        list[y][x].setChecked()
    }

    if (list[y][x].getTileValue() == "0") {
        //uncover btn
        for (i in 0..<8) {
            val iNew = getIndexes8(i, x, y)
            val xn = iNew.x
            val yn = iNew.y
            if (xn != -1 && !list[yn][xn].isBomb() && !list[yn][xn].isRevealed()) {
                revealTile(list[yn][xn])
                tilesCovered--
            }
        }

        // move on to next btn to uncover new ones
        for (i in 0..<8) {
            val iNew = getIndexes8(i, x, y)
            val xn = iNew.x
            val yn = iNew.y

            if (xn != -1)
                revealNumbers(list, xn, yn)
        }
        return
    }
    return
}

fun revealTile(tile: SweeperTile) {
    tile.background = Color(240, 240, 240)
    tile.isOpaque = false
    tile.foreground = Color.RED
    tile.setNumberAsText()
    tile.setRevealed()
}

// starts revealing when enough flags are set
fun revealCluster(list: ArrayList<ArrayList<SweeperTile>>, x: Int, y: Int) {

    var flagsCount = 0
    // Counting the flags in the surrounding buttons
    for (i in 0..<8) {
        val iNew = getIndexes8(i, x, y)
        val xn = iNew.x
        val yn = iNew.y

        if (xn >= 0 && xn < btnField.width && yn >= 0 && yn < btnField.height) {
            if (list[yn][xn].text == "F") flagsCount++
        }
    }

    if (flagsCount == list[y][x].getTileValue().toInt()) {

        for (i in 0..<8) {
            val iNew = getIndexes8(i, x, y)
            val xn = iNew.x
            val yn = iNew.y

            if (xn != -1) {
                if (list[yn][xn].text != "F" && !list[yn][xn].isRevealed()) {
                    if (list[yn][xn].isBomb()) endGame()
                    else {
                        revealTile(list[yn][xn])
                        tilesCovered--
                    }
                    if (list[yn][xn].getTileValue() == "0") revealNumbers(list, xn, yn)
                }
            }
        }
    }
}

fun checkGameWon() {
    frame.displayBombsCount(bombsCount, tilesCovered)
    if (bombsCount <= 0 && tilesCovered == 0) {
        JOptionPane.showMessageDialog(frame, "Game Won!!!", "Game Over", JOptionPane.INFORMATION_MESSAGE)
        exitProcess(0)
    }
}

fun endGame() {
    JOptionPane.showMessageDialog(frame, "Game Lost!!!", "Game Over", JOptionPane.INFORMATION_MESSAGE)
    exitProcess(0)
}
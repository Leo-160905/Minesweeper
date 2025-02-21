package org.example

import java.awt.Color
import javax.swing.JButton

class SweeperTile : JButton() {
    private var tileValue = ""
    private var revealed = false
    private var checked = false

    fun setBomb() {
        this.tileValue = "B"
    }
    fun isBomb(): Boolean {
        return tileValue == "B"
    }

    fun setNumberAsText() {
        when(tileValue) {
            "1" -> this.foreground = Color.BLUE
            "2" -> this.foreground = Color(0,130,0)
            "3" -> this.foreground = Color.RED
            "4" -> this.foreground = Color(0,0,90)
            "5" -> this.foreground = Color(80,0,0)
            "6" -> this.foreground = Color(0,140,140)
            "7" -> this.foreground = Color.BLACK
            "8" -> this.foreground = Color.GRAY
            else -> this.foreground = Color.BLACK
        }
        this.text = if(tileValue != "0") tileValue else ""
    }

    fun setTileValue(tileValue: String) {
        this.tileValue = tileValue
    }
    fun getTileValue(): String {
        return tileValue
    }
    fun setRevealed() {
        this.revealed = true
    }
    fun isRevealed(): Boolean {
        return revealed
    }fun setChecked() {
        this.checked = true
    }
    fun isChecked(): Boolean {
        return checked
    }
}
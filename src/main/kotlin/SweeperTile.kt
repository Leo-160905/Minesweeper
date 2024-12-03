package org.example

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
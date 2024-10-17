package com.zybooks.decodethemobilemystery

import android.widget.TextView

class CrosswordGame {

    private val crosswordGrid = Array(GRID_SIZE) { Array(GRID_SIZE) { ' ' } }
    fun newGame() {
        // create a random crossword grid with letters each time
    }
    fun getLetter(row: Int, col: Int): Char {
        return crosswordGrid[row][col]
    }

    fun setLetter(row: Int, col: Int, letter: Char) {

        crosswordGrid[row][col] = letter
    }

    fun isCorrectLetter(row: Int, col: Int, letter: Char): Boolean {
        return crosswordGrid[row][col] == letter
    }

    val isCrosswordComplete: Boolean
        get() {
            for (row in 0 until GRID_SIZE) {
                for (col in 0 until GRID_SIZE) {
                    if (crosswordGrid[row][col] == ' ') {
                        return false
                    }
                }
            }
            return true
        }

    companion object {
        const val GRID_SIZE = 5
    }

    fun getID(): String {
        // TODO: Make something happen here idk
        return "";
    }

}

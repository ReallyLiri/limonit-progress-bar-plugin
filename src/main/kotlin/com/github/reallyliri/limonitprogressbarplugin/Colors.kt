package com.github.reallyliri.limonitprogressbarplugin

import java.awt.Color

interface Colors {
    companion object {
        private val RED: Color = Color(255, 0, 0)
        private val ORANGE: Color = Color(255, 154, 0)
        private val YELLOW: Color = Color(255, 255, 0)
        private val GREEN: Color = Color(25, 255, 0)
        private val BLUE: Color = Color(0, 150, 255)
        private val VIOLET: Color = Color(100, 30, 255)
        val RAINBOW_ARRAY: Array<Color> = arrayOf<Color>(RED, ORANGE, YELLOW, GREEN, BLUE, VIOLET)
    }
}
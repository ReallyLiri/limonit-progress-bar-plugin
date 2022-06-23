package com.github.reallyliri.limonitprogressbarplugin

import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.LafManagerListener
import javax.swing.UIManager

class ProgressBarSetup : LafManagerListener {
    init {
        updateProgressBarUi()
    }

    private fun updateProgressBarUi() {
        UIManager.put("ProgressBarUI", ProgressBarUi::class.java.name)
        UIManager.getDefaults()[ProgressBarUi::class.java.name] = ProgressBarUi::class.java
    }

    override fun lookAndFeelChanged(source: LafManager) {
        updateProgressBarUi()
    }
}

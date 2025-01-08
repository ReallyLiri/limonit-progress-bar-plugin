package com.github.reallyliri.limonitprogressbarplugin.ProgressBar;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ide.AppLifecycleListener;
import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.LafManagerListener;
import com.intellij.openapi.application.ApplicationManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ProgressBarUiRegister implements AppLifecycleListener {
    private static final Logger log = Logger.getInstance(ProgressBarUiRegister.class);

    public ProgressBarUiRegister() {
        try {
            ApplicationManager.getApplication().getMessageBus().connect().subscribe(LafManagerListener.TOPIC, new LafManagerListener() {
                @Override
                public void lookAndFeelChanged(@NotNull LafManager source) {
                    updateProgressBarUI();
                }
            });
            updateProgressBarUI();
        } catch (IllegalStateException e) {
            log.warn("Failed to register ProgressBarUi", e);
        }
    }

    private static void updateProgressBarUI() {
        UIManager.put("ProgressBarUI", ProgressBarUi.class.getName());
        UIManager.getDefaults().put(ProgressBarUi.class.getName(), ProgressBarUi.class);
    }
}

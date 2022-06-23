package com.github.reallyliri.limonitprogressbarplugin.Tray;

import com.github.reallyliri.limonitprogressbarplugin.Res.Icons;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationInfo;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class TrayIconUpdater implements Disposable {
    private static final boolean systemTraySupported = SystemTray.isSupported();
    private static final String applicationName = ApplicationInfo.getInstance().getFullApplicationName();
    private static final long ANIMATION_DELAY_MS = 1000L;
    private static final java.util.List<Image> IMAGES = java.util.List.of(
            Icons.MARIO.getImage(),
            Icons.SHELL.getImage()
    );

    private final AtomicBoolean showing = new AtomicBoolean(false);
    private final TrayIcon trayIcon = new TrayIcon(IMAGES.get(0), String.format("%s is working hard! (or hardly working?)", applicationName));

    public void show() {
        if (!systemTraySupported || !showing.compareAndSet(false, true)) {
            return;
        }
        SwingUtilities.invokeLater(() -> {
            try {
                SystemTray.getSystemTray().add(trayIcon);
                new Thread(() -> {
                    int index = 0;
                    while (showing.get()) {
                        trayIcon.setImage(IMAGES.get(index));
                        index = (index + 1) % IMAGES.size();
                        try {
                            Thread.sleep(ANIMATION_DELAY_MS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } catch (AWTException e) {
                e.printStackTrace();
            }
        });
    }

    public void hide() {
        if (!systemTraySupported || !showing.compareAndSet(true, false)) {
            return;
        }
        SwingUtilities.invokeLater(() -> SystemTray.getSystemTray().remove(trayIcon));
    }

    @Override
    public void dispose() {
        hide();
    }
}

package com.github.reallyliri.limonitprogressbarplugin.Tray;

import com.github.reallyliri.limonitprogressbarplugin.Res.Icons;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.LazyInitializer;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class TrayIconUpdater implements Disposable {
    private static final Logger log = Logger.getInstance(TrayIconUpdater.class);
    private static final boolean systemTraySupported = SystemTray.isSupported();
    private static final String applicationName = ApplicationInfo.getInstance().getFullApplicationName();
    private static final long ANIMATION_DELAY_MS = 1000L;
    private static final java.util.List<Image> IMAGES = java.util.List.of(
            Icons.TRAY_1.getImage(),
            Icons.TRAY_2.getImage(),
            Icons.TRAY_3.getImage()
    );

    private final AtomicBoolean showing = new AtomicBoolean(false);
    private final LazyInitializer.LazyValue<TrayIcon> trayIconLazy = LazyInitializer.create(() -> new TrayIcon(IMAGES.get(0), String.format("%s is working hard! (or hardly working?)", applicationName)));

    public void show() {
        if (!systemTraySupported || !showing.compareAndSet(false, true)) {
            return;
        }
        SwingUtilities.invokeLater(() -> {
            try {
                TrayIcon trayIcon = trayIconLazy.get();
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
            } catch (Throwable e) {
                log.error("failed to create tray", e);
            }
        });
    }

    public void hide() {
        if (!systemTraySupported || !showing.compareAndSet(true, false)) {
            return;
        }
        SwingUtilities.invokeLater(() -> SystemTray.getSystemTray().remove(trayIconLazy.get()));
    }

    @Override
    public void dispose() {
        hide();
    }
}

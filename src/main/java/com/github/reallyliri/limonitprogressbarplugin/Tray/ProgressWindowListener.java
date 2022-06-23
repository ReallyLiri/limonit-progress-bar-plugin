package com.github.reallyliri.limonitprogressbarplugin.Tray;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.util.AbstractProgressIndicatorBase;
import com.intellij.openapi.progress.util.ProgressWindow;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProgressWindowListener implements ProgressWindow.Listener, Disposable {
    private static final Logger log = Logger.getInstance(ProgressWindowListener.class);

    private static final long LOOP_DELAY_MS = 1500L;

    private final Object lock = new Object();
    private boolean inProgress = false;
    private boolean disposed = false;
    private final @NotNull TrayIconUpdater trayIconUpdater = new TrayIconUpdater();
    private @NotNull List<ProgressWindow> progressWindows = new ArrayList<>();

    public ProgressWindowListener() {
        new Thread(() -> {
            while (!disposed) {
                boolean wasInProgress;
                synchronized (lock) {
                    wasInProgress = inProgress;
                    progressWindows = progressWindows.stream()
                            .filter(AbstractProgressIndicatorBase::isRunning)
                            .collect(Collectors.toList());
                    inProgress = CollectionUtils.isNotEmpty(progressWindows);
                }
                if (inProgress != wasInProgress) {
                    onProgressChange();
                }
                try {
                    Thread.sleep(LOOP_DELAY_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void progressWindowCreated(@NotNull ProgressWindow progressWindow) {
        log.info(String.format("new progress window: %s", progressWindow));
        synchronized (lock) {
            progressWindows.add(progressWindow);
        }
    }

    private void onProgressChange() {
        if (disposed) {
            return;
        }
        if (inProgress) {
            log.info("showing tray");
            trayIconUpdater.show();
        } else {
            log.info("hiding tray");
            trayIconUpdater.hide();
        }
    }

    @Override
    public void dispose() {
        log.info("disposing");
        disposed = true;
        trayIconUpdater.dispose();
    }
}

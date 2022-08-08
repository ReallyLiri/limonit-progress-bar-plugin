package com.github.reallyliri.limonitprogressbarplugin.Res;

import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class Colors {
    private static final Color ORANGE_LIGHT = toJBColor(new Color(218, 154, 88));
    private static final Color ORANGE_DARK = toJBColor(new Color(179, 99, 29));
    private static final Color BROWN = toJBColor(new Color(70, 35, 13));

    @NotNull
    private static JBColor toJBColor(@NotNull Color color) {
        return new JBColor(color, color);
    }

    public static final List<Color> COLORS = List.of(
            ORANGE_LIGHT,
            ORANGE_DARK,
            BROWN,
            ORANGE_DARK,
            ORANGE_LIGHT
    );
}

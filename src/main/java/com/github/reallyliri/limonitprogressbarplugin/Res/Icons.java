package com.github.reallyliri.limonitprogressbarplugin.Res;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public interface Icons {
    ImageIcon MARIO = new ImageIcon(Objects.requireNonNull(Icons.class.getResource("/mario.gif")));
    ImageIcon SHELL = new ImageIcon(Objects.requireNonNull(Icons.class.getResource("/shell.gif")));
}

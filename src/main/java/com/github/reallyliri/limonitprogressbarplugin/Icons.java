package com.github.reallyliri.limonitprogressbarplugin;

import javax.swing.*;
import java.util.Objects;

public interface Icons {
    ImageIcon MARIO = new ImageIcon(Objects.requireNonNull(Icons.class.getResource("/mario.gif")));
    ImageIcon SHELL = new ImageIcon(Objects.requireNonNull(Icons.class.getResource("/shell.gif")));
}

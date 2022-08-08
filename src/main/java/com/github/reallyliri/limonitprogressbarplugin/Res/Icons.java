package com.github.reallyliri.limonitprogressbarplugin.Res;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public interface Icons {
    ImageIcon TRAY_1 = new ImageIcon(Objects.requireNonNull(Icons.class.getResource("/tray1.png")));
    ImageIcon TRAY_2 = new ImageIcon(Objects.requireNonNull(Icons.class.getResource("/tray2.png")));
    ImageIcon TRAY_3 = new ImageIcon(Objects.requireNonNull(Icons.class.getResource("/tray3.png")));

    ImageIcon DETERMINATE_BAR_1 = new ImageIcon(Objects.requireNonNull(Icons.class.getResource("/bar1.png")));
    ImageIcon DETERMINATE_BAR_2 = new ImageIcon(Objects.requireNonNull(Icons.class.getResource("/bar2.png")));
    ImageIcon DETERMINATE_BAR_3 = new ImageIcon(Objects.requireNonNull(Icons.class.getResource("/bar3.png")));
    ImageIcon DETERMINATE_BAR_4 = new ImageIcon(Objects.requireNonNull(Icons.class.getResource("/bar4.png")));

    ImageIcon HAND = new ImageIcon(Objects.requireNonNull(Icons.class.getResource("/hand.gif")));
    ImageIcon CAN = new ImageIcon(Objects.requireNonNull(Icons.class.getResource("/can.gif")));
}

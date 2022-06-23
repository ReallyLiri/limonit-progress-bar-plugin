package com.github.reallyliri.limonitprogressbarplugin.ProgressBar;

import com.github.reallyliri.limonitprogressbarplugin.Res.Colors;
import com.github.reallyliri.limonitprogressbarplugin.Res.Icons;
import com.intellij.openapi.ui.GraphicsConfig;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.ui.scale.JBUIScale;
import com.intellij.util.ui.GraphicsUtil;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.concurrent.atomic.AtomicInteger;

public class ProgressBarUi extends BasicProgressBarUI {
    private final AtomicInteger offset = new AtomicInteger(0);
    private final AtomicInteger velocity = new AtomicInteger(1);

    @SuppressWarnings({"MethodOverridesStaticMethodOfSuperclass", "UnusedDeclaration"})
    public static ComponentUI createUI(JComponent component) {
        component.setBorder(JBUI.Borders.empty().asUIResource());
        return new ProgressBarUi();
    }

    @Override
    protected int getBoxLength(int availableLength, int otherDimension) {
        return availableLength;
    }

    @Override
    @NotNull
    public Dimension getPreferredSize(@NotNull JComponent component) {
        return new Dimension(super.getPreferredSize(component).width, JBUIScale.scale(20));
    }

    @Override
    protected void paintIndeterminate(@NotNull Graphics g, @NotNull JComponent component) {
        if (!(g instanceof Graphics2D)) {
            return;
        }
        Graphics2D graphics = (Graphics2D) g;
        Insets insets = progressBar.getInsets();
        int barRectWidth = progressBar.getWidth() - (insets.right + insets.left);
        int barRectHeight = progressBar.getHeight() - (insets.top + insets.bottom);
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return;
        }
        graphics.setColor(new JBColor(Gray._240.withAlpha(50), Gray._128.withAlpha(50)));
        int width = component.getWidth();
        int height = component.getPreferredSize().height;
        if ((component.getHeight() - height) % 2 != 0) {
            height++;
        }
        if (component.isOpaque()) {
            graphics.fillRect(0, (component.getHeight() - height) / 2, width, height);
        }
        graphics.setColor(new JBColor(Gray._165.withAlpha(50), Gray._88.withAlpha(50)));
        final GraphicsConfig config = GraphicsUtil.setupAAPainting(graphics);
        graphics.translate(0, (component.getHeight() - height) / 2);

        final float R = JBUIScale.scale(8f);
        final float R2 = JBUIScale.scale(9f);
        final Area containingRoundRect = new Area(new RoundRectangle2D.Float(1f, 1f, width - 2f, height - 2f, R, R));
        graphics.fill(containingRoundRect);
        offset.set(offset.get() + velocity.get());
        if (offset.get() <= 2) {
            offset.set(2);
            velocity.set(1);
        } else if (offset.get() >= width - JBUIScale.scale(15)) {
            offset.set(width - JBUIScale.scale(15));
            velocity.set(-1);
        }
        Area area = new Area(new Rectangle2D.Float(0, 0, width, height));
        area.subtract(new Area(new RoundRectangle2D.Float(1f, 1f, width - 2f, height - 2f, R, R)));
        if (component.isOpaque()) {
            graphics.fill(area);
        }
        area.subtract(new Area(new RoundRectangle2D.Float(0, 0, width, height, R2, R2)));
        if (component.isOpaque()) {
            graphics.fill(area);
        }

        Icons.SHELL.paintIcon(progressBar, graphics, offset.get() - JBUIScale.scale(3), -JBUIScale.scale(-2));
        Icons.SHELL.paintIcon(progressBar, graphics, width - offset.get() - JBUIScale.scale(3), -JBUIScale.scale(-2));

        graphics.draw(new RoundRectangle2D.Float(1f, 1f, width - 2f - 1f, height - 2f - 1f, R, R));
        graphics.translate(0, -(component.getHeight() - height) / 2);

        if (progressBar.isStringPainted()) {
            if (progressBar.getOrientation() == SwingConstants.HORIZONTAL) {
                paintString(graphics, insets.left, insets.top, barRectWidth, barRectHeight, boxRect.x, boxRect.width);
            } else {
                paintString(graphics, insets.left, insets.top, barRectWidth, barRectHeight, boxRect.y, boxRect.height);
            }
        }
        config.restore();
    }

    @Override
    protected void paintDeterminate(@NotNull Graphics g, @NotNull JComponent component) {
        if (!(g instanceof Graphics2D)) {
            return;
        }
        Graphics2D graphics = (Graphics2D) g;

        if (progressBar.getOrientation() != SwingConstants.HORIZONTAL || !component.getComponentOrientation().isLeftToRight()) {
            super.paintDeterminate(graphics, component);
            return;
        }
        final GraphicsConfig config = GraphicsUtil.setupAAPainting(graphics);
        Insets insets = progressBar.getInsets(); // area for border
        int width = progressBar.getWidth();
        int height = progressBar.getPreferredSize().height;
        if ((component.getHeight() - height) % 2 != 0) {
            height++;
        }
        int barRectWidth = width - (insets.right + insets.left);
        int barRectHeight = height - (insets.top + insets.bottom);
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return;
        }
        int amountFull = getAmountFull(insets, barRectWidth, barRectHeight);
        Container parent = component.getParent();
        Color background = parent != null ? parent.getBackground() : UIUtil.getPanelBackground();
        graphics.setColor(background);
        if (component.isOpaque()) {
            graphics.fillRect(0, 0, width, height);
        }

        final float R = JBUIScale.scale(8f);
        final float R2 = JBUIScale.scale(9f);
        final float off = JBUIScale.scale(1f);
        graphics.translate(0, (component.getHeight() - height) / 2);
        graphics.setColor(progressBar.getForeground());
        graphics.fill(new RoundRectangle2D.Float(0, 0, width - off, height - off, R2, R2));
        graphics.setColor(background);
        graphics.fill(new RoundRectangle2D.Float(off, off, width - 2f * off - off, height - 2f * off - off, R, R));

        graphics.setPaint(getRainbowPaintFromHeight(height));

        graphics.fill(new RoundRectangle2D.Float(2f * off, 2f * off, amountFull - JBUIScale.scale(5f), height - JBUIScale.scale(5f), JBUIScale.scale(7f), JBUIScale.scale(7f)));

        Icons.MARIO.paintIcon(progressBar, graphics, amountFull - JBUIScale.scale(5), -JBUIScale.scale(1));
        graphics.translate(0, -(component.getHeight() - height) / 2);

        if (progressBar.isStringPainted()) {
            paintString(graphics, insets.left, insets.top,
                    barRectWidth, barRectHeight,
                    amountFull, insets);
        }
        config.restore();
    }

    private void paintString(@NotNull Graphics2D graphics, int x, int y, int width, int height, int fillStart, int amountFull) {
        String progressString = progressBar.getString();
        graphics.setFont(progressBar.getFont());
        Point renderLocation = getStringPlacement(graphics, progressString, x, y, width, height);
        Rectangle oldClip = graphics.getClipBounds();

        if (progressBar.getOrientation() == SwingConstants.HORIZONTAL) {
            graphics.setColor(getSelectionBackground());
            BasicGraphicsUtils.drawString(progressBar, graphics, progressString, renderLocation.x, renderLocation.y);

            graphics.setColor(getSelectionForeground());
            graphics.clipRect(fillStart, y, amountFull, height);
            BasicGraphicsUtils.drawString(progressBar, graphics, progressString, renderLocation.x, renderLocation.y);
        } else {
            graphics.setColor(getSelectionBackground());
            AffineTransform rotate = AffineTransform.getRotateInstance(Math.PI / 2);
            graphics.setFont(progressBar.getFont().deriveFont(rotate));
            renderLocation = getStringPlacement(graphics, progressString, x, y, width, height);
            BasicGraphicsUtils.drawString(progressBar, graphics, progressString, renderLocation.x, renderLocation.y);
            graphics.setColor(getSelectionForeground());
            graphics.clipRect(x, fillStart, width, amountFull);
            BasicGraphicsUtils.drawString(progressBar, graphics, progressString, renderLocation.x, renderLocation.y);
        }
        graphics.setClip(oldClip);
    }

    private int getPeriodLength() {
        return JBUIScale.scale(16);
    }

    private LinearGradientPaint getRainbowPaintFromHeight(float scaledHeight) {

        int numRainbowColors = Colors.COLORS.size();
        float epsilon = 0.000001f;

        float[] fractionList = new float[numRainbowColors * 2];
        Color[] colorList = new Color[numRainbowColors * 2];

        for (int i = 0; i < numRainbowColors; i++) {
            fractionList[i * 2] = (float) i / numRainbowColors;
            fractionList[i * 2 + 1] = ((i + 1) - epsilon) / numRainbowColors;

            colorList[i * 2] = Colors.COLORS.get(i);
            colorList[i * 2 + 1] = Colors.COLORS.get(i);
        }

        return new LinearGradientPaint(
                0, JBUI.scale(1),
                0, scaledHeight - JBUI.scale(3),
                fractionList, colorList
        );
    }
}


package com.github.reallyliri.limonitprogressbarplugin


import com.intellij.openapi.ui.GraphicsConfig
import com.intellij.openapi.util.ScalableIcon
import com.intellij.ui.Gray
import com.intellij.ui.JBColor
import com.intellij.util.ui.GraphicsUtil
import com.intellij.util.ui.JBUI
import java.awt.*
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.geom.*
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.SwingConstants
import javax.swing.plaf.ComponentUI
import javax.swing.plaf.basic.BasicGraphicsUtils
import javax.swing.plaf.basic.BasicProgressBarUI


class ProgressBarUi : BasicProgressBarUI() {

    companion object {
        fun createUI(c: JComponent): ComponentUI {
            c.border = JBUI.Borders.empty().asUIResource()
            return ProgressBarUi()
        }

        private fun isEven(value: Int): Boolean {
            return value % 2 == 0
        }
    }

    override fun getPreferredSize(c: JComponent): Dimension {
        return Dimension(super.getPreferredSize(c).width, JBUI.scale(20))
    }

    override fun installListeners() {
        super.installListeners()
        progressBar.addComponentListener(object : ComponentAdapter() {
            override fun componentShown(e: ComponentEvent) {
                super.componentShown(e)
            }

            override fun componentHidden(e: ComponentEvent) {
                super.componentHidden(e)
            }
        })
    }

    @Volatile
    protected var offset = 0

    @Volatile
    protected var offset2 = 0

    @Volatile
    protected var velocity = 1
    override fun paintIndeterminate(g2d: Graphics, c: JComponent) {
        if (g2d !is Graphics2D) {
            return
        }
        val g = g2d
        val b: Insets = progressBar.insets // area for border
        val barRectWidth: Int = progressBar.width - (b.right + b.left)
        val barRectHeight: Int = progressBar.height - (b.top + b.bottom)
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return
        }
        //boxRect = getBox(boxRect);
        g.color = JBColor(Gray._240.withAlpha(50), Gray._128.withAlpha(50))
        val w: Int = c.width
        var h: Int = c.preferredSize.height
        if (!isEven(c.height - h)) h++
        val baseRainbowPaint: LinearGradientPaint = getRainbowPaintFromHeight(h.toFloat())
        g.paint = baseRainbowPaint
        if (c.isOpaque) {
            g.fillRect(0, (c.height - h) / 2, w, h)
        }
        g.color = JBColor(Gray._165.withAlpha(50), Gray._88.withAlpha(50))
        val config: GraphicsConfig = GraphicsUtil.setupAAPainting(g)
        g.translate(0, (c.height - h) / 2)
        var x = -offset.toDouble()
        val old: Paint = g.paint
        g.paint = baseRainbowPaint
        val R = JBUI.scale(8f)
        val R2 = JBUI.scale(9f)
        val containingRoundRect = Area(RoundRectangle2D.Float(1f, 1f, w - 2f, h - 2f, R, R))
        while (x < Math.max(c.width, c.height)) {
            val path = Path2D.Double()
            val ww = periodLength / 2f
            path.moveTo(x, 0.0)
            path.lineTo(x + ww, 0.0)
            path.lineTo(x + ww - h / 2.0, h.toDouble())
            path.lineTo(x - h / 2, h.toDouble())
            path.lineTo(x, 0.0)
            path.closePath()
            val area = Area(path)
            area.intersect(containingRoundRect)
            g.fill(area)
            x += periodLength
        }
        g.paint = old
        offset = (offset + 1) % periodLength
        offset2 += velocity
        if (offset2 <= 2) {
            offset2 = 2
            velocity = 1
        } else if (offset2 >= w - JBUI.scale(15)) {
            offset2 = w - JBUI.scale(15)
            velocity = -1
        }
        //        offset2 = (offset2 + 1) % (w - 3);
        val area = Area(Rectangle2D.Float(0f, 0f, w.toFloat(), h.toFloat()))
        area.subtract(Area(RoundRectangle2D.Float(1f, 1f, w - 2f, h - 2f, R, R)))
        g.paint = Gray._128
        //        g.setPaint(baseRainbowPaint);
        if (c.isOpaque) {
            g.fill(area)
        }
        area.subtract(Area(RoundRectangle2D.Float(0f, 0f, w.toFloat(), h.toFloat(), R2, R2)))
        g.paint = c.parent.background
        //        g.setPaint(baseRainbowPaint);
        if (c.isOpaque) {
            g.fill(area)
        }

//        g.setPaint(baseRainbowPaint);
        val scaledIcon: Icon =
            if (velocity > 0) Icons.CAT_ICON as ScalableIcon else Icons.RCAT_ICON as ScalableIcon
        //        if (velocity < 0) {
//            scaledIcon = new ReflectedIcon(scaledIcon);
//        }
        scaledIcon.paintIcon(progressBar, g, offset2 - JBUI.scale(10), -JBUI.scale(6))
        g.draw(RoundRectangle2D.Float(1f, 1f, w - 2f - 1f, h - 2f - 1f, R, R))
        g.translate(0, -(c.height - h) / 2)

        // Deal with possible text painting
        if (progressBar.isStringPainted) {
            if (progressBar.orientation === SwingConstants.HORIZONTAL) {
                paintString(g, b.left, b.top, barRectWidth, barRectHeight, boxRect.x, boxRect.width)
            } else {
                paintString(g, b.left, b.top, barRectWidth, barRectHeight, boxRect.y, boxRect.height)
            }
        }
        config.restore()
    }

    override fun paintDeterminate(g: Graphics, c: JComponent) {
        if (g !is Graphics2D) {
            return
        }
        if (progressBar.orientation !== SwingConstants.HORIZONTAL || !c.componentOrientation
                .isLeftToRight
        ) {
            super.paintDeterminate(g, c)
            return
        }
        val config: GraphicsConfig = GraphicsUtil.setupAAPainting(g)
        val b: Insets = progressBar.insets // area for border
        val w: Int = progressBar.width
        var h: Int = progressBar.preferredSize.height
        if (!isEven(c.height - h)) h++
        val barRectWidth: Int = w - (b.right + b.left)
        val barRectHeight: Int = h - (b.top + b.bottom)
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return
        }
        val amountFull: Int = getAmountFull(b, barRectWidth, barRectHeight)
        g.setColor(c.parent.background)
        val g2 = g
        if (c.isOpaque) {
            g.fillRect(0, 0, w, h)
        }
        val R = JBUI.scale(8f)
        val R2 = JBUI.scale(9f)
        val off = JBUI.scale(1f)
        g2.translate(0, (c.height - h) / 2)
        g2.color = progressBar.foreground
        g2.fill(RoundRectangle2D.Float(0f, 0f, w - off, h - off, R2, R2))
        g2.color = c.parent.background
        g2.fill(RoundRectangle2D.Float(off, off, w - 2f * off - off, h - 2f * off - off, R, R))
        //        g2.setColor(progressBar.getForeground());
        g2.paint = getRainbowPaintFromHeight(h.toFloat())
        Icons.CAT_ICON.paintIcon(progressBar, g2, amountFull - JBUI.scale(10), -JBUI.scale(6))
        g2.fill(
            RoundRectangle2D.Float(
                2f * off,
                2f * off,
                amountFull - JBUI.scale(5f),
                h - JBUI.scale(5f),
                JBUI.scale(7f),
                JBUI.scale(7f)
            )
        )
        g2.translate(0, -(c.height - h) / 2)

        // Deal with possible text painting
        if (progressBar.isStringPainted) {
            paintString(
                g, b.left, b.top,
                barRectWidth, barRectHeight,
                amountFull, b
            )
        }
        config.restore()
    }

    private fun paintString(g: Graphics, x: Int, y: Int, w: Int, h: Int, fillStart: Int, amountFull: Int) {
        if (g !is Graphics2D) {
            return
        }
        val g2 = g
        val progressString: String = progressBar.string
        g2.font = progressBar.font
        var renderLocation: Point = getStringPlacement(
            g2, progressString,
            x, y, w, h
        )
        val oldClip: Rectangle = g2.clipBounds
        if (progressBar.orientation === SwingConstants.HORIZONTAL) {
            g2.color = selectionBackground
            BasicGraphicsUtils.drawString(
                progressBar, g2, progressString,
                renderLocation.x.toFloat(), renderLocation.y.toFloat()
            )
            g2.color = selectionForeground
            g2.clipRect(fillStart, y, amountFull, h)
            BasicGraphicsUtils.drawString(
                progressBar, g2, progressString,
                renderLocation.x.toFloat(), renderLocation.y.toFloat()
            )
        } else { // VERTICAL
            g2.color = selectionBackground
            val rotate: AffineTransform = AffineTransform.getRotateInstance(Math.PI / 2)
            g2.font = progressBar.font.deriveFont(rotate)
            renderLocation = getStringPlacement(
                g2, progressString,
                x, y, w, h
            )
            BasicGraphicsUtils.drawString(
                progressBar, g2, progressString,
                renderLocation.x.toFloat(), renderLocation.y.toFloat()
            )
            g2.color = selectionForeground
            g2.clipRect(x, fillStart, w, amountFull)
            BasicGraphicsUtils.drawString(
                progressBar, g2, progressString,
                renderLocation.x.toFloat(), renderLocation.y.toFloat()
            )
        }
        g2.clip = oldClip
    }

    /** Create a gradient such as [0, 0.99, 1, 1.99, ...], [RED, RED, ORANGE, ORANGE, ..]  */
    private fun getRainbowPaintFromHeight(scaledHeight: Float): LinearGradientPaint {
        val numColors = Colors.RAINBOW_ARRAY.size
        val epsilon = 0.000001f
        val fractionList = FloatArray(numColors * 2)
        val colorList: Array<Color?> = arrayOfNulls<Color>(numColors * 2)
        for (i in 0 until numColors) {
            fractionList[i * 2] = i.toFloat() / numColors
            fractionList[i * 2 + 1] = (i + 1 - epsilon) / numColors
            colorList[i * 2] = Colors.RAINBOW_ARRAY[i]
            colorList[i * 2 + 1] = Colors.RAINBOW_ARRAY[i]
        }
        return LinearGradientPaint(
            0f, JBUI.scale(1f),
            0f, scaledHeight - JBUI.scale(3),
            fractionList, colorList
        )
    }

    override fun getBoxLength(availableLength: Int, otherDimension: Int): Int {
        return availableLength
    }

    protected val periodLength: Int
        protected get() = JBUI.scale(16)
}
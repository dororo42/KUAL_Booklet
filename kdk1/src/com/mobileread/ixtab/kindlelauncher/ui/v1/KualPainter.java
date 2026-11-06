/*
 * Decompiled with CFR 0.152.
 */
package com.mobileread.ixtab.kindlelauncher.ui.v1;

import com.mobileread.ixtab.kindlelauncher.ui.v1.KualFonts;
import com.mobileread.ixtab.kindlelauncher.ui.v1.KualPaintable;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class KualPainter {
    private KualPainter() {
    }

    public static void paint(Graphics graphics, KualPaintable paintable) {
        Component component = (Component)((Object)paintable);
        KualFonts fonts = KualFonts.getInstance(component);
        paintable.paintBorder(graphics);
        graphics.setColor(paintable.getTextColor());
        String text = paintable.getText();
        char[] chars = text.toCharArray();
        float alignX = paintable.getTextAlignmentX();
        float alignY = paintable.getTextAlignmentY();
        if (fonts.isNonUnicode(chars)) {
            int textX = 0;
            if (alignX == 0.5f) {
                textX = component.getWidth() / 2 - fonts.defaultFontMetrics.stringWidth(text) / 2;
            }
            int textY = 0;
            if (alignY == 0.5f) {
                textY = KualPainter.getCenteredTextYPosition(fonts.defaultFontMetrics, component.getHeight());
            }
            graphics.setFont(fonts.defaultFont);
            graphics.drawString(text, textX, textY);
        } else {
            int textX = 0;
            int[] offsets = fonts.calculateOffsets(chars);
            if (alignX == 0.5f) {
                textX = component.getWidth() / 2 - offsets[chars.length] / 2;
            }
            int textY = 0;
            if (alignY == 0.5f) {
                textY = KualPainter.getCenteredTextYPosition(fonts.unicodeFontMetrics, component.getHeight());
            }
            // C-3: batch consecutive same-font chars into one setFont + drawChars
            // call (pixel-identical: offsets are cumulative char widths).
            int i = 0;
            while (i < chars.length) {
                Font runFont = fonts.getFontFor(chars[i]);
                int start = i;
                while (i < chars.length && fonts.getFontFor(chars[i]) == runFont) {
                    ++i;
                }
                graphics.setFont(runFont);
                graphics.drawChars(chars, start, i - start, textX + offsets[start], textY);
            }
        }
    }

    private static int getCenteredTextYPosition(FontMetrics fm, int height) {
        return height + (1 - height) / 2 - 1 - (fm.getAscent() + fm.getDescent()) / 2 + fm.getAscent();
    }
}


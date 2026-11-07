/*
 * Decompiled with CFR 0.152.
 */
package com.mobileread.ixtab.kindlelauncher.ui.v1;

import com.mobileread.ixtab.kindlelauncher.ui.v1.KualFonts;
import com.mobileread.ixtab.kindlelauncher.ui.v1.KualPaintable;
import java.awt.Component;
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
            for (int i = 0; i < chars.length; ++i) {
                graphics.setFont(fonts.getFontFor(chars[i]));
                graphics.drawChars(chars, i, 1, textX + offsets[i], textY);
            }
        }
    }

    private static int getCenteredTextYPosition(FontMetrics fm, int height) {
        return height + (1 - height) / 2 - 1 - (fm.getAscent() + fm.getDescent()) / 2 + fm.getAscent();
    }
}


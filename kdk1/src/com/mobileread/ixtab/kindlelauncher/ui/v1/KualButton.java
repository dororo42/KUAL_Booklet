/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.amazon.kindle.kindlet.ui.KButton
 */
package com.mobileread.ixtab.kindlelauncher.ui.v1;

import com.amazon.kindle.kindlet.ui.KButton;
import com.mobileread.ixtab.kindlelauncher.resources.KualEntry;
import com.mobileread.ixtab.kindlelauncher.ui.v1.KualPaintable;
import com.mobileread.ixtab.kindlelauncher.ui.v1.KualPainter;
import java.awt.Color;
import java.awt.Graphics;

public class KualButton
extends KButton
implements KualPaintable {
    private static final long serialVersionUID = 1L;
    private static final int BORDER_PX = 2;
    private static final int GAP_PX = 0;
    private static final int ARC_PX = 15;
    private KualEntry kualEntry = null;

    public KualButton(String text, KualEntry kualEntry) {
        super(text);
        this.kualEntry = kualEntry;
    }

    public KualEntry getKualEntry() {
        return this.kualEntry;
    }

    public void paint(Graphics g) {
        KualPainter.paint(g, this);
    }

    public Color getTextColor() {
        return this.isEnabled() ? (this.hasFocus() ? COLOR_BACKGROUND : COLOR_FOREGROUND) : COLOR_DISABLED;
    }

    public void paintBorder(Graphics g) {
        Color foreground = this.isEnabled() ? COLOR_FOREGROUND : COLOR_DISABLED;
        Color background = COLOR_BACKGROUND;
        int width = this.getWidth();
        int height = this.getHeight();
        g.setColor(foreground);
        g.fillRoundRect(0, 0, width - 0, height - 0, 15, 15);
        if (!this.hasFocus()) {
            g.setColor(background);
            g.fillRoundRect(2, 2, width - 4, height - 4, 15, 15);
        }
    }

    public String getText() {
        return this.getLabel();
    }

    public float getTextAlignmentX() {
        return 0.5f;
    }

    public float getTextAlignmentY() {
        return 0.5f;
    }
}


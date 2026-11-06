/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.amazon.kindle.kindlet.ui.KLabel
 */
package com.mobileread.ixtab.kindlelauncher.ui.v1;

import com.amazon.kindle.kindlet.ui.KLabel;
import com.mobileread.ixtab.kindlelauncher.ui.v1.KualPaintable;
import com.mobileread.ixtab.kindlelauncher.ui.v1.KualPainter;
import java.awt.Color;
import java.awt.Graphics;

public class KualLabel
extends KLabel
implements KualPaintable {
    private static final long serialVersionUID = 1L;

    public KualLabel(String text) {
        super(text);
    }

    public void paint(Graphics g) {
        KualPainter.paint(g, this);
    }

    public void paintBorder(Graphics g) {
    }

    public Color getTextColor() {
        return COLOR_FOREGROUND;
    }

    public float getTextAlignmentX() {
        return 0.0f;
    }

    public float getTextAlignmentY() {
        return 0.5f;
    }
}


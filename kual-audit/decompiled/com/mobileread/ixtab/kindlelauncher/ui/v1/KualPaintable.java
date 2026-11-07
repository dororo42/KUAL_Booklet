/*
 * Decompiled with CFR 0.152.
 */
package com.mobileread.ixtab.kindlelauncher.ui.v1;

import java.awt.Color;
import java.awt.Graphics;

public interface KualPaintable {
    public static final Color COLOR_FOREGROUND = Color.BLACK;
    public static final Color COLOR_BACKGROUND = Color.WHITE;
    public static final Color COLOR_DISABLED = Color.LIGHT_GRAY;

    public void paintBorder(Graphics var1);

    public Color getTextColor();

    public String getText();

    public float getTextAlignmentX();

    public float getTextAlignmentY();
}


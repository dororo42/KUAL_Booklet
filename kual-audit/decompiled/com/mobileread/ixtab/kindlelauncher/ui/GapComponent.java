/*
 * Decompiled with CFR 0.152.
 */
package com.mobileread.ixtab.kindlelauncher.ui;

import java.awt.Component;
import java.awt.Dimension;

public class GapComponent
extends Component {
    private static final long serialVersionUID = 1L;
    private final Dimension size;

    public GapComponent(int size) {
        this.size = new Dimension(size, size);
        this.setEnabled(false);
        this.setFocusable(false);
    }

    public Dimension getSize() {
        return this.size;
    }

    public Dimension getSize(Dimension rv) {
        return this.size;
    }

    public Dimension getPreferredSize() {
        return this.size;
    }

    public Dimension getMinimumSize() {
        return this.size;
    }

    public Dimension getMaximumSize() {
        return this.size;
    }
}


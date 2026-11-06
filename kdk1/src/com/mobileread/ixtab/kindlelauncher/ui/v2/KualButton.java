/*
 * Decompiled with CFR 0.152.
 */
package com.mobileread.ixtab.kindlelauncher.ui.v2;

import com.mobileread.ixtab.kindlelauncher.resources.KualEntry;
import javax.swing.JButton;

public class KualButton
extends JButton {
    private static final long serialVersionUID = 1L;
    private KualEntry kualEntry = null;

    public KualButton(String text, KualEntry kualEntry) {
        super(text);
        this.kualEntry = kualEntry;
    }

    public KualEntry getKualEntry() {
        return this.kualEntry;
    }
}


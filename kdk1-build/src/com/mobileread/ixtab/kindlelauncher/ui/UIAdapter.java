/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.amazon.kindle.kindlet.KindletContext
 */
package com.mobileread.ixtab.kindlelauncher.ui;

import com.amazon.kindle.kindlet.KindletContext;
import com.mobileread.ixtab.kindlelauncher.resources.KualEntry;
import com.mobileread.ixtab.kindlelauncher.ui.v1.KUIAdapter;
import com.mobileread.ixtab.kindlelauncher.ui.v2.JUIAdapter;
import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

public abstract class UIAdapter {
    public static final UIAdapter INSTANCE = UIAdapter.createInstance();

    private static UIAdapter createInstance() {
        try {
            Class.forName("com.amazon.kindle.kindlet.ui.KPanel");
            return new KUIAdapter();
        }
        catch (Throwable t) {
            return new JUIAdapter();
        }
    }

    public abstract Container newPanel(LayoutManager var1);

    public abstract Component newLabel(String var1);

    public abstract Component newButton(String var1, ActionListener var2, KeyListener var3, KualEntry var4);

    public abstract void setText(Component var1, String var2);

    public abstract void suicide(KindletContext var1);

    public abstract int getDefaultPageSize();

    public abstract KualEntry getKualEntry(Component var1);

    public int getGap() {
        return 5;
    }

    public abstract void setupUserFont(Container var1, String var2, int var3);
}


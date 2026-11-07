/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.amazon.kindle.kindlet.KindletContext
 *  com.amazon.kindle.kindlet.ui.KButton
 *  com.amazon.kindle.kindlet.ui.KLabel
 *  com.amazon.kindle.kindlet.ui.KPanel
 */
package com.mobileread.ixtab.kindlelauncher.ui.v1;

import com.amazon.kindle.kindlet.KindletContext;
import com.amazon.kindle.kindlet.ui.KButton;
import com.amazon.kindle.kindlet.ui.KLabel;
import com.amazon.kindle.kindlet.ui.KPanel;
import com.mobileread.ixtab.kindlelauncher.resources.KualEntry;
import com.mobileread.ixtab.kindlelauncher.ui.UIAdapter;
import com.mobileread.ixtab.kindlelauncher.ui.v1.KualButton;
import com.mobileread.ixtab.kindlelauncher.ui.v1.KualLabel;
import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KUIAdapter
extends UIAdapter {
    private static String userFontFamily;
    private static int userFontStyle;

    public void setupUserFont(Container root, String fontFamily, int fontStyle) {
        userFontFamily = fontFamily;
        userFontStyle = fontStyle;
    }

    public static String getUserFontFamily() {
        return userFontFamily;
    }

    public static int getUserFontStyle() {
        return userFontStyle;
    }

    public Container newPanel(LayoutManager layout) {
        return layout != null ? new KPanel(layout) : new KPanel();
    }

    public Component newLabel(String text) {
        return new KualLabel(text);
    }

    public Component newButton(String text, ActionListener listener, KeyListener keyListener, KualEntry kualEntry) {
        KualButton button = new KualButton(text, kualEntry);
        if (listener != null) {
            button.setName(text);
            button.addActionListener(listener);
        }
        if (keyListener != null) {
            button.addKeyListener(keyListener);
        }
        return button;
    }

    public void setText(Component component, String text) {
        if (component instanceof KLabel) {
            ((KLabel)component).setText(text);
            component.repaint();
        }
        if (component instanceof KButton) {
            ((KButton)component).setLabel(text);
        }
    }

    public void suicide(KindletContext context) {
        int code = 61441;
        KeyEvent k = new KeyEvent(context.getRootContainer(), 401, System.currentTimeMillis(), 0, code, '\u0000');
        context.getRootContainer().dispatchEvent(k);
    }

    public int getDefaultPageSize() {
        return 5;
    }

    public KualEntry getKualEntry(Component component) {
        if (component instanceof KualButton) {
            return ((KualButton)((Object)component)).getKualEntry();
        }
        return null;
    }
}


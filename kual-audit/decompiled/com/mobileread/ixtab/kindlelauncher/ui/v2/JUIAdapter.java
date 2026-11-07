/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.amazon.kindle.kindlet.KindletContext
 */
package com.mobileread.ixtab.kindlelauncher.ui.v2;

import com.amazon.kindle.kindlet.KindletContext;
import com.mobileread.ixtab.kindlelauncher.resources.KualEntry;
import com.mobileread.ixtab.kindlelauncher.ui.UIAdapter;
import com.mobileread.ixtab.kindlelauncher.ui.v2.KualButton;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JUIAdapter
extends UIAdapter {
    private static Font defaultFont;
    private static Font userFont;

    public void setupUserFont(Container root, String fontFamily, int fontStyle) {
        defaultFont = root.getFont();
        userFont = new Font(fontFamily, fontStyle, defaultFont.getSize());
        if (userFont.getFamily().equals(defaultFont.getFamily())) {
            userFont = defaultFont;
        }
    }

    public Container newPanel(LayoutManager layout) {
        return layout != null ? new JPanel(layout) : new JPanel();
    }

    public Component newLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(userFont);
        return label;
    }

    public Component newButton(String text, ActionListener listener, KeyListener keyListener, KualEntry kualEntry) {
        KualButton button = new KualButton(text, kualEntry);
        button.setFont(userFont);
        if (listener != null) {
            button.setName(text);
            button.addActionListener(listener);
        }
        return button;
    }

    public void setText(Component component, String text) {
        if (component instanceof JLabel) {
            ((JLabel)component).setText(text);
        }
        if (component instanceof JButton) {
            ((JButton)component).setText(text);
        }
    }

    public void suicide(KindletContext context) {
        try {
            Runtime.getRuntime().exec("lipc-set-prop com.lab126.appmgrd stop app://com.lab126.booklet.kindlet");
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public int getDefaultPageSize() {
        return 10;
    }

    public KualEntry getKualEntry(Component component) {
        if (component instanceof KualButton) {
            return ((KualButton)component).getKualEntry();
        }
        return null;
    }
}


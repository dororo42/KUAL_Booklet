/*
 * Decompiled with CFR 0.152.
 */
package com.mobileread.ixtab.kindlelauncher.ui.v1;

import com.mobileread.ixtab.kindlelauncher.ui.v1.KUIAdapter;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class KualFonts {
    public final Font defaultFont;
    public final FontMetrics defaultFontMetrics;
    public final Font unicodeFont;
    public final FontMetrics unicodeFontMetrics;
    private static final Map INSTANCES = new HashMap();

    private KualFonts(Font defaultFont, FontMetrics defaultFontMetrics, Font unicodeFont, FontMetrics unicodeFontMetrics) {
        this.defaultFont = defaultFont;
        this.defaultFontMetrics = defaultFontMetrics;
        this.unicodeFont = unicodeFont;
        this.unicodeFontMetrics = unicodeFontMetrics;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static KualFonts getInstance(Component caller) {
        KualFonts result = (KualFonts)INSTANCES.get(caller.getClass());
        if (result == null) {
            Class clazz = KualFonts.class;
            synchronized (clazz) {
                Font callerFont = caller.getFont();
                Font defaultFont = new Font(KUIAdapter.getUserFontFamily(), KUIAdapter.getUserFontStyle(), callerFont.getSize());
                if (defaultFont.getFamily().equals("dialog") && !defaultFont.getName().equals("dialog")) {
                    defaultFont = callerFont;
                }
                FontMetrics defaultFontMetrics = caller.getFontMetrics(defaultFont);
                Font unicodeFont = null;
                FontMetrics unicodeFontMetrics = null;
                try {
                    unicodeFont = new File("/usr/java/lib/fonts/code2000.ttf").exists() ? new Font("code2000", defaultFont.getStyle(), defaultFont.getSize()) : new Font("symbol", defaultFont.getStyle(), defaultFont.getSize());
                    unicodeFontMetrics = caller.getFontMetrics(unicodeFont);
                }
                catch (Throwable t) {
                    unicodeFont = null;
                    unicodeFontMetrics = null;
                }
                result = new KualFonts(defaultFont, defaultFontMetrics, unicodeFont, unicodeFontMetrics);
                INSTANCES.put(caller.getClass(), result);
            }
        }
        return result;
    }

    public boolean isNonUnicode(char[] chars) {
        return this.unicodeFont == null || this.isAsciiText(chars);
    }

    public boolean isAsciiText(char[] chars) {
        for (int i = 0; i < chars.length; ++i) {
            if (!this.isUnicode(chars[i])) continue;
            return false;
        }
        return true;
    }

    private boolean isUnicode(char c) {
        return c > '\u00ff';
    }

    public int[] calculateOffsets(char[] chars) {
        int[] offsets = new int[chars.length + 1];
        for (int i = 1; i <= chars.length; ++i) {
            char c = chars[i - 1];
            FontMetrics fm = this.isUnicode(c) ? this.unicodeFontMetrics : this.defaultFontMetrics;
            offsets[i] = offsets[i - 1] + fm.charWidth(c);
        }
        return offsets;
    }

    public Font getFontFor(char c) {
        return this.isUnicode(c) ? this.unicodeFont : this.defaultFont;
    }
}


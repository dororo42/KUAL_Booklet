/*
 * Decompiled with CFR 0.152.
 */
package com.mobileread.ixtab.kindlelauncher.timer;

import com.mobileread.ixtab.kindlelauncher.timer.JTimerAdapter;
import com.mobileread.ixtab.kindlelauncher.timer.KTimerAdapter;

public abstract class TimerAdapter {
    public static final TimerAdapter INSTANCE = TimerAdapter.createInstance();

    private static TimerAdapter createInstance() {
        try {
            Class.forName("com.amazon.kindle.kindlet.ui.KPanel");
            return new KTimerAdapter();
        }
        catch (Throwable t) {
            return new JTimerAdapter();
        }
    }

    public abstract Object newTimer();

    public abstract Object newTimerTask(Runnable var1);

    public abstract void schedule(Object var1, Object var2, long var3, long var5);

    public abstract void cancel(Object var1);
}


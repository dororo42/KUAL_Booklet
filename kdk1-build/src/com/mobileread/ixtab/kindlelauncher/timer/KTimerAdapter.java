/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.amazon.kindle.kindlet.util.Timer
 *  com.amazon.kindle.kindlet.util.TimerTask
 */
package com.mobileread.ixtab.kindlelauncher.timer;

import com.amazon.kindle.kindlet.util.Timer;
import com.amazon.kindle.kindlet.util.TimerTask;
import com.mobileread.ixtab.kindlelauncher.timer.TimerAdapter;

public class KTimerAdapter
extends TimerAdapter {
    public Object newTimer() {
        return new Timer();
    }

    public Object newTimerTask(final Runnable runnable) {
        return new TimerTask(){

            public void run() {
                runnable.run();
            }
        };
    }

    public void schedule(Object timerObject, Object timerTaskObject, long delay, long period) {
        Timer timer = (Timer)timerObject;
        TimerTask timerTask = (TimerTask)timerTaskObject;
        timer.schedule(timerTask, delay, period);
    }

    public void cancel(Object timerObject) {
        Timer timer = (Timer)timerObject;
        timer.cancel();
    }
}


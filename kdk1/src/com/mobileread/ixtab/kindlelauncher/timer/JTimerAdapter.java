/*
 * Decompiled with CFR 0.152.
 */
package com.mobileread.ixtab.kindlelauncher.timer;

import com.mobileread.ixtab.kindlelauncher.timer.TimerAdapter;
import java.util.Timer;
import java.util.TimerTask;

public class JTimerAdapter
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


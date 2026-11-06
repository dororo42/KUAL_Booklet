/*
 * Decompiled with CFR 0.152.
 */
package com.mobileread.ixtab.kindlelauncher.resources;

import com.mobileread.ixtab.kindlelauncher.Util;
import com.mobileread.ixtab.kindlelauncher.resources.KualMenu;
import com.mobileread.ixtab.kindlelauncher.resources.MailboxCommand;
import com.mobileread.ixtab.kindlelauncher.timer.TimerAdapter;
import java.io.BufferedReader;
import java.io.File;

public class MailboxProcessor {
    private KualMenu km;
    private int eventId;
    private MailboxCommand command;
    private long delay;
    private long period;
    private int countdown;
    private String mailboxPath;
    private final TimerAdapter tmi = MailboxProcessor.getTimer();
    private final Object timer = this.tmi.newTimer();
    private String message = null;
    private BufferedReader mailbox = null;

    public MailboxProcessor(KualMenu km, int eventId, MailboxCommand command, long delay, long period, int countdown) {
        this.km = km;
        this.eventId = eventId;
        this.command = command;
        this.delay = delay;
        this.period = period;
        this.countdown = countdown;
        this.mailboxPath = km.getMailboxPath();
        if (0 < countdown) {
            this.monitor();
        } else {
            if (0L < delay) {
                try {
                    Thread.sleep(delay);
                }
                catch (InterruptedException interruptedException) {
                    // empty catch block
                }
            }
            if (this.isRinging()) {
                if (null != this.message) {
                    this.process(command);
                }
                this.resetMailbox();
            }
        }
    }

    private static TimerAdapter getTimer() {
        return TimerAdapter.INSTANCE;
    }

    private void monitor() {
        Runnable runnable = new Runnable(){

            public void run() {
                boolean gotMail = MailboxProcessor.this.isRinging();
                if (gotMail || --MailboxProcessor.this.countdown <= 0) {
                    MailboxProcessor.this.tmi.cancel(MailboxProcessor.this.timer);
                }
                if (null != MailboxProcessor.this.message) {
                    MailboxProcessor.this.process(MailboxProcessor.this.command);
                }
                if (gotMail) {
                    MailboxProcessor.this.resetMailbox();
                }
            }
        };
        Object task = this.tmi.newTimerTask(runnable);
        this.tmi.schedule(this.timer, task, this.delay, this.period);
    }

    private void resetMailbox() throws RuntimeException {
        try {
            this.mailbox.close();
            File f = new File(this.mailboxPath);
            f.delete();
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private boolean isRinging() {
        this.mailbox = Util.sureFileReader(this.mailboxPath);
        if (null != this.mailbox) {
            try {
                this.message = this.mailbox.readLine();
            }
            catch (Throwable t) {
                this.message = null;
            }
            return true;
        }
        return false;
    }

    public void callCommand(MailboxCommand command, Object data) {
        command.execute(data);
    }

    private void process(MailboxCommand command) {
        try {
            if (null != this.mailbox) {
                this.mailbox.close();
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        if (null == this.message) {
            return;
        }
        char id = this.message.charAt(0);
        if (this.eventId == id) {
            switch (id) {
                case '1': {
                    BufferedReader cacheReader = Util.sureFileReader(this.message.substring(2));
                    if (null == cacheReader) break;
                    this.callCommand(command, cacheReader);
                }
            }
        }
    }
}


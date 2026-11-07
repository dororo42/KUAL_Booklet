/*
 * Decompiled with CFR 0.152.
 */
package com.mobileread.ixtab.kindlelauncher.resources;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class KualLog {
    private final String logfilePath = "/var/tmp/KUAL.log";

    public void append(String line) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("/var/tmp/KUAL.log", true));
            bw.write("KUAL: " + line);
            bw.newLine();
            bw.close();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    public void printStackTrace(Throwable t) {
        try {
            PrintWriter pw = new PrintWriter((Writer)new FileWriter("/var/tmp/KUAL.log", true), true);
            t.printStackTrace(pw);
            pw.flush();
            pw.close();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }
}


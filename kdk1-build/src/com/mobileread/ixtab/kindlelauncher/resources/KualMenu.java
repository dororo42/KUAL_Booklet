/*
 * Decompiled with CFR 0.152.
 */
package com.mobileread.ixtab.kindlelauncher.resources;

import com.mobileread.ixtab.kindlelauncher.resources.KualConfig;
import com.mobileread.ixtab.kindlelauncher.resources.KualEntry;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashMap;

public class KualMenu {
    private static final int MAXLEVEL = 10;
    private final LinkedHashMap[] levelMap = new LinkedHashMap[10];
    private KualConfig kualConfig;

    public KualMenu(BufferedReader reader) throws IOException, InterruptedException {
        for (int i = 0; i < 10; ++i) {
            this.levelMap[i] = new LinkedHashMap();
        }
        try {
            this.kualConfig = new KualConfig(reader);
            String line = reader.readLine();
            while (line != null) {
                String options = "";
                int level = -1;
                KualEntry ke = null;
                switch (line.charAt(0)) {
                    case '4': {
                        options = reader.readLine();
                    }
                    case '3': {
                        try {
                            ke = new KualEntry(options, reader.readLine(), reader.readLine(), reader.readLine());
                            break;
                        }
                        catch (Throwable t) {
                            throw new Exception("record format " + t.getMessage());
                        }
                    }
                    default: {
                        throw new Exception("input format");
                    }
                }
                this.levelMap[ke.level].put(ke.id, ke);
                line = reader.readLine();
            }
        }
        catch (Throwable t) {
            String report = "error: " + t.getMessage();
            KualEntry ke = new KualEntry(0, report);
            this.levelMap[0].put(ke.id, ke);
        }
    }

    public String getConfig(String name) {
        return this.kualConfig.get(name);
    }

    public String getVersion() {
        return this.kualConfig.getVersion();
    }

    public String getModel() {
        return this.kualConfig.getModel();
    }

    public String getMailboxPath() {
        return this.kualConfig.getMailboxPath();
    }

    public LinkedHashMap getLevel(int level) {
        if (0 <= level && level <= 10) {
            return this.levelMap[level];
        }
        return null;
    }

    public KualEntry getEntry(int level, Object id) {
        if (0 <= level && level <= 10) {
            return (KualEntry)this.levelMap[level].get(id);
        }
        return null;
    }

    public KualEntry getEntry(String id) {
        int level;
        try {
            level = Integer.parseInt(id.substring(0, id.indexOf(":")));
        }
        catch (Throwable t) {
            return null;
        }
        return this.getEntry(level, id);
    }
}


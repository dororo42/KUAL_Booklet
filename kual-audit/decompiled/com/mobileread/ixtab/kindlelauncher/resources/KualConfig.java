/*
 * Decompiled with CFR 0.152.
 */
package com.mobileread.ixtab.kindlelauncher.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class KualConfig {
    private final HashMap configMap = new HashMap();
    private String mailboxPath;
    private String version;

    public KualConfig(BufferedReader reader) throws IOException, InterruptedException, NumberFormatException, Exception {
        this.configMap.clear();
        try {
            int i;
            int size = Integer.parseInt(reader.readLine());
            for (i = 1; i <= size; ++i) {
                this.configMap.put("meta" + Integer.toString(i), reader.readLine());
            }
            this.version = this.get("meta1");
            this.mailboxPath = this.get("meta2");
            size = Integer.parseInt(reader.readLine());
            for (i = 1; i <= size; ++i) {
                String line = reader.readLine();
                int p = line.indexOf(61);
                if (p <= 0) continue;
                this.configMap.put(line.substring(0, p), line.substring(p + 1));
            }
        }
        catch (Throwable t) {
            throw new Exception(t.getMessage());
        }
    }

    public String get(String name) {
        return (String)this.configMap.get(name);
    }

    public String getVersion() {
        return this.version;
    }

    public String getModel() {
        String model = this.get("model");
        String modelnick = "K??";
        if ("Kindle2".equals(model)) {
            modelnick = "K2";
        } else if ("KindleDX".equals(model)) {
            modelnick = "DX";
        } else if ("KindleDXG".equals(model)) {
            modelnick = "DXG";
        } else if ("Kindle3".equals(model)) {
            modelnick = "K3";
        } else if ("Kindle4".equals(model)) {
            modelnick = "K4";
        } else if ("KindleTouch".equals(model)) {
            modelnick = "K5";
        } else if ("KindlePaperWhite".equals(model)) {
            modelnick = "PW";
        } else if ("KindlePaperWhite2".equals(model)) {
            modelnick = "PW2";
        } else if ("KindleBasic".equals(model)) {
            modelnick = "KT2";
        } else if ("KindleVoyage".equals(model)) {
            modelnick = "KV";
        } else if ("KindlePaperWhite3".equals(model)) {
            modelnick = "PW3";
        } else if ("KindleOasis".equals(model)) {
            modelnick = "KOA";
        } else if ("KindleBasic2".equals(model)) {
            modelnick = "KT3";
        } else if ("KindleOasis2".equals(model)) {
            modelnick = "KOA2";
        } else if ("KindlePaperWhite4".equals(model)) {
            modelnick = "PW4";
        } else if ("KindleBasic3".equals(model)) {
            modelnick = "KT4";
        } else if ("KindleOasis3".equals(model)) {
            modelnick = "KOA3";
        } else if ("KindlePaperWhite5".equals(model)) {
            modelnick = "PW5";
        } else if ("KindleBasic4".equals(model)) {
            modelnick = "KT5";
        } else if ("KindleScribe".equals(model)) {
            modelnick = "KS";
        } else if ("KindleBasic5".equals(model)) {
            modelnick = "KT6";
        } else if ("KindlePaperWhite6".equals(model)) {
            modelnick = "PW6";
        } else if ("KindleScribe2".equals(model)) {
            modelnick = "KS2";
        } else if ("KindleColorSoft".equals(model)) {
            modelnick = "CS";
        }
        return modelnick;
    }

    public String getMailboxPath() {
        return this.mailboxPath;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.mobileread.ixtab.kindlelauncher.resources;

public class KualEntry {
    public String id;
    public String label;
    public boolean isSubmenu = false;
    public boolean isInternalAction = false;
    public int internalAction;
    public String internalArgs = null;
    public int level;
    private String levelSnpath;
    public String action;
    public String dir = null;
    public String options = null;
    private String snpath;

    public KualEntry(String options, String levelSnpath, String label, String action) throws Exception {
        if (null == levelSnpath || null == label || null == action) {
            throw new Exception("invalid entry");
        }
        try {
            this.options = options;
            this.levelSnpath = this.id = levelSnpath;
            int p = levelSnpath.indexOf(":");
            this.level = Integer.parseInt(levelSnpath.substring(0, p));
            this.snpath = levelSnpath.substring(p + 1);
            if (action.startsWith("^")) {
                this.isSubmenu = true;
                this.label = label + " \u25bd";
                this.action = action;
                this.dir = null;
            } else {
                this.label = label;
                if (action.startsWith("#")) {
                    this.isInternalAction = true;
                    this.internalAction = action.charAt(1);
                    p = 2 + action.substring(2).indexOf("#");
                    int len = Integer.parseInt(action.substring(2, p));
                    ++p;
                    if (0 < len) {
                        this.internalArgs = action.substring(p, p + len);
                    }
                    action = action.substring(p + len);
                }
                p = action.indexOf(";");
                this.action = action.substring(p + 1);
                this.dir = action.substring(0, p);
            }
        }
        catch (Throwable t) {
            throw new Exception("invalid entry " + t.getMessage());
        }
    }

    public KualEntry(int instanceId, String label) {
        this.action = null;
        this.dir = null;
        switch (instanceId) {
            case 0: {
                this.options = "e";
                this.id = "0:ff";
                this.levelSnpath = "0:ff";
                this.level = 0;
                this.snpath = "ff";
                this.isSubmenu = false;
                this.isInternalAction = true;
                this.internalAction = 0;
                this.internalArgs = "Try restarting \u266b";
                this.label = label;
                break;
            }
            case 1: {
                this.options = "e";
                this.id = "";
                this.levelSnpath = "";
                this.level = 0;
                this.isSubmenu = false;
                this.isInternalAction = true;
                this.internalAction = 1;
                break;
            }
            case 2: {
                this.id = "";
                this.levelSnpath = "";
                this.level = 0;
                this.isSubmenu = false;
                this.isInternalAction = true;
                this.internalAction = 2;
                this.action = ":";
                this.dir = "/var/tmp";
                this.label = label;
                break;
            }
            default: {
                this.options = "e";
                this.id = "0:ff";
                this.levelSnpath = "0:ff";
                this.level = 0;
                this.snpath = "ff";
                this.isSubmenu = false;
                this.isInternalAction = true;
                this.internalAction = 0;
                this.internalArgs = "Error \u266b";
                this.label = label;
            }
        }
    }

    public boolean hasOption(char option) {
        return null == this.options ? false : 0 <= this.options.indexOf(option);
    }

    public void setChecked(boolean enable) {
        boolean isChecked = this.label.startsWith("\u2713");
        if (enable && !isChecked) {
            this.label = "\u2713 " + this.label;
        } else if (isChecked) {
            this.label = this.label.substring(2);
        }
    }

    public String getBareLabel() {
        return this.label.substring(0, this.label.length() - 2);
    }

    public int getGoToLevel() {
        try {
            return Integer.parseInt(this.action.substring(1, this.action.indexOf(":")));
        }
        catch (Throwable t) {
            return 0;
        }
    }

    public String getParentLink() {
        return this.action;
    }

    public boolean isLinkedUnder(String linkerToThis) {
        int dot = linkerToThis.indexOf(".");
        int len = this.levelSnpath.length();
        return linkerToThis.regionMatches(1, this.levelSnpath, 0, dot - 1) && linkerToThis.regionMatches(dot + 2, this.levelSnpath, len - 2, 2) && linkerToThis.length() - 2 == len;
    }
}


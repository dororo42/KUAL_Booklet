/*
 * Decompiled with CFR 0.152.
 */
package com.mobileread.ixtab.kindlelauncher;

import ixtab.jailbreak.Jailbreak;
import java.security.AllPermission;

public class LauncherKindletJailbreak
extends Jailbreak {
    public boolean enable() {
        if (!super.enable()) {
            return false;
        }
        return this.getContext().requestPermission(new AllPermission());
    }
}


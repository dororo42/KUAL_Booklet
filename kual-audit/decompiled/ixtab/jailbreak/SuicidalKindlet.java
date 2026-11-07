/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.amazon.kindle.kindlet.AbstractKindlet
 *  com.amazon.kindle.kindlet.KindletContext
 */
package ixtab.jailbreak;

import com.amazon.kindle.kindlet.AbstractKindlet;
import com.amazon.kindle.kindlet.KindletContext;
import ixtab.jailbreak.Jailbreak;

public abstract class SuicidalKindlet
extends AbstractKindlet {
    protected final Jailbreak jailbreak = this.instantiateJailbreak();
    private final AbstractKindlet delegate;

    public SuicidalKindlet() {
        this(true);
    }

    public SuicidalKindlet(boolean bl) {
        ClassLoader classLoader;
        AbstractKindlet abstractKindlet = null;
        if (bl && this.jailbreak.isAvailable() && (classLoader = this.jailbreak.getContext().classLoader) != ((Object)((Object)this)).getClass().getClassLoader()) {
            try {
                Class<?> clazz = classLoader.loadClass(((Object)((Object)this)).getClass().getName());
                abstractKindlet = (AbstractKindlet)clazz.newInstance();
            }
            catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }
        this.delegate = bl ? this.instantiateDelegate(abstractKindlet) : abstractKindlet;
    }

    protected AbstractKindlet instantiateDelegate(AbstractKindlet abstractKindlet) {
        return abstractKindlet;
    }

    protected Jailbreak instantiateJailbreak() {
        return new Jailbreak();
    }

    public final void create(KindletContext kindletContext) {
        if (this.jailbreak.isAvailable() && this.jailbreak.getMetadata().autoEnable) {
            this.jailbreak.enable();
        }
        if (this.delegate != null) {
            this.delegate.create(kindletContext);
            return;
        }
        this.onCreate(kindletContext);
    }

    public final void start() {
        if (this.delegate != null) {
            this.delegate.start();
            return;
        }
        this.onStart();
    }

    public final void stop() {
        if (this.delegate != null) {
            this.delegate.stop();
            return;
        }
        this.onStop();
    }

    public final void destroy() {
        this.onDestroy();
        if (this.jailbreak.isAvailable() && this.jailbreak.getMetadata().autoDisable) {
            this.jailbreak.disable();
        }
        if (this.delegate != null) {
            this.delegate.destroy();
        }
    }

    protected void onCreate(KindletContext kindletContext) {
        super.create(kindletContext);
    }

    protected void onStart() {
        super.start();
    }

    protected void onStop() {
        super.stop();
    }

    protected void onDestroy() {
        super.destroy();
    }
}


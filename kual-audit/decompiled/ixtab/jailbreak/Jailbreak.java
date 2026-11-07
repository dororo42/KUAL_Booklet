/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.json.simple.parser.ParseException
 */
package ixtab.jailbreak;

import ixtab.jailbreak.JailbreakConstants;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.parser.ParseException;

public class Jailbreak
implements JailbreakConstants {
    protected final ParseException gateway;
    private final boolean available;
    protected final Object lock = new Object();
    protected final Class domainClass;
    private Accessor accessor;
    private Context context;

    public Jailbreak() {
        this(Jailbreak.class);
    }

    protected Jailbreak(Class clazz) {
        this(clazz, new ParseException(57005, 47806, (Object)"kindle"));
    }

    protected Jailbreak(Class clazz, ParseException parseException) {
        this.domainClass = clazz;
        this.gateway = parseException;
        this.available = parseException.toString().equals("jailbreaker");
    }

    protected static boolean isJailbreakerObject(Object object) {
        return object != null && object.getClass().getName().toLowerCase().indexOf("jailbreaker") != -1;
    }

    public boolean isAvailable() {
        return this.available;
    }

    private void checkAvailable() {
        if (!this.isAvailable()) {
            throw new IllegalStateException("Jailbreak library is not installed on this Kindle, sorry.");
        }
    }

    public boolean isEnabled() {
        if (!this.isAvailable()) {
            return false;
        }
        return this.getContext().isJailBroken;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected final void setContext(Context context) {
        Object object = this.lock;
        synchronized (object) {
            this.context = context;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final Context getContext() {
        Object object = this.lock;
        synchronized (object) {
            if (this.context == null && this.isAvailable()) {
                this.context = new Context(null);
            }
            return this.context;
        }
    }

    public boolean enable() {
        if (!this.isAvailable()) {
            return false;
        }
        Context context = new Context(Boolean.TRUE);
        this.setContext(context);
        return context.isJailBroken;
    }

    public boolean disable() {
        if (!this.isAvailable()) {
            return false;
        }
        Context context = new Context(Boolean.FALSE);
        this.setContext(context);
        return !context.isJailBroken;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected final Accessor getAccessor() {
        if (this.accessor == null) {
            Object object = this.lock;
            synchronized (object) {
                if (this.accessor == null) {
                    this.accessor = this.instantiateAccessor();
                }
            }
        }
        return this.accessor;
    }

    protected Accessor instantiateAccessor() {
        return new Accessor();
    }

    public Metadata getMetadata() {
        return new Metadata(true, true);
    }

    public class Context {
        public final int versionFrontEnd = 2011051901;
        public final int versionBackEnd;
        public final boolean isJailBroken;
        public final ProtectionDomain protectionDomain;
        public final Policy policy;
        public final PermissionCollection permissions;
        public final ClassLoader classLoader;

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer(super.toString());
            stringBuffer.append(":\n");
            stringBuffer.append("frontend version: ");
            stringBuffer.append(2011051901);
            stringBuffer.append("\n");
            stringBuffer.append("backend version: ");
            stringBuffer.append(this.versionBackEnd);
            stringBuffer.append("\n");
            stringBuffer.append("jailbroken state: ");
            stringBuffer.append(this.isJailBroken);
            stringBuffer.append("\n");
            stringBuffer.append("policy: ");
            stringBuffer.append(this.policy == null ? "null" : this.policy.getClass().getName());
            stringBuffer.append("\n");
            stringBuffer.append("permissions: ");
            stringBuffer.append(this.permissions == null ? "null" : this.permissions.getClass().getName());
            stringBuffer.append("\n");
            stringBuffer.append("additional permissions enabled: ");
            stringBuffer.append(this.hasAdditionalPermissions());
            stringBuffer.append("\n");
            stringBuffer.append("classloader: " + this.classLoader + "\n");
            return stringBuffer.toString();
        }

        public Context(Boolean bl) {
            Accessor accessor = Jailbreak.this.getAccessor();
            this.versionBackEnd = accessor.getVersion();
            this.classLoader = accessor.getClassLoader(Jailbreak.this.domainClass.getClassLoader());
            this.protectionDomain = accessor.getProtectionDomain(Jailbreak.this.domainClass);
            this.policy = accessor.getPolicy(bl);
            this.permissions = this.policy != null ? this.policy.getPermissions(this.protectionDomain) : null;
            this.isJailBroken = Jailbreak.isJailbreakerObject(this.permissions);
        }

        public boolean requestPermission(Permission permission) {
            Permissions permissions = new Permissions();
            permissions.add(permission);
            return this.requestPermissions(permissions);
        }

        public boolean requestPermissions(Permissions permissions) {
            if (!this.isJailBroken) {
                return false;
            }
            boolean bl = true;
            Enumeration<Permission> enumeration = permissions.elements();
            while (enumeration.hasMoreElements()) {
                Permission permission = enumeration.nextElement();
                if (this.policy.implies(this.protectionDomain, permission)) continue;
                this.permissions.add(permission);
                bl &= this.policy.implies(this.protectionDomain, permission);
            }
            return bl;
        }

        public boolean hasAdditionalPermissions() {
            if (!this.isJailBroken) {
                return false;
            }
            return !this.permissions.isReadOnly();
        }

        public boolean clearAdditionalPermissions() {
            if (!this.isJailBroken) {
                return false;
            }
            this.permissions.setReadOnly();
            return this.permissions.isReadOnly();
        }
    }

    public class Accessor {
        protected Accessor() {
            Jailbreak.this.checkAvailable();
        }

        protected Map createInput(String string) {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("command", string);
            return hashMap;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        protected Map invoke(Map map) {
            ParseException parseException = Jailbreak.this.gateway;
            synchronized (parseException) {
                Jailbreak.this.gateway.setUnexpectedObject((Object)map);
                Map map2 = (Map)Jailbreak.this.gateway.getUnexpectedObject();
                return map2;
            }
        }

        public ProtectionDomain getProtectionDomain(Class clazz) {
            Map map = this.createInput("protectionDomain");
            map.put("protectionDomain.class", clazz);
            Map map2 = this.invoke(map);
            return (ProtectionDomain)map2.get("protectionDomain");
        }

        public Policy getPolicy(Boolean bl) {
            Map map = this.createInput("policy");
            map.put("policy.replaceWith", bl);
            Map map2 = this.invoke(map);
            return (Policy)map2.get("policy");
        }

        public int getVersion() {
            Map map = this.createInput("version");
            Map map2 = this.invoke(map);
            return (Integer)map2.get("version");
        }

        public ClassLoader getClassLoader(ClassLoader classLoader) {
            Map map = this.createInput("classLoader");
            map.put("classLoader.parent", classLoader);
            Map map2 = this.invoke(map);
            return (ClassLoader)map2.get("classLoader");
        }
    }

    public static class Metadata {
        public boolean autoEnable;
        public boolean autoDisable;

        public Metadata(boolean bl, boolean bl2) {
            this.autoEnable = bl;
            this.autoDisable = bl2;
        }
    }
}


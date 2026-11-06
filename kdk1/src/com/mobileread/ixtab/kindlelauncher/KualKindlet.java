/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.amazon.kindle.kindlet.KindletContext
 */
package com.mobileread.ixtab.kindlelauncher;

import com.amazon.kindle.kindlet.KindletContext;
import com.mobileread.ixtab.kindlelauncher.LauncherKindletJailbreak;
import com.mobileread.ixtab.kindlelauncher.Util;
import com.mobileread.ixtab.kindlelauncher.resources.KualEntry;
import com.mobileread.ixtab.kindlelauncher.resources.KualLog;
import com.mobileread.ixtab.kindlelauncher.resources.KualMenu;
import com.mobileread.ixtab.kindlelauncher.resources.MailboxCommand;
import com.mobileread.ixtab.kindlelauncher.resources.MailboxProcessor;
import com.mobileread.ixtab.kindlelauncher.resources.ResourceLoader;
import com.mobileread.ixtab.kindlelauncher.timer.TimerAdapter;
import com.mobileread.ixtab.kindlelauncher.ui.GapComponent;
import com.mobileread.ixtab.kindlelauncher.ui.UIAdapter;
import ixtab.jailbreak.Jailbreak;
import ixtab.jailbreak.SuicidalKindlet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class KualKindlet
extends SuicidalKindlet
implements ActionListener {
    public static final String RESOURCE_PARSER_SCRIPT = "parse.awk";
    private static final String EXEC_PREFIX_PARSE = "klauncher_parse-";
    private static final String EXEC_PREFIX_BACKGROUND = "klauncher_background-";
    private static final String EXEC_EXTENSION_SH = ".sh";
    private static final String EXEC_EXTENSION_AWK = ".awk";
    private static final long serialVersionUID = 1L;
    private static String PRIVILEGE_HINT_PREFIX = "?";
    private static final int VK_KEYBOARD = 17;
    private static final int PAGING_PREVIOUS = -1;
    private static final int PAGING_NEXT = 1;
    private static final int LEVEL_PREVIOUS = -1;
    private static final int LEVEL_NEXT = 1;
    private KualMenu kualMenu;
    private final ArrayList viewList = new ArrayList();
    private KindletContext context;
    private boolean started = false;
    private String commandToRunOnExit = null;
    private String dirToChangeToOnExit = null;
    final String CROSS = "\u00d7";
    final String ATTN = "\u25cf";
    final String RARROW = "\u25b6";
    final String LARROW = "\u25c0";
    final String UARROW = "\u25b2";
    final String BULLET = "\u25aa";
    final String PATH_SEP = "/";
    private KeyListener keyListener = new KeyAdapter(){

        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case 61448: 
                case 61449: {
                    KualKindlet.this.handlePaging(1, KualKindlet.this.depth, true);
                    break;
                }
                case 61450: 
                case 61452: {
                    KualKindlet.this.handleLevel(-1, true);
                    break;
                }
                case 49: 
                case 81: {
                    KualKindlet.this.handleButtonSelect(1, true);
                    break;
                }
                case 50: 
                case 87: {
                    KualKindlet.this.handleButtonSelect(2, true);
                    break;
                }
                case 51: 
                case 69: {
                    KualKindlet.this.handleButtonSelect(3, true);
                    break;
                }
                case 52: 
                case 82: {
                    KualKindlet.this.handleButtonSelect(4, true);
                    break;
                }
                case 53: 
                case 84: {
                    KualKindlet.this.handleButtonSelect(5, true);
                    break;
                }
                case 54: 
                case 89: {
                    KualKindlet.this.handleButtonSelect(6, true);
                    break;
                }
                case 55: 
                case 85: {
                    KualKindlet.this.handleButtonSelect(7, true);
                    break;
                }
                case 56: 
                case 73: {
                    KualKindlet.this.handleButtonSelect(8, true);
                    break;
                }
                case 57: 
                case 79: {
                    KualKindlet.this.handleButtonSelect(9, true);
                    break;
                }
                case 48: 
                case 80: {
                    KualKindlet.this.handleButtonSelect(10, true);
                    break;
                }
                case 10: 
                case 32: {
                    KualKindlet.this.handleLauncherButton((Component)e.getSource(), KualKindlet.this.depth);
                    break;
                }
                case 17: 
                case 61447: {
                    KualKindlet.this.handleButtonSelect(-1, false);
                    break;
                }
                case 65482: {
                    KualKindlet.this.handleButtonSelect(99, false);
                }
            }
            new MailboxProcessor(KualKindlet.this.kualMenu, 49, new ReloadMenuFromCache(), 0L, 0L, 0);
        }
    };
    private Container entriesPanel;
    private Component status = null;
    private Component nextPageButton = KualKindlet.getUI().newButton("  \u25b6  ", this, this.keyListener, null);
    private Component prevPageButton = KualKindlet.getUI().newButton("  \u25b2  ", this, this.keyListener, null);
    private Component breadcrumb;
    private KualEntry toTopEntry;
    private Component toTopButton;
    private KualEntry quitEntry;
    private Component quitButton;
    private int[] offset = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private KualEntry[] keTrail = new KualEntry[]{null, null, null, null, null, null, null, null, null, null};
    private int depth = 0;
    private static int viewLevel = -1;
    private static int viewOffset = -1;
    private static int onStartPageSize = -1;

    protected Jailbreak instantiateJailbreak() {
        return new LauncherKindletJailbreak();
    }

    public void onCreate(KindletContext context) {
        super.onCreate(context);
        this.context = context;
    }

    public void onStart() {
        if (this.started) {
            return;
        }
        super.onStart();
        this.started = true;
        String error = this.getJailbreakError();
        if (error != null) {
            this.displayErrorMessage(error);
            return;
        }
        Runnable runnable = new Runnable(){

            public void run() {
                KualKindlet.this.longStart();
            }
        };
        EventQueue.invokeLater(runnable);
    }

    private void longStart() {
        try {
            this.initializeState();
            this.initializeUI();
            new MailboxProcessor(this.kualMenu, 49, new ReloadMenuFromCache(), 1000L, 500L, 20);
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private void setStatus(String text) {
        if (null == this.status) {
            this.setBreadcrumb(text, null);
        } else {
            KualKindlet.getUI().setText(this.status, text);
        }
    }

    private void setBreadcrumb(String left, String center) {
        String text;
        String string = text = null == left ? "" : left + " ";
        if (null != center) {
            text = text + center;
        } else if (0 == this.depth) {
            text = text + "/";
        } else {
            String label = this.keTrail[this.depth - 1].getBareLabel();
            int width = this.getTrailMaxWidth() - text.length();
            for (int i = this.depth - 2; i >= 0 && label.length() <= width; --i) {
                label = this.keTrail[i].getBareLabel() + "/" + label;
            }
            label = "/" + label;
            int len = label.length();
            if (len > width) {
                label = "..." + label.substring(len - width + 3);
            }
            text = text + label;
        }
        text = PRIVILEGE_HINT_PREFIX + " " + "\u25aa" + " " + text;
        KualKindlet.getUI().setText(this.breadcrumb, text);
    }

    private static UIAdapter getUI() {
        return UIAdapter.INSTANCE;
    }

    private void initializeUI() {
        String userReqStyle;
        Container root = this.context.getRootContainer();
        int gap = KualKindlet.getUI().getGap();
        root.removeAll();
        String currentUsername = System.getProperty("user.name");
        PRIVILEGE_HINT_PREFIX = "root".equals(currentUsername) ? "#" : (new File("/var/local/mkk/gandalf").exists() ? "$" : "%");
        String userReqFamily = this.kualMenu.getConfig("font_family");
        if (null == userReqFamily) {
            userReqFamily = "Futura";
        }
        if (null == (userReqStyle = this.kualMenu.getConfig("font_style"))) {
            userReqStyle = "Regular";
        }
        int userFontStyle = userReqStyle.equals("BoldItalic") ? 3 : (userReqStyle.equals("Bold") ? 1 : (userReqStyle.equals("Italic") ? 2 : 0));
        KualKindlet.getUI().setupUserFont(root, userReqFamily, userFontStyle);
        this.breadcrumb = KualKindlet.getUI().newLabel("/");
        this.toTopEntry = new KualEntry(1, "/");
        this.toTopButton = KualKindlet.getUI().newButton("/", this, this.keyListener, this.toTopEntry);
        this.quitEntry = new KualEntry(2, "\u00d7 Quit");
        this.quitButton = KualKindlet.getUI().newButton("\u00d7 Quit", this, this.keyListener, this.quitEntry);
        root.setLayout(new BorderLayout(gap, gap));
        Container main = KualKindlet.getUI().newPanel(new BorderLayout(gap, gap));
        root.add((Component)main, "Center");
        root.add((Component)new GapComponent(0), "North");
        root.add((Component)new GapComponent(0), "East");
        root.add((Component)new GapComponent(0), "South");
        root.add((Component)new GapComponent(0), "West");
        main.add(this.prevPageButton, "West");
        main.add(this.nextPageButton, "East");
        String show = this.kualMenu.getConfig("no_show_status");
        if (null != show && show.equals("true")) {
            this.status = null;
        } else {
            this.status = KualKindlet.getUI().newLabel("Status");
            main.add(this.status, "South");
        }
        main.add(this.breadcrumb, "North");
        GridLayout grid = new GridLayout(this.getPageSize(), 1, gap, gap);
        this.entriesPanel = KualKindlet.getUI().newPanel(grid);
        main.add((Component)this.entriesPanel, "Center");
        this.depth = 0;
        this.updateDisplayedLaunchers(0, true, null);
    }

    private void initializeState() throws IOException, InterruptedException, Exception {
        this.cleanupTemporaryDirectory();
        this.runParser();
    }

    private void runParser() throws IOException, InterruptedException, Exception {
        File parseFile = this.extractParseFile();
        BufferedReader reader = Util.execute(parseFile.getAbsolutePath());
        this.readParser(reader);
        reader.close();
    }

    private void readParser(BufferedReader reader) throws IOException, InterruptedException, Exception {
        this.kualMenu = new KualMenu(reader);
        for (int i = 0; i < 10; ++i) {
            this.keTrail[i] = null;
        }
        this.depth = 0;
    }

    private File extractParseFile() throws IOException, FileNotFoundException {
        InputStream script = ResourceLoader.load(RESOURCE_PARSER_SCRIPT);
        File parseInput = File.createTempFile(EXEC_PREFIX_PARSE, EXEC_EXTENSION_AWK);
        FileOutputStream cmd = new FileOutputStream(parseInput);
        Util.copy(script, cmd);
        return parseInput;
    }

    private void displayErrorMessage(String error) {
        Container root = this.context.getRootContainer();
        root.removeAll();
        Component message = KualKindlet.getUI().newLabel(error);
        message.setFont(new Font(message.getFont().getName(), 1, message.getFont().getSize() + 6));
        root.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = 2;
        gbc.fill |= 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        root.add(message, gbc);
    }

    private void killKnownOffenders(Runtime rtime) {
        String offenders = "matchbox-keyboard kterm skipstone cr3";
        try {
            rtime.exec("/usr/bin/killall " + offenders, null);
            rtime.exec("/usr/bin/killall -9 " + offenders, null);
        }
        catch (Throwable t) {
            new KualLog().append(t.toString());
            this.setStatus("Exception logged.");
        }
    }

    private void cleanupTemporaryDirectory() {
        File tmpDir = new File("/tmp");
        File[] files = tmpDir.listFiles();
        for (int i = 0; i < files.length; ++i) {
            String file;
            if (!files[i].isFile() || !(file = files[i].getName()).startsWith(EXEC_PREFIX_BACKGROUND) && !file.startsWith(EXEC_PREFIX_PARSE)) continue;
            files[i].delete();
        }
    }

    private String getJailbreakError() {
        if (!this.jailbreak.isAvailable()) {
            return "Mobileread Kindlet Kit is not installed";
        }
        if (!this.jailbreak.isEnabled()) {
            return "MKK could not enable Kindlet Jailbreak";
        }
        return null;
    }

    public void actionPerformed(ActionEvent e) {
        Component button = (Component)e.getSource();
        if (button == this.prevPageButton) {
            this.handleLevel(-1, false);
        } else if (button == this.nextPageButton) {
            this.handlePaging(1, this.depth, false);
        } else {
            this.handleLauncherButton(button, this.depth);
        }
        new MailboxProcessor(this.kualMenu, 49, new ReloadMenuFromCache(), 0L, 0L, 0);
    }

    private void handlePaging(int direction, int level, boolean resetFocus) {
        int newOffset = this.offset[level] + this.getPageSize() * direction;
        if (newOffset < 0) {
            newOffset = this.getEntriesCount(level);
            if ((newOffset -= newOffset % this.getPageSize()) == this.getEntriesCount(level)) {
                newOffset -= this.getPageSize();
            }
        } else if (newOffset >= this.getEntriesCount(level)) {
            newOffset = 0;
        }
        if (newOffset == this.offset[level]) {
            return;
        }
        this.offset[level] = newOffset;
        this.updateDisplayedLaunchers(level, false, resetFocus ? null : (-1 == direction ? this.prevPageButton : this.nextPageButton));
    }

    private void handleButtonSelect(int buttonIndex, boolean clickIt) {
        Component[] buttons = this.entriesPanel.getComponents();
        int maxButtons = buttons.length;
        buttonIndex = --buttonIndex > --maxButtons ? maxButtons : (buttonIndex < 0 ? 0 : buttonIndex);
        buttons[buttonIndex].requestFocus();
        if (clickIt) {
            this.handleLauncherButton(buttons[buttonIndex], this.depth);
        }
    }

    private void handleLevel(int direction, boolean resetFocus) {
        int goToOffset;
        int goToLevel;
        if (-1 == direction) {
            goToLevel = this.depth > 0 ? this.depth - 1 : 0;
            goToOffset = this.offset[goToLevel];
        } else {
            KualEntry ke = this.keTrail[this.depth];
            goToLevel = ke.getGoToLevel();
            goToOffset = 0;
        }
        this.depth = goToLevel;
        this.offset[this.depth] = goToOffset;
        this.updateDisplayedLaunchers(this.depth, false, resetFocus ? null : (-1 == direction ? (0 >= this.depth ? null : this.prevPageButton) : null));
    }

    private void updateDisplayedLaunchers(int level, boolean resetViewport, Component focusRequestor) {
        boolean enableButtons;
        if (resetViewport) {
            viewOffset = -1;
            viewLevel = -1;
            for (int i = 0; i < 10; ++i) {
                this.offset[i] = 0;
            }
            this.viewList.clear();
        }
        if (viewLevel != level || viewOffset != this.offset[level]) {
            viewLevel = level;
            viewOffset = this.offset[level];
            this.viewList.clear();
            if (0 == level) {
                this.viewList.addAll(this.kualMenu.getLevel(0).keySet());
            } else {
                KualEntry ke = this.keTrail[level - 1];
                String parentLink = ke.getParentLink();
                Iterator it = this.kualMenu.getLevel(level).entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry)it.next();
                    ke = (KualEntry)entry.getValue();
                    if (!ke.isLinkedUnder(parentLink)) continue;
                    this.viewList.add(entry.getKey());
                }
            }
            this.viewList.add("inject_last_button");
        }
        Iterator it = this.viewList.iterator();
        for (int i = 0; i < viewOffset; ++i) {
            if (!it.hasNext()) continue;
            it.next();
        }
        this.entriesPanel.removeAll();
        int end = viewOffset;
        this.toTopButton.setEnabled(true);
        this.quitButton.setEnabled(true);
        for (int i = this.getPageSize(); i > 0; --i) {
            Component button;
            if (it.hasNext()) {
                KualEntry ke = this.kualMenu.getEntry(level, it.next());
                button = null == ke ? (0 == level ? this.quitButton : this.toTopButton) : KualKindlet.getUI().newButton(ke.label, this, this.keyListener, ke);
                if (null == focusRequestor) {
                    focusRequestor = button;
                }
                ++end;
                this.entriesPanel.add(button);
                if (null != ke) continue;
                break;
            }
            button = 0 == level ? this.quitButton : this.toTopButton;
            ++end;
            this.entriesPanel.add(button);
            this.viewList.add("foo_last_button");
            break;
        }
        boolean bl = enableButtons = this.getPageSize() < this.viewList.size();
        if (null != this.status) {
            this.setStatus("Entries " + (viewOffset + 1) + " - " + end + " of " + this.viewList.size() + " " + "\u25aa" + " " + this.kualMenu.getVersion() + " " + "\u25aa" + " " + this.kualMenu.getModel());
        }
        this.setBreadcrumb(null == this.status && enableButtons ? viewOffset + 1 + "-" + end + "/" + this.viewList.size() : null, null);
        this.prevPageButton.setEnabled(level > 0);
        this.nextPageButton.setEnabled(enableButtons);
        this.entriesPanel.invalidate();
        this.entriesPanel.repaint();
        this.context.getRootContainer().invalidate();
        this.context.getRootContainer().repaint();
        if (null != focusRequestor) {
            focusRequestor.requestFocus();
        }
    }

    private int getEntriesCount(int level) {
        return this.viewList.size() > 0 ? this.viewList.size() : this.kualMenu.getLevel(level).size();
    }

    private int getPageSize() {
        if (0 < onStartPageSize) {
            return onStartPageSize;
        }
        onStartPageSize = 0;
        String size = this.kualMenu.getConfig("page_size");
        if (null != size) {
            try {
                onStartPageSize = Integer.parseInt(size);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        if (0 == onStartPageSize) {
            onStartPageSize = KualKindlet.getUI().getDefaultPageSize();
        }
        return onStartPageSize;
    }

    private int getTrailMaxWidth() {
        return 60;
    }

    private void handleLauncherButton(Component button, int level) {
        boolean internalStatus = false;
        KualEntry ke = KualKindlet.getUI().getKualEntry(button);
        if (ke.isSubmenu) {
            this.keTrail[level] = ke;
            try {
                this.handleLevel(1, false);
            }
            catch (Throwable t) {
                new KualLog().append(t.toString());
                this.setStatus("Exception logged.");
            }
        } else {
            if (ke.isInternalAction) {
                switch (ke.internalAction) {
                    case 0: 
                    case 65: {
                        this.setBreadcrumb(ke.internalArgs + " | ", null);
                        break;
                    }
                    case 66: {
                        this.setStatus(ke.internalArgs);
                        internalStatus = true;
                        break;
                    }
                    case 1: {
                        this.depth = 0;
                        this.handleLevel(-1, false);
                        break;
                    }
                }
            }
            if (null != ke.action) {
                this.killKnownOffenders(Runtime.getRuntime());
                if (!ke.hasOption('s')) {
                    this.setStatus(ke.action);
                }
                try {
                    int beforeAction = 0;
                    if (0 < beforeAction) {
                        Thread.sleep(beforeAction);
                    }
                    if (!ke.hasOption('e')) {
                        this.commandToRunOnExit = ke.action;
                        this.dirToChangeToOnExit = ke.dir;
                        KualKindlet.getUI().suicide(this.context);
                    } else {
                        this.execute(ke.action, ke.dir, true);
                        int afterAction = 0;
                        if (0 < afterAction) {
                            Thread.sleep(afterAction);
                        }
                    }
                }
                catch (Throwable t) {
                    new KualLog().append(t.toString());
                    this.setStatus("Exception logged.");
                }
            }
        }
        if (ke.hasOption('c')) {
            ke.setChecked(true);
            KualKindlet.getUI().setText(button, ke.label);
        }
        if (ke.hasOption('r')) {
            long afterParser = 750L;
            String model = this.kualMenu.getModel();
            // C-2 (OPTIONAL, device-test gated): include Kindle4 (parse.awk maps both
            // K4 0E and black K4B/5th-gen 23 to "Kindle4").
            if ("K2".equals(model) || "DX".equals(model) || "DXG".equals(model) || "K3".equals(model) || "Kindle4".equals(model)) {
                afterParser += 750L;
            }
            if (internalStatus) {
                this.refreshMenu(250L, afterParser, null);
            } else {
                this.refreshMenu(250L, afterParser, ke.label);
            }
        }
        if (ke.hasOption('d')) {
            Date now = new Date();
            this.setStatus(now.toString());
        }
        if (ke.hasOption('h')) {
            // empty if block
        }
    }

    private Process execute(String cmd, String dir, boolean background) throws IOException, InterruptedException {
        File workingDir = new File(dir);
        if (!workingDir.isDirectory()) {
            new KualLog().append("directory '" + dir + "' not found");
            return null;
        }
        File launcher = this.createLauncherScript(cmd, background, "");
        if ("$".equals(PRIVILEGE_HINT_PREFIX)) {
            return Runtime.getRuntime().exec(new String[]{"/var/local/mkk/su", "-s", "/bin/ash", "-c", launcher.getAbsolutePath()}, null, workingDir);
        }
        return Runtime.getRuntime().exec(new String[]{"/bin/sh", launcher.getAbsolutePath()}, null, workingDir);
    }

    protected void onStop() {
        if (this.commandToRunOnExit != null) {
            try {
                this.execute(this.commandToRunOnExit, this.dirToChangeToOnExit, true);
                Thread.sleep(175L);
            }
            catch (Exception exception) {
                // empty catch block
            }
            this.dirToChangeToOnExit = null;
            this.commandToRunOnExit = null;
        }
        super.onStop();
    }

    public void onDestroy() {
        try {
            Thread.sleep(175L);
            this.cleanupTemporaryDirectory();
        }
        catch (Exception exception) {
            // empty catch block
        }
        super.onDestroy();
    }

    private File createLauncherScript(String cmd, boolean background, String init) throws IOException {
        File tempFile = File.createTempFile(EXEC_PREFIX_BACKGROUND, EXEC_EXTENSION_SH);
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
        bw.write("#!/bin/ash");
        bw.newLine();
        bw.write("{ " + init + cmd + " ; } 2>>/var/tmp/KUAL.log" + (background ? " &" : ""));
        bw.newLine();
        bw.close();
        Runtime.getRuntime().exec("chmod +x " + tempFile.getAbsolutePath(), null);
        return tempFile;
    }

    public void refreshMenu(final long beforeParser, final long afterParser, String requestor) throws RuntimeException {
        this.setBreadcrumb("Refreshing the menu \u25cf Please wait...", "");
        if (null != requestor) {
            this.setStatus(requestor);
        }
        final TimerAdapter tmi = TimerAdapter.INSTANCE;
        final Object timer = tmi.newTimer();
        Runnable runnable = new Runnable(){

            public void run() {
                try {
                    Thread.sleep(beforeParser > 0L ? beforeParser : 250L);
                    KualKindlet.this.runParser();
                    new MailboxProcessor(KualKindlet.this.kualMenu, 49, new ReloadMenuFromCache(), afterParser, 0L, 0);
                    KualKindlet.this.cleanupTemporaryDirectory();
                    KualKindlet.this.initializeUI();
                    new MailboxProcessor(KualKindlet.this.kualMenu, 49, new ReloadMenuFromCache(), 0L, 250L, 20);
                }
                catch (Throwable t) {
                    new KualLog().append(t.toString());
                    KualKindlet.this.setStatus("Exception logged.");
                    throw new RuntimeException(t);
                }
                String text = "Menu refreshed.";
                KualKindlet.this.setStatus(text);
                KualKindlet.this.setBreadcrumb(text, null);
                tmi.cancel(timer);
            }
        };
        Object task = tmi.newTimerTask(runnable);
        tmi.schedule(timer, task, 0L, 250L);
    }

    public class ReloadMenuFromCache
    implements MailboxCommand {
        public void execute(Object data) {
            KualKindlet.this.setBreadcrumb("Loading new menu \u25cf Please wait...", "");
            KualKindlet.this.setStatus("Loading...");
            try {
                KualKindlet.this.readParser((BufferedReader)data);
                KualKindlet.this.updateDisplayedLaunchers(KualKindlet.this.depth = 0, true, null);
                KualKindlet.this.setStatus("New menu loaded. Please go to top.");
            }
            catch (Throwable t) {
                new KualLog().append(t.toString());
                KualKindlet.this.setStatus("Exception logged.");
            }
        }
    }
}


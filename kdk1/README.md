# KDK-1.0 build source (official v2.7 decompiled + audit patches)

来源与正当性：`src/` 是官方 `KUAL-KDK-1.0.azw2` (v2.7, SHA-256 70c301f2…) 经 CFR 0.152
的忠实反编译（26→23 个 com 包源文件；jailbreak 7 类直接用 `lib/jailbreak.jar` 真类）。
仅用于自有设备的互操作修复，不用于上游贡献。

内容：
- `src/`：官方 v2.7 反编译源码 + 3 处审计补丁
  - C-3a KualFonts: per-string glyph offset 缓存（上限 256 + 防御性副本）
  - C-3b KualPainter: 同字体 run 合并绘制（像素等价）
  - C-2  KualKindlet: "Kindle4" 加入 +750ms 慢 CPU 刷新缓冲（parse.awk:1095 把
    usid 0E 与 23 都映射为 "Kindle4"）
  - 反编译修复：KualKindlet.java:446 补 (Map.Entry) cast
- `stub-src/`：**仅为编译期**的 Amazon API stub（官方 37 类的完整调用面反推，
  共 9 类；绝不打进产物——assemble 时只取 class 目录）
- `resources/parse.awk`：**发行版原文**（v2.7.37-gfcb45b5, 20250419）——不再重新
  strip/注入版本，保证与官方解析引擎逐字一致

重要背景：NiLuJe/KUAL_Booklet master 的 `src/` 是 **Booklet 化**的 KUAL
（UIAdapter/KUIAdapter/KualKindlet 用 BookletContext、KTimerAdapter 用
java.util.Timer、ui/v1 无 kindlet.ui.KPanel 引用），**不能用于 KDK-1.0 构建**——
在 K4 的 Kindlet 框架上生命周期签名不匹配，报 "An error occurred while trying
to open this item"。本目录即为此而生（引用面已与官方 37 类逐一对齐）。

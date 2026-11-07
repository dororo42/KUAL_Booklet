# KUAL-KDK-1.0 审查报告 v1 核实结论（2026-11-06）

对 `reports/KUAL-KDK-1.0_独立审查报告_v1.md` 的独立复核。方法：重新下载原件 → 哈希比对 → 解包 → 字节码版本扫描 → CFR 0.152 反编译 → 逐条对照报告引用的代码位置与参数。

## 一、逐项核实结果

| 报告论断 | 核实结果 | 证据位置（本次复核产物） |
| --- | --- | --- |
| URL 路径（Legacy/post-jailbreak/installing-kual-mrpi/） | ✅ 一致 | 下载成功（直连） |
| SHA-256 `70c301f2…e140` / MD5 `0c7eb793…cabd` / 131,069 B | ✅ 逐字一致 | `sha256sum`/`md5sum` 输出 |
| ZIP 58 条目、CRC OK、解压 204,490 B | ✅ 一致 | `unzip -l` |
| MANIFEST 全部属性（v2.7 / Temurin 1.8.0_442 / Ant 1.10.15 / Toolbar-Mode persistent / KDK 1.0） | ✅ 逐项一致 | `META-INF/MANIFEST.MF` |
| 三套签名 DNTEST/DITEST/DKTEST | ✅ 一致 | `META-INF/` |
| parse.awk 33,152 B / 1276 行 / `v2.7.37-gfcb45b5 (20250419)` | ✅ 逐字一致 | `extracted/…/resources/parse.awk:5` |
| 37 个 class 全部 major 48（Java 1.4） | ✅ 一致 | 字节码扫描（本目录 `bytecode-scan.txt`） |
| CFR 0.152 反编译 26 个 .java 全成功 | ✅ 一致 | `decompiled/` |
| **Z-1** Util.execute 探测 `/mnt/us/extensions/gawk/bin/gawk` → `gawk -O --non-decimal-data` | ✅ 一致 | `Util.java:27-28` |
| 执行模型 `{ cd <dir> ; <cmd> ; } 2>>/var/tmp/KUAL.log &` | ✅ 一致 | `KualKindlet.java:655` |
| **S-3** killKnownOffenders = `matchbox-keyboard kterm skipstone cr3` | ✅ 一致 | `KualKindlet.java:339-342` |
| **C-1** refreshMenu 全量重做（runParser → cleanup → initializeUI，`root.removeAll()`），无增量路径 | ✅ 一致 | `KualKindlet.java:662-694, 250-254` |
| **C-3** 含非 ASCII 时逐字符 `setFont`+`drawChars`；offsets 每次重算无缓存 | ✅ 一致 | `KualPainter.java:44-51`、`KualFonts.java`（calculateOffsets 无缓存） |
| KUIAdapter 每页 5 项（JUIAdapter 10 项） | ✅ 一致 | `KUIAdapter.java:81-83` / `JUIAdapter.java:74-76` |
| MailboxProcessor 500ms × 20 次轮询 | ✅ 一致 | `KualKindlet.java:207`（period=500, countdown=20） |
| /proc/usid → `0E|23` → "Kindle4" | ✅ 一致 | `parse.awk:1085,1095` |
| md5 挡板缓存（busybox md5sum + KUAL.cache） | ✅ 一致 | `parse.awk:99,164` |
| suicide() → keycode 61441 | ✅ 一致 | `KUIAdapter.java:76` |
| KDK-2.0.azw2 站点 404 | ✅ 一致 | HTTP 404 实测 |
| 官方设备表 "K4 and older → KUAL-KDK-1.0.azw2 / K5 and newer → PEKI" | ✅ 一致 | 页面正文实测（4×"K4 and older", 5×azw2 文件名） |

## 二、与报告的细微出入（不影响结论，供报告 v2 修正）

1. **C-2 表述过强**：`handleLauncherButton` 中 `afterParser = 750L` 是**所有机型共享的基线**，K2/DX/DXG/K3 再 `+= 750L`（共 1500ms，`KualKindlet.java:591-595`）。因此 K4 并非"未享受 afterParser 缓冲"，而是**享受 750ms 基线、未享受 +750ms 追加**。报告引用的代码片段准确，但小节标题"K4 未享受 afterParser 缓冲"有夸大。
2. **MailboxProcessor 存在 1000ms 初始延迟**：`new MailboxProcessor(…, 1000L, 500L, 20)`——报告只提"500ms × 20"，漏了首轮等待 1s（对体感分析影响小，如实记录）。
3. **killKnownOffenders 是两段式**：先 `killall`，再 `killall -9`（`KualKindlet.java:341-342`），报告只提 killall。

## 三、核实产物清单（本目录）

| 产物 | 说明 |
| --- | --- |
| `KUAL-KDK-1.0.azw2` | 重新下载原件（哈希与报告一致） |
| `extracted/` | 解包全量（parse.awk 原文） |
| `decompiled/` | CFR 0.152 反编译 26 个 .java |
| `bytecode-scan.txt` | 37 class × major 48 扫描记录 |
| `page.html` | kindlemodding.org Legacy 安装页快照 |

**总体结论：报告 v1 的全部关键论断经独立复核成立**，三处细微出入已在上方列明。

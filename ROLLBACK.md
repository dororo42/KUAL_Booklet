# 真机实测回滚与防变砖分析（全部经反编译源/parse.awk 核实，2026-11-06）

**结论先行：本轮全部优化项（Z-1 gawk、Z-2 KUAL.cfg）都只写入 `/mnt/us`（用户数据区）与 `/var/tmp`（临时区），不触碰 rootfs/内核/引导分区。最坏情况 = KUAL 这个 Kindlet 显示错误框或空菜单，删除对应文件即完整回滚，不存在变砖路径。**

## 一、为什么不可能变砖（故障传播链核实）

1. **改动面**：Z-1 新增 `/mnt/us/extensions/gawk/` 目录；Z-2 覆盖 `/mnt/us/extensions/KUAL.cfg`。两者都是 KUAL 运行时**读取**的用户区文件，Kindlet 沙箱内的 Java 无任何系统分区写路径（审查报告 S-5 已核实 awk 侧 `rm` 亦限于 `/var/tmp/KUAL.*` 自有文件）。
2. **失败兜底**：`KualKindlet.longStart()`（KualKindlet.java:203-212）把 `initializeState()`（含阻塞解析）的任何 `Throwable` 包成 `RuntimeException` 抛给 Kindlet 框架 → Kindle 固件显示标准 Kindlet 错误框/回到书架。进程级隔离，设备不重启不受损。
3. **gawk 挂掉的两种形态**：
   - exec 失败（二进制损坏/架构不符）→ `Util.execute` 抛 IOException → 同上错误框；
   - 运行中产出空/坏输出 → `readParser→new KualMenu(reader)` 抛解析异常 → 同上。
   两种都只影响当次 KUAL 会话。

## 二、逐项回滚程序（实证依据）

### Z-1（gawk）回滚

```
USB 连接 → 删除 /mnt/us/extensions/gawk/ 整个目录 → 重开 KUAL
```

- `Util.java:27` 的探测是 `new File("/mnt/us/extensions/gawk/bin/gawk").exists()`——目录一删，探测即否，**下一次启动自动回到 `/usr/bin/awk`（busybox）路径**，无需其他操作。
- 缓存自愈：`parse.awk:193-203 cache_send()` 先校验缓存头两行的版本串与 md5，不匹配即 `cache_file_delete()` 并全量重解析——**旧缓存不会污染新状态**。确定性兜底：`rm -f /var/tmp/KUAL.*`（或重启清空）。

### Z-2（KUAL.cfg）回滚

```
USB 连接 → 删除 /mnt/us/extensions/KUAL.cfg → 重开 KUAL
```

- KUAL 首次运行会自动重建缺省模板（`parse.awk` `config_full_path("create")`）。
- 畸形内容安全：`config_read`（parse.awk:251-265）只接受匹配 `^\s*KUAL_\w+=` 的行，其余行**静默忽略**——手改 cfg 不会把解析器弄挂；拼错的键只是不生效。

### 两台设备（K4 银 + 黑色 K4B）：回滚流程完全相同

用户两台设备（K4 `0E` / 黑色 Kindle 5th Gen `23`）同为 FW 4.1.4 + KDK 1.0，同用一份 azw2（见 `docs/DEVICE_CLARIFICATION.md`）——上文的 gawk/cfg/azw2 回滚在两台上一字不差地适用，同一套删除/重开操作。真机逐项判定与回滚点见 `docs/REAL_DEVICE_TEST.md`。

（此前误归类的 "K5/PEKI 链" 属 FW 5.x+ 世代，与这两台无关；如需参考其 documents/ 用户区文件的删除回滚，逻辑同构。）

### C-2/C-3（改码级）回滚

重编译版 azw2 属于"换文件"：把 `/documents/` 里的 azw2 换回官方原件（本仓 `kual-audit/KUAL-KDK-1.0.azw2`，SHA-256 `70c301f2…`）即可，与 KUAL 自身安装/卸载同级，无系统影响。

## 三、实测安全清单

| 动作 | 风险 | 兜底 |
| --- | --- | --- |
| 拷入 gawk 目录 | 无（纯新增文件） | 删目录 |
| 覆盖 KUAL.cfg | 无（自动重建） | 删文件 |
| KUAL 内执行刷新/动作 | KUAL 会话级错误 | 退出重开 |
| 换装重编译 azw2 | MKK 信任链要求钥匙一致 | 见 patches/README.md 的钥匙指纹核验；换回官方 azw2 |
| 任何情况 | — | **K4 有官方恢复镜像刷写通道（Amazon diags/恢复模式），即便 rootfs 意外损坏也可线刷恢复——但本轮改动根本不触及 rootfs，此为最后一道与本题无关的保险** |

## 四、社区权威背书（KOReader 官方仓库）

- **NiLuJe（KUAL 现役维护者）** 在 [KOReader #12692](https://github.com/koreader/koreader/issues/12692) 明确："**The rootfs is RO on Kindles, so you can't [brick it]**"——K4NT 出现 "needs repair" 是老设备软锁，非软件改砖；社区常规处置为长按电源重启 / 充电器静置。
- KUAL 白屏类故障（[#13602](https://github.com/koreader/koreader/issues/13602)、[#14204](https://github.com/koreader/koreader/issues/14204)）的标准处置是重启、重跑 hotfix、换回 KUAL 文件——与本仓"删新增文件即回滚"同构。
- 详见 `docs/KOREADER_RESEARCH.md`、`docs/INSTALL_NEW_BUILD.md`。

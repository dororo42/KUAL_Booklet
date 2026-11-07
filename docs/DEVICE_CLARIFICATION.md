# 设备澄清与 K4/K5(Black) 同构核实（2026-11-06 四轮）

## 用户口径（以此为准）

> "K4 和 K5，都通过 K4 的原版 KUAL（KDK-1.0 azw2）使用"；"kindle5 不是 Kindle Touch，是黑色的 kindle5，是 kindle4 的更新版"。

即：用户两台设备是 **Kindle 4 (2011, 银, `0E` / Tequila)** 与 **Kindle 5 / Kindle (5th Gen, 2012, 黑, `23` / Sauza)**——社区常称 K4 / **K4B（黑）**，二者都跑 **FW 4.1.4**、都**非触摸**、都属 **Yoshi 平台**。此前台账把 "K5" 误归类为 "Kindle Touch / PEKI 链"，**已作废**。

## 官方机型库实证（kindlemodding 仓库 `static/kindles.json`）

| 世代 | amazon_name | nicknames | platform/board | 最新 FW | usid 码 (KindleTool) |
| --- | --- | --- | --- | --- | --- |
| Kindle (4th Gen, 2011) | Kindle (4th Generation) | **K4**, K4S | Yoshi / Tequila | **4.1.4** | `0E` = Kindle4NonTouch |
| **Kindle (5th Gen, 2012, 黑)** | Kindle (5th Generation) | **K4, K4B**（黑） | Yoshi / **Sauza** | **4.1.4** | `23` = **Kindle4NonTouchBlack** |
| (被排除的) Kindle Touch (2011) | Kindle Touch (4th Generation) | KT, **K5** | Yoshi / Whitney | 5.3.7.3 | `0F|10|11|12` = Kindle5Touch* |

注意：官方库里 "K5" 这个**绰号**属于 Kindle Touch（FW 5.x），与用户所说的黑色 K5 不是同一台；MobileRead KUAL 发布帖里 "K5, PW & PW2 (5.0–5.4.2.1)" 指的也是 FW 5.x 世代，**不适用于用户的两台 FW 4.1.4 设备**。这解释了为何 kindlemodding 安装页 "K5 and newer → PEKI" 与用户实证不矛盾：那条路由针对的是 FW 5.x+ 世代。

## parse.awk 与 KDK 运行时实证

- `parse.awk:1095`：`device ~ /^(0E)|(23)$/ ? "Kindle4"` —— **两台 usid 码都被识别为 "Kindle4"**。C-2 补丁里 `|| "K4".equals(model)` 对两台**同效**（注意：代码里模型名是 `"Kindle4"` 而非 `"K4"`——见下方待修项）。
- `UIAdapter.createInstance()` 用 `Class.forName("com.amazon.kindle.kindlet.ui.KPanel")` 探测：两台均为 KDK 1.0 运行时（FW 4.1.4）→ 都走 **KUIAdapter（每页 5 项）+ KTimerAdapter**，UI 路径完全相同。
- 官方 `KUAL-KDK-1.0.azw2` 与本仓 CI 产物 `KUAL-KDK-1.0-custom.azw2` 的 manifest 均为 `SDK-Specification-Version: 1.0`，`Main-Class: KualKindlet` —— 同一个文件、同一条加载链。

## 结论

**K5（黑色 K4B / `23`）完全可以使用与 K4 相同的安装、回测、回滚流程，同一份 azw2 产物、同一套 gawk 扩展、同一份 KUAL.cfg，无需任何区分。** 真机回测按 `docs/REAL_DEVICE_TEST.md` 的两机同流程执行。

## ⚠️ 由本澄清引出的一个代码待修项（重要）

C-2 补丁写的是 `|| "K4".equals(model)`，而 `parse.awk` 的 `getModel()` 对 `0E|23` 返回的是 **`"Kindle4"`**，不是 `"K4"`。也就是说 **C-2 补丁在任何一台设备上都不会生效**（模型名不匹配）——这反而印证了报告核实时的注记"K4 未入列表可能因其 JVM 较快本就不需要"。处理选项：

1. 保持现状（C-2 本就是"可选、真机实测门控"），并在 fork 里把该提交 revert 掉，避免误导；
2. 或修正为 `"Kindle4".equals(model)` 让补丁真正在两台设备上生效，再真机实测决定是否保留。

**建议**：先按 `docs/REAL_DEVICE_TEST.md` 测出刷新是否真的丢帧，再决定——若两台 FW 4.1.4 实测都不丢帧，直接 revert C-2 提交（选项 1），fork 只保留 C-3 与审查修复，最干净。

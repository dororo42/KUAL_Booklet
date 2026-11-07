# KUAL-KDK-1.0 核实与 K4/K5 优化工作区

依据 `~/reports/KUAL-KDK-1.0_独立审查报告_v1.md`（2026-11-05）执行：①独立核实报告全部论断；②落地报告 §7 建议路线中"立刻可做"的 Z-1/Z-2 与可交付的改码级补丁。**适配范围：Kindle 4（K4NT/D01100）+ Kindle 5（D01200）双机型**。工作方法遵循 ub-mode（先能用→再正确→守惯例），全程日志见 `LOG.md`。

## 双机型适用口径（2026-11-06 四轮澄清后定稿）

> ⚠️ **此前"K5→PEKI"的归类已作废**。用户明确：两台设备（K4 银 + 黑色 Kindle 5 = Kindle 4th/5th Gen 黑、K4B）**都通过同一份 K4 原版 KUAL-KDK-1.0.azw2 使用**。官方机型库实证：K4=`0E`/Tequila/FW 4.1.4，黑色 K5(5th Gen)=`23`/Sauza/FW 4.1.4，**同属 Yoshi 平台、同为 KDK 1.0、非触摸**；parse.awk:1095 把 `0E|23` 都识别为 `"Kindle4"`，运行时同走 KUIAdapter（每页 5 项）。此前误把 "K5" 当成 Kindle Touch（FW 5.x，官方绰号也叫 K5）进而路由到 PEKI——那是另一台设备。
> **结论：Z-1/Z-2/改码版 azw2/回滚流程对用户的两台设备完全同构、同一产物、无需区分**。详见 `docs/DEVICE_CLARIFICATION.md`；真机回测按 `docs/REAL_DEVICE_TEST.md` 两机各跑一遍。

| 设备 | usid / 板型 / FW | KUAL 链 | 本轮优化 |
| --- | --- | --- | --- |
| K4 (2011 银) | `0E` / Tequila / 4.1.4 | `KUAL-KDK-1.0.azw2`（KDK 1.0） | ✅ 同一套 |
| 黑色 K5 = Kindle 5th Gen (2012, K4B) | `23` / Sauza / 4.1.4 | **同一份** `KUAL-KDK-1.0.azw2`（KDK 1.0） | ✅ 同一套 |

（kindlemodding 安装页 "K5 and newer → PEKI" 针对 FW 5.x+ 世代，与用户两台 FW 4.1.4 设备无关；PEKI 留档 `kual-audit/peki/` 仅作反例参考。）

## 交付物索引

| 目录 | 内容 | 对应报告项 |
| --- | --- | --- |
| `kual-audit/` | 核实证据链：azw2 原件（哈希比对一致）、解包、CFR 反编译 26 .java、字节码扫描、安装页快照、PEKI 留档（反例）、**核实结论 VERIFICATION.md** | 报告全文复核 |
| `gawk-extension/` | **Z-1**：静态 gawk 5.3.2（ARMv7-A/VFPv3，1.07MB，K4+K5 通用）+ 双机型安装/验证说明；qemu-arm 下跑通原版 parse.awk 全流程 | §4.1 Z-1 / §5.② 前置 |
| `kual-cfg/KUAL.cfg` | **Z-2**：K4/K5 调优配置（键名/取值均按反编译源核实），覆盖安装到 `/mnt/us/extensions/KUAL.cfg` | §4.1 Z-2 |
| `patches/` | **C-2/C-3 补丁套件**（**基线=NiLuJe master 真源码**）：统一 diff + 桩编译验证 + PR 路线结论（NiLuJe 关 GitHub 协作流 → MobileRead 正道 / KOL fork 可 PR）；C-1 明确不动 | §4.2 C-1/C-2/C-3 |
| `ROLLBACK.md` | **真机回滚与防变砖分析**（故障传播链逐级核实，全改动仅用户区/临时区，无变砖路径） | — |
| `docs/PROJECTS.md` | GitHub/Reddit 研究项目清单 + **KUAL_Booklet fork 谱系与 PR 路线** | — |
| `docs/CODE_REVIEW.md` | **code-review-skill + eagle-eye 双技能审查报告**（3 项发现已修、验证记录、提交拓扑） | — |
| `docs/KOREADER_RESEARCH.md` | **KOReader 官方 issue/PR 中 K4/K5/KUAL 研究**（239 命中筛选、#15919/#12692/#3018/#13064/#13602 等） | — |
| `docs/INSTALL_NEW_BUILD.md` | **已装原版 KUAL → 安装新构建**（CI 产物、并存安装、验证、30 秒回滚、K5 差别） | — |
| `ci-artifact/` | **当前待测** CI 产物 `KUAL-KDK-1.0-custom.azw2`（SHA-256 `614a31e02ba4b0771ce601535a83400e7708320ec9b703ba7955dbced2af8842`，含 C-3 + 已修正 C-2；major 48×30 / 三签名 / parse.awk 已实测） | — |
| `docs/DEVICE_CLARIFICATION.md` | **设备澄清**：黑色 K5 = K4B(`23`)/Yoshi/FW4.1.4，与 K4 同构；C-2 模型名 bug 记录 | — |
| `docs/REAL_DEVICE_TEST.md` | **两机同流程真机回测方案**（基线→Z-1/Z-2→并存装新构建→6 项判定→回滚点→差分表） | — |
| `upstream-kual/` | **NiLuJe master 留档（= v2.7.37 发行版源码，含指纹一致的 developer.keystore 与 1.4 构建链）** | 补丁重建与 PR 基线 |
| `upstream-denswitch/` | DenSwitch 三把 MR 开发者 p12（指纹=官方 v2.7，等价备用）+ 签名工具脚本 | 重签名备用材料 |

## 核实结论（TL;DR）

报告 v1 **全部关键论断经独立复核成立**：哈希/结构/字节码版本/parse.awk 指纹/MANIFEST 逐字一致；代码级论断（Z-1 gawk 分支、执行模型、C-1 全量重建、C-3 逐字符绘制、S-3 kill 名单、500ms×20 轮询、K4=0E/23、KDK-2.0 404、官方设备表）全部在反编译源中复现。三处细微出入（不影响结论）：

1. C-2 表述过强：K4 享有 750ms afterParser 基线，只是没有 K2/K3 的 +750ms 追加；
2. MailboxProcessor 首轮有 1000ms 初始延迟（报告只提 500ms×20）；
3. killKnownOffenders 是 killall + killall -9 两段式。

详见 `kual-audit/VERIFICATION.md`（含逐项证据表）。

## 真机侧下一步（两台设备各跑一遍，流程相同）

> **四轮更新**：fork `dororo42/KUAL_Booklet` master 已到 `3889400`（C-3 + 审查修复 + **C-2 模型名修正** `"K4"`→`"Kindle4"`）；CI 重出产物已验证（`ci-artifact/`，SHA `614a31e0…8842`）。**未提交 PR，遵用户指令**。本机无构建环境 → GitHub Actions workflow 已实装并多次成功。两机按 `docs/REAL_DEVICE_TEST.md` 同一流程执行。

1. 两机各做基线（开原版 KUAL 记录表现）→ 按 `docs/INSTALL_NEW_BUILD.md` 装 gawk 扩展、覆盖 KUAL.cfg，验证 `(20250419,g)` 指纹；
2. 两机各以**不同文件名**并存安装 CI 产物 `KUAL-KDK-1.0-custom.azw2`，先开原版对照、再开新构建，按 REAL_DEVICE_TEST.md 的 B1–B6 逐项判定（重点 B4：刷新 +750ms 是否值得保留，决定 revert C-2 与否）；
3. 任何异常删新文件即 30 秒回滚；K4 与 K4B 结果应完全一致，差异先查 MKK/JB 状态；
4. C-4（awk→Rust）维持挂起：gawk 落地后先实测 awk 是否仍是可感知瓶颈。

## 复现构建

- gawk：`build/gawk-5.3.2/` 内 `./configure --host=arm-kindle5-linux-gnueabi --disable-nls --disable-extensions --without-readline --without-mpfr --without-gmp CFLAGS="-O2 -fno-asynchronous-unwind-tables" LDFLAGS="-static" && make && strip`（工具链 `~/x-tools/arm-kindle5-linux-gnueabi`）
- qemu 行为验证：`qemu-arm ./gawk -O --non-decimal-data -v EXTENSIONDIR=<dir> -v PRODUCTNAME=KUALTEST -f parse.awk`（需 `LC_ALL=C`，详见 gawk-extension/README.md 测试注记）

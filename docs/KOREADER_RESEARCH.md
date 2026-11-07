# KOReader 官方仓库中 Kindle4 / Kindle5 / KUAL 相关 issue/PR 研究（2026-11-06）

检索方式：GitHub API `search/issues`（`repo:koreader/koreader`），按 KUAL / Kindle4 / K4NT / K5 / legacy 组合过滤 + 逐个读正文与评论。**KUAL 关键词命中 239 条**（issue+PR），下列为对本项目（K4/K5、防变砖、KUAL 部署）有直接价值的条目。

## 一、K4NT（Kindle 4 Non-Touch）专属

| # | 状态 | 要点 | 对本项目的意义 |
| --- | --- | --- | --- |
| [#15919](https://github.com/koreader/koreader/issues/15919) | open (2026-08) | K4NT / FW 4.1.4（代号 K4/yoshi/MX50）退出 KOReader 时发送 keycode 139 而非 Home(102)，UI 冻结；评论提到"从自定义签名 Kindlet launcher 启动"是变量，并**直接推荐 [DenSwitch/kindle4-kindlet-launcher](https://github.com/DenSwitch/kindle4-kindlet-launcher)**（与我们重签名所用钥匙同源） | ①印证 K4 键码/启动链细节；②自定义签名 azw2 在社区是常规做法；③启动器与 KOReader 退出路径存在耦合，属真机实测关注点 |
| [#12692](https://github.com/koreader/koreader/issues/12692) | closed (2024-10) | "K4NT dead after KOReader exit" → 出现 "Your Kindle needs repair" 软锁；**NiLuJe 本人结论：rootfs 在 Kindle 上是只读的，软件层面你没法把它弄砖；设备只是老了、遇故障容易软锁**（rootfs is RO on Kindles, so you can't…） | **防变砖结论的权威背书**：我们的改动全在 /mnt/us 用户区，比 KOReader 更不可能触及 rootfs；所谓"变砖"多为老设备软锁，可通过重启/恢复模式解决 |
| [#3018](https://github.com/koreader/koreader/issues/3018) | closed (2017-07) | K4NT 上 KOReader 最后可用版本为 nightly 20170308，之后点菜单崩溃（疑似 swipe-to-open-menu 引入） | K4 软件生态的"世代冻结"事实：K4 固件 4.1.x 与老库绑定，不要在其上假设现代组件 |
| [#9977](https://github.com/koreader/koreader/issues/9977) | open (2022) | K4NT WiFi 打不开 | K4 硬件/驱动层已知问题，与 KUAL 无关但属环境背景 |

## 二、KUAL 机制与故障（跨机型）

| # | 状态 | 要点 | 对本项目的意义 |
| --- | --- | --- | --- |
| [#13064](https://github.com/koreader/koreader/issues/13064) | closed (2025-01) | K2I 上 KOReader 不出现在 KUAL 菜单；**结论：必须把 KOReader 的 `extensions/` 文件夹拷到 /mnt/us 根目录**（目录与已有 extensions 是合并而非替换），KUAL 靠 `config.xml` 扫描发现扩展 | 实证 KUAL 扩展发现机制（与 parse.awk `find … -name config.xml` 一致）；我们的 gawk 目录**故意不放 config.xml** 因此不进菜单，正确 |
| [#13602](https://github.com/koreader/koreader/issues/13602) | closed (2025-04) | 装错 KOReader 包后 KUAL 白屏；排障结论：看 koreader 目录下 crash.log，若 "Cannot access framebuffer" 则是 JB/hotfix 未装好，重启两次并重跑 hotfix | **KUAL 白屏 ≠ 变砖**：属框架/JB 层，重装 hotfix 可恢复，佐证回滚/恢复路径 |
| [#14204](https://github.com/koreader/koreader/issues/14204) | open (2025-08) | "Getting a Blank Screen on KUAL" | 同类症状仍在出现，属 KUAL 生态常见问题，非本补丁引入 |
| [#13566](https://github.com/koreader/koreader/issues/13566) | open (2025-04) | `[ERROR] Syntax koreader/menu.json in KUAL` | menu.json 语法错误被 KUAL 解析器明确报错并进 crash 日志（与我们核实的 parse.awk 错误机制一致） |
| [#12923](https://github.com/koreader/koreader/issues/12923) | closed | "Mobileread Kindlet Kit removed after exiting Kual (Kindle DX)" | MKK 信任链与 KUAL 退出路径的历史耦合——重签名 azw2 依赖 MKK  intact，安装前确认 |
| [#15352](https://github.com/koreader/koreader/pull/15352) (PR) | closed | KUAL 扩展加 SSH server 支持 | KUAL 扩展生态仍在维护的实例 |
| [#15608](https://github.com/koreader/koreader/pull/15608) (PR) | closed | "Stop awesome regardless whether KOReader was launched from KUAL or…" | 启动链（KUAL vs 原生启动器）差异的上游工程处理 |

## 三、K5 / 固件门槛

- **[#11986](https://github.com/koreader/koreader/issues/11986)**（closed）："Add support for kindles after fw 5.16.3" —— 对应我们已知的 kual-next（原生启动器，要求 5.16.3+ hard-float）。**K4/K5（FW 4.1.x / 5.x 早期）不在此列**，K4/K5 世代的原生路线仍是 kite/launchpad 式热键守护。
- K5 相关命中多为 Paperwhite 世代（如 #13602 的 PW 5.6.5），**没有发现针对 K5 (D01200) KUAL 优化的专项研究帖**——印证本工作"K5 走 PEKI、与 KDK-1.0 同引擎"的适配结论在社区无异议。

## 四、结论映射

1. **防变砖**：NiLuJe 在 #12692 的 "rootfs 是只读的" 与我们从代码推得的"改动仅 /mnt/us + /var/tmp"完全一致；社区实际"变砖"报告多为老设备软锁，可恢复。
2. **可回滚**：#13602/#14204 表明 KUAL 白屏类故障的常见处置是重启/重跑 hotfix/换回 KUAL 文件——与本仓 `ROLLBACK.md` 的"删新增文件即回滚"同构。
3. **重签名/自定义启动器**：#15919 官方讨论直接推荐 DenSwitch 工具（我们留档的钥匙），说明自定义签名 Kindlet launcher 在 K4 社区是受认可路径；但同时提醒 KOReader 退出键码与启动器耦合，真机需验证。
4. **扩展机制**：#13064 实证 extensions 目录合并与 config.xml 扫描，佐证 gawk 库型扩展不进菜单的设计。

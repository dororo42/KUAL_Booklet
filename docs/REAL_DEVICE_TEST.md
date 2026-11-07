# K4 + K5(黑色 K4B) 真机回测方案（同一流程，两台各跑一遍）

> 前提澄清见 `docs/DEVICE_CLARIFICATION.md`：两台都是 FW 4.1.4 / Yoshi / KDK 1.0 / 非触摸，usid `0E`(K4) 与 `23`(K4B) 都被 parse.awk 识别为 `"Kindle4"`，**同一份 CI 产物、同一套 gawk 扩展、同一份 KUAL.cfg、同一套回滚流程**。
> 当前待测产物（**v4，2026-11-07**）：`ci-artifact/v4/KUAL-KDK-1.0-custom.azw2`（SHA-256 `6fde95bf7f35f0801b5d512eb98265854010ca1a5f317f083ec54ee369828bb7`）——**官方 v2.7 反编译源重建**（kdk1/，修复 master 源码 Booklet 化与 KDK-1.0 不符的第二根因）+ C-3/C-2 补丁；37×major 48、与官方引用面一致（仅 KUIAdapter 常量内联差异）。v2/v3 作废。

## 0. 测试前置（每台）

- 已越狱 + MKK 信任链 intact（原版 KUAL 能正常打开即证明满足）。
- **原版 `KUAL-KDK-1.0.azw2` 保留在 `/mnt/us/documents/`，不删不覆盖**。
- 电脑上备好：CI 产物（校验 sha256）、`gawk-extension/gawk/`、`kual-cfg/KUAL.cfg`。
- 记录表：设备序列前缀（K4=`D01100` / K4B=`D01200`）、FW 版本（应 4.1.4）、原版 KUAL 基线表现。

## 1. 基线（先测原版，两机各一次）

打开原版 KUAL → 记录：菜单项数、翻页是否正常、扩展动作能否执行、含 Unicode 符号（✓▽✯）标签显示、执行菜单 `r` 刷新的耗时与有无闪黑/白屏。USB 拷回 `/var/tmp/KUAL.log` 备查。

## 2. 阶段 A：零改码项（Z-1 gawk + Z-2 cfg）— 两机同做

```
USB → 拷 gawk/ 到 /mnt/us/extensions/gawk/（最终 bin/gawk）
    → 覆盖 /mnt/us/extensions/KUAL.cfg
安全弹出 → 重开 KUAL
```

**判定**：
- `head -2 /var/tmp/KUAL.cache` 第 2 行版本串尾为 `(20250419,g)`（两机相同；`g`=gawk 生效）；
- 状态栏隐藏、ABC 排序生效、每页 5 项（KUIAdapter，两机相同）；
- 菜单打开体感（gawk ≈2× 解析提速）。

## 3. 阶段 B：改码版 azw2（C-3 / C-2）— 两机同做，并存安装

```
USB → 把 KUAL-KDK-1.0-custom.azw2 放 /mnt/us/documents/（与原版不同名，两本并存）
安全弹出 → 重启一次 → 书架出现第二本 KUAL
先点原版（对照）→ 再点新构建
```

**逐项判定（两机各填一份）**：

| # | 观察点 | 期望 | 关联改动 |
| --- | --- | --- | --- |
| B1 | 新构建能打开、菜单齐全 | 无错误框/白屏 | KDK1 加载链 + 三签名 |
| B2 | 含 `✓▽✯` 的标签显示 | 无错位/缺字（像素等价） | C-3 offsets 缓存 + 字体 run 合绘 |
| B3 | 反复翻页/重绘 20 次 | 无累积错位、无 OOM/卡顿 | C-3 缓存上限 256 + 防御性副本（审查修复） |
| B4 | 执行菜单 `r` 刷新 | 无丢帧/白屏；留意是否比原版多约 750ms 等待 | C-2（+750ms，已修正为 `"Kindle4"`） |
| B5 | 扩展动作执行 | 日志追加 `/var/tmp/KUAL.log` 正常 | KDK1 执行模型 |
| B6 | 退出 KUAL 后系统正常 | 回书架/主界面 | Kindlet 生命周期 |

**B4 决策点**：`docs/DEVICE_CLARIFICATION.md` 指出 C-2 原为 `"K4"`（不匹配、从未生效），现已修正为 `"Kindle4"` 使两台都真正生效。若两台实测**刷新本就不丢帧且新等待反而拖慢** → 在 fork 里 revert `332659e`（C-2 独立提交）并让 CI 重出产物，只保留 C-3；若**有可感知改善** → 保留。这是本次真机测试最需要给出的结论。

## 4. 回滚点（任何一项不达标，30 秒）

| 症状 | 动作 |
| --- | --- |
| 新构建白屏/错误框 | 删 `/mnt/us/documents/KUAL-KDK-1.0-custom.azw2` → 重启 → 用原版 |
| 只想去掉 C-2 | fork revert `332659e` → CI 重出产物；或直接换回原版 |
| gawk 异常 | 删 `/mnt/us/extensions/gawk/`（自动回 busybox awk） |
| cfg 异常 | 删 `/mnt/us/extensions/KUAL.cfg`（KUAL 自动重建缺省模板） |
| 缓存异常 | 删 `/var/tmp/KUAL.*`（版本/md5 自校验会重建） |
| 老设备软锁 | 长按电源重启；NiLuJe 在 KOReader #12692 明确 rootfs 只读、软件层面无法变砖 |

**全程不触碰 rootfs/系统分区**；改动仅 `/mnt/us` 用户区 + `/var/tmp` 临时区。

## 5. 两机差分记录表（回填）

| 项目 | K4 (`0E` / D01100) | K4B 黑色 (`23` / D01200) |
| --- | --- | --- |
| FW / 序列前缀 | | |
| 基线菜单项数 / 每页数 | | |
| gawk 指纹 `(20250419,g)` | ☐ | ☐ |
| B1 新构建可开 | ☐ | ☐ |
| B2 Unicode 标签 | ☐ | ☐ |
| B3 反复重绘 | ☐ | ☐ |
| B4 刷新体感（+750ms 评价） | ☐ | ☐ |
| B5 动作执行 | ☐ | ☐ |
| B6 退出正常 | ☐ | ☐ |
| 结论：保留 C-2 / revert | | |

> 预期两机结果**应完全一致**（同平台/同固件/同 UI 路径/同模型名 `"Kindle4"`）。若出现差异，优先查 MKK/JB 状态与扩展目录内容，而非 azw2 本身——这正是两机对照测试的价值。

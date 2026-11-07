# KDK-1.0 改码级补丁套件（C-2 / C-3）— 基于 NiLuJe master 真源码

> **基线更新（2026-11-06 二轮）**：本套件已从 CFR 反编译基线**整体迁移到 `NiLuJe/KUAL_Booklet` master 真源码**（`upstream-kual/`，本地留档）。实证依据：master 提交 `c6ac782 "Resync w/ KUAL 2.7.37" (2025-04-19)` 与发行版 azw2 构建串 `v2.7.37-gfcb45b5 (20250419)` 对应；其 `developer.keystore` 三别名指纹与发行版三证书**逐一比对一致**（ditest=`F4:77:D4:AB…` / dktest=`C7:7E:FA:9B…` / dntest=`3D:6C:62:5B…`，密码见 `build.properties`，为社区公开测试钥匙）。三个补丁位点在 master 上的位置：
> - C-2：`KualKindlet.java:786-790`（原注释 "Add 750ms for legacy devices with slower CPU"）
> - C-3a：`ui/v1/KualFonts.java:89-98`（calculateOffsets 无缓存）
> - C-3b：`ui/v1/KualPainter.java:64-67`（逐字符 setFont+drawChars）

## 补丁内容

| diff | 内容 | 状态 |
| --- | --- | --- |
| `c3-kualfonts-offsets-cache.diff` | `calculateOffsets` 按 `String` 缓存（标签集合有限，无需淘汰） | 桩编译 ✓ |
| `c3-kualpainter-font-run-batching.diff` | 连续同字体字符合并为一次 `setFont`+`drawChars`（像素等价：offsets 为累计宽度） | 桩编译 ✓ |
| `c2-k4-afterparser.diff` | 把 `"K4"` 加入 +750ms 慢 CPU 列表 | **可选**：真机实测刷新丢帧才应用（master 原注释表明该列表本意即"慢设备"） |

`patched-src/` 为已应用补丁的完整文件（tab 缩进，保持 master 风格）。验证：`javac --release 8` + `KUIAdapter` 静态方法桩。

## 重建 azw2（fork 内自足，无需外部材料）

```bash
# upstream-kual/ 即 NiLuJe master：源码 + build.xml(source/target=1.4) + developer.keystore(指纹=官方) 全齐
cd upstream-kual
ant          # 见 build.xml 产物流程；需 JDK 8（javac 1.4 目标）+ Ant + KDK 库（lib/）
# 签名由 build.xml:100-102 自动完成（SHA256withRSA, dk/di/dn 三别名）
```

构建依赖核对：`build.properties`/`build.xml` 的 classpath 指向 `lib/`（仓内自带）与 JDK 1.4 bootclasspath 配置；**JDK 25 无法产出 major 48 字节码，必须 JDK 8**。

## 提交 PR 的可行性结论（2026-11-06 调查）

| 目标仓 | GitHub PR 通道 | 结论 |
| --- | --- | --- |
| **NiLuJe/KUAL_Booklet**（正主，v2.7.37 源） | 仓库 `has_issues=False`，历史 0 PR —— **GitHub 协作流关闭** | ❌ 不走 GitHub PR；**贡献正道 = MobileRead 主帖**（KUAL v2.7 官方支持帖，NiLuJe 以 "Resync" 流程吸收上游） |
| **yparitcher/KUAL_Booklet**（=KOL，只启动 KOReader 的 20 commits 定制 fork） | PR 可用（有先例：NiLuJe 本人 2022 年提过 #1） | ⚠️ 可提但受众窄（KOReader 快启场景）；C-3 对其标签绘制同样受益 |
| **coplate/KUAL_Booklet**（fork 源头） | 2018-02 后停更 | ❌ 不建议 |

**推荐路线**：真机实测确认 C-2/C-3 有体感收益后 → fork `NiLuJe/KUAL_Booklet` → 用本套件 diff（已是 master 基线）建分支 → **首选在 MobileRead 主帖跟帖提交补丁并附 fork 链接**（社区惯例）；若坚持 GitHub PR，唯一现实目标是 yparitcher 的 KOL fork。

**License 注意**：三个仓均无 LICENSE 文件（默认 all rights reserved；真源可再上溯 ixtab 的 bitbucket）。提 PR 本身受 GitHub ToS 授权覆盖，但**自行分发重建 azw2 前需自行评估**；本仓补丁为独立最小改动，不拷贝上游大段代码。

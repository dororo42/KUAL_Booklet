# 行动日志（ub-mode: Keep a Log）

2026-11-06，工作区 `/home/doro/KUAL`。

| # | 动作 | 原因 | 结果/证据 |
| --- | --- | --- | --- |
| 1 | 加载 ub-mode skill | 用户指令 | 三原则约束后续全部动作 |
| 2 | 通读审查报告 v1 | 确定核实范围 | 优化点 Z-1/Z-2/C-1~C-4/S 系列 |
| 3 | 检查 `/home/doro/KUAL`（空）与报告附录 `kual-audit/`（不存在） | 原工作产物不在本机，需重建证据链 | find 全盘确认 |
| 4 | 重新下载 azw2 → 哈希比对 | 核实① | SHA-256/MD5/大小逐字一致 |
| 5 | 解包 → 58 条目/parse.awk(33152B,1276行,构建串)/MANIFEST | 核实② | 全部一致 |
| 6 | 37 class 字节码 major 扫描 | 核实③ | 全部 48（Java 1.4），`bytecode-scan.txt` |
| 7 | 下载 CFR 0.152（Maven Central 直连）反编译 | 核实④ | 26 .java 全成功，与报告一致 |
| 8 | 逐条验证代码级论断 | 核实④ | Z-1/C-1/C-3/S-3/执行模型/轮询/61441/每页5项/usid 0E-23/md5 缓存全部复现；发现 3 处细微出入（afterParser 基线、1000ms 初始延迟、killall -9）→ `VERIFICATION.md` |
| 9 | 下载 KDK-2.0（404）+ 安装页快照 | 核实网络类论断 | 404 ✓；"K4 and older" 设备表 ✓ |
| 10 | **Z-1**：gawk 5.3.2 交叉编译（静态、禁扩展/mpfr/nls，armv7 工具链） | 报告 Z-1 交付物 | 1.07MB stripped，readelf 确认 ARMv7-A/Thumb-2/VFPv3 |
| 11 | qemu-arm 用户态模拟验证 gawk | 不能交付未测的二进制 | --version/核心语言 OK；首轮失败排查：①路径拼接错误（cwd 陷阱）②glibc 2.12.2 静态读宿主 locale-archive 断言 → `LC_ALL=C` 绕过（真机不受影响） |
| 12 | parse.awk 全流程实测 | Z-1 行为验证 | proot 被沙箱 exec 监控破坏 → 改用 `-v EXTENSIONDIR/-v PRODUCTNAME` 注入宿主路径（parse.awk:89,106 仅空值才设缺省），**零改动跑通原版 1276 行**：30 行协议 + cache + cfg，exit=0 |
| 13 | **Z-2**：KUAL.cfg 调优 | 报告 Z-2 | 键名/取值从 KualKindlet.java:257-280,507 逐项核实后写入 |
| 14 | **C-3** 补丁：offsets 缓存 + 字体 run 合并 | 报告 C-3 | 桩编译（javac --release 8 + KUIAdapter 桩）通过；diff ×2 |
| 15 | **C-2** 补丁：K4 加入 +750ms 列表 | 报告 C-2 | 一行 diff；README 标注"先实测再决定"（方向性风险）与核实注记 |
| 16 | C-1 决策：不动 | 架构级改动无真机收益证据；Z-1 已部分消解 | patches/README 记录立场 |
| 17 | git init + 分步提交 | ub-mode: Commit and Push | 见 git log |
| 18 | **回滚/防变砖核实**（用户追加指令） | 确认真机实测安全 | 故障传播链逐级核实：改动仅 /mnt/us + /var/tmp；longStart 异常→Kindlet 框架错误框；缓存版本/md5 自愈；cfg 畸形行被 `^\s*KUAL_\w+=` 过滤 → `ROLLBACK.md` |
| 19 | **K4+K5 双机型适配核实**（用户追加指令） | 用户要求"必须适配 kindle4 和 kindle5" | 官方安装页：K5→PEKI；v2.7 发布帖支持表无 K5 → **同一 azw2 不官方覆盖 K5**；但 PEKI KUAL.jar 与 KDK-1.0 同引擎（gawk 探测串/EXTENSIONDIR:90/CONFIGFILE:92 实证）→ Z-1/Z-2 两链同效；gawk 静态二进制 ARMv7-A 两机型通用 |
| 20 | GitHub/Reddit/MobileRead 检索（用户追加指令） | 找相关研究项目 | KUAL 真源码仓（coplate，已克隆）、PEKI（已留档）、DenSwitch 三把 p12 **指纹与官方 v2.7 证书逐一比对一致**→重签名阻断解除、kual-next（5.16.3+，K4/K5 不适用）、kite-kindle4nt/launchpad 先例 → `docs/PROJECTS.md` |
| 21 | orbital handoff（用户追加指令） | 跨 agent 交接 | `handoffs/` 三档全套，本地 only 不入库 |
| 22 | **KUAL_Booklet fork 谱系与 PR 可行性调查**（用户指令） | 用户问"能否以 KUAL_Booklet 的 fork 提 PR" | GitHub API 实证谱系：coplate(origin,2018停更)←NiLuJe(正主,2025-04 resync=v2.7.37本源,has_issues=False/0PR)←yparitcher(+20 commits=KOL,PR有先例)；结论：**机制可行但 NiLuJe 关 GitHub 协作流 → 正道 MobileRead，KOL fork 是唯一 PR 现实目标** |
| 23 | 补丁基线升级：coplate staging(2018) → NiLuJe master | master=发行版源码本尊，旧基线过时 | tarball 留档 `upstream-kual/`；三补丁位点在 master 重定位(KualKindlet:786/KualFonts:89/KualPainter:64)并重生成 diff；桩编译 ✓；**master 自带 developer.keystore 指纹=官方三证书(重大发现,fork 即受信构建链)** |
| 24 | fork `dororo42/KUAL_Booklet`（用户已建）→ 特性分支 2 提交 + --no-ff 合入 master + push | 用户要求 git/push/合并 | 远程 master 6619bcc；C-3/C-2 分提交便于单独 revert；无 PR 提交 |
| 25 | **code-review-skill + eagle-eye 双技能审查** | 用户指令 | 3 项发现：缓存可变数组泄漏(已修:防御性副本)、缓存无界(已修:上限256+清空)、private isUnicode 误用(编译拦截并回退)；边界行为测试+桩编译+diff check；审查修复提交 770cd82 已 push；报告 `docs/CODE_REVIEW.md` |
| 26 | **GH Actions CI 实装**（用户问本机无环境能否用 CI） | 本机缺 JDK8/Ant/Kindlet SDK jar，且 Amazon jar 版权不可分发 | workflow `build-kdk1.yml`：JDK8+ant+**6 个 Amazon API 编译期 stub**(不进产物)+stripit.sh 生成 parse.awk+三签名；本地端到端模拟闭环后提交 bbee562 并 push；**首次运行 42143295293 success**，产物下载实证：30×major48、jarsigner jar verified、parse.awk 33143B；留档 `ci-artifact/` |
| 27 | KOReader 官方 issue/PR 研究 | 用户指令 | GitHub API 检索 KUAL=239 命中；重点 #15919(K4键码/推荐DenSwitch启动器)、#12692(NiLuJe: rootfs RO 无法变砖=防变砖权威背书)、#3018(K4NT世代冻结)、#13064(extensions/config.xml 机制)、#13602/#14204(白屏≠变砖)；报告 `docs/KOREADER_RESEARCH.md` |
| 28 | 安装方法文档 | 用户要求"已装原KUAL→新安装" | `docs/INSTALL_NEW_BUILD.md`：CI 产物下载校验、不同文件名并存安装、验证清单、30 秒回滚矩阵、K5(PEKI)差别、构建侧说明；ROLLBACK.md 补社区背书段 |
| 29 | **设备澄清（用户更新口径）**：黑色 K5 ≠ Kindle Touch | 用户说两台都通过 K4 原版 KUAL 使用；"kindle5 是 K4 的更新版、黑色、非 Touch" | 官方机型库 `kindles.json` 实证：K4=`0E`/Tequila/4.1.4；黑色 K5=Kindle 5th Gen=`23`/Sauza/4.1.4（绰号 K4B），同 Yoshi 平台；parse.awk:1095 `0E\|23` → "Kindle4"；KPanel 探测两台同走 KUIAdapter。**"K5→PEKI"旧归类作废**，两机同构同产物同流程；`docs/DEVICE_CLARIFICATION.md` |
| 30 | **C-2 模型名 bug 修复**（澄清顺带发现） | 补丁写 `"K4".equals(model)`，但 getModel() 对 0E/23 返回 `"Kindle4"` → 补丁在两台从未生效 | 改 `"Kindle4"`（KualConfig.java:72 既有用法交叉印证）；fork 提交 3889400（git over HTTPS 抖动，改用 gh api git/refs 直推）；CI 重出产物验证 class 内字符串=Kindle4、30×major48、三签名、jar verified；新 SHA `614a31e0…8842` |
| 31 | 两机同流程真机回测方案 | 用户"可以提供真机测试" | `docs/REAL_DEVICE_TEST.md`：基线→Z-1/Z-2→并存装新构建→B1–B6 判定（B4 决定 C-2 保留/revert）→回滚点→两机差分表 |
| 32 | 批量修正旧文档 PEKI 错误口径 | 保持全仓一致 | README/gawk-extension/README/KUAL.cfg/INSTALL_NEW_BUILD/ROLLBACK/PROJECTS/LOG 全部改为"两机同构"口径；handoff 与 memory 同步四轮状态 |
| 32b | **真机安装执行（K4）**：USB 已连 → udisks/sudo 挂载 sdb1(uid=1000) → 原版哈希比对一致 → 拷入 custom.azw2 + gawk + KUAL.cfg（全部 sha256 校验）→ 两本 azw2 并存于 documents/，gawk/cfg 就位，待设备端按 REAL_DEVICE_TEST.md B1–B6 回测 |
| 33 | WebSearch 不可用原因核实 | 用户追问是否需要装 skill | 属模型/后端不再提供原生 WebSearch，非 skill 问题；**无需安装额外 skill**，WebFetch + curl(GitHub API/raw/官方页) + LAN 代理已覆盖同类需求 |

| 34 | **K4 真机报障定位 + 修复**：两个同名 KUAL、其一报 main class not found | 用户报告书架有两本同名 KUAL，其中较新的一本打不开 | `documents/` 两份 azw2 的 Implementation-Title 均为 KUAL（故同名）；crash.log（`/mnt/us/developer/KUAL/work/crash.log`）实证较新那份缺 `ixtab/jailbreak/*`，`KualKindlet` 父类 SuicidalKindlet 加载失败 → KDK 报 main class not found。修复：fork 打包并入 lib/jailbreak.jar 的 7 个类、剔除误打包 .java（e312b14），本地重建并装机 `documents/KUAL-K4-custom.azw2`（sha256 83b1ddec…522b（JDK8 重签），58 条目/37 class 全 major48/jar verified）；原版未动 |
- **重签 azw2 必须用 JDK8 的 jarsigner**：JDK9+ 会写 CMS 认证属性（signingTime、CMSAlgorithmProtection），K4 的 KDK 校验失败并显示 "This item is not signed by an authorized developer."，而 `jarsigner -verify` 在本机仍报 verified——本机通过 ≠ 设备信任。判据：PKCS#7 SignerInfo 若含 [0] authenticated attributes（.RSA 约 1525 B）即不合格；JDK8 产物 .RSA 约 1370 B、无认证属性，与可用原版同构。
- 证书本身不是问题：新旧包三个签名证书 SHA-256 指纹完全一致（05:AB…:8A / 65:1A…:71 / C1:9D…:8F），有效期 1999-12-31→2099-12-31，digest 均为 SHA-256；差异只在签名块格式。

教训记录：
- Bash cwd 跨调用持久化 + `cd` 失败链：所有 qemu/gawk "静默 exit 1" 均为相对路径拼接错误，非二进制问题——排障应先 `pwd && ls` 确认基面。
- 本沙箱：sudo 不可用、userns 禁用、proot（/tmp 拷贝执行）被 exec 监控拦截；qemu-user 与 `apt-get download` + `dpkg-deb -x` 本地解包可行。
- 静态老 glibc 交叉二进制在宿主 qemu 测试必须 `LC_ALL=C`。
- qemu 测试环境已清理（磁盘回收 ~800MB）；复现：`curl -O http://deb.debian.org/debian/pool/main/q/qemu/qemu-user_10.0.2+ds-2+deb13u1~bpo12+1_amd64.deb && dpkg-deb -x … && qemu-arm ./gawk …`（qemu-user-static 包在新版只是符号链接，需配 qemu-user 包）。
- KDK1 的 azw2 必须把 `lib/jailbreak.jar` 的 7 个 `ixtab/jailbreak/*.class` 打进 jar：`KualKindlet extends ixtab.jailbreak.SuicidalKindlet`，只放进编译 classpath 不够。
- KDK 的 “main class not found” 不等于 Main-Class 写错：父类/依赖类缺失同样报这一句；权威证据在 `/mnt/us/developer/<Implementation-Title>/work/crash.log`（USB 可直接读），其中的 Caused by 才是真因。

## 五轮：RESUME 接手 + 启动错误根因修复（2026-11-06 晚）

| # | 动作 | 原因 | 结果 |
| --- | --- | --- | --- |
| 34 | orbital_handoff RESUME：check_staleness（VERY_STALE 判定=设备路径误报+正常迭代）+ 读 handoff | 用户要求接手核实项目状态 | 上轮停在"K4 并存安装完成、待真机回测" |
| 35 | 诊断"启动错误"：官方 vs CI 产物 class 逐一对比 | 安装后 custom 启动错误 | **实锤：CI 产物缺 7 个 `ixtab/jailbreak/*` class（含 SuicidalKindlet）**——KualKindlet extends SuicidalKindlet → NoClassDefFoundError；根因=上游 build.xml 注释掉的 `zipfileset lib/jailbreak.jar`，我的 workflow 组包跟着漏了 |
| 36 | 发现用户已在本地自行修复（e312b14：build.xml 恢复 zipfileset + workflow 加 unzip + Implementation-Title 改 KUAL-custom 便于书架区分），但 push 未成功 | 网络抖动 | 远程 master 仍 3889400 |
| 37 | 推送修复：git push 失败 ×N → 改走 gh api git data API（上传 2 blob 6c1779c/5b2352d → 组 tree 4623aab → commit 3f70880 → PATCH ref），单提交快进远程 | HTTPS 持续抖动 | 远程 master=3f70880，CI run#3 (42241900057) 自动触发并 success |
| 38 | v3 产物全量验证：**37 class 与官方逐一对齐（零缺失零多出）、37×major 48、三签名 jar verified、Implementation-Title=KUAL-custom、parse.awk `3f70880 (20261106,g)`** | 确认根因消除 | SHA `22c5f74f…f653`，留档 `ci-artifact/v3/` |
| 39 | 重装设备（USB 重枚举 sdb→sdc，remount）→ 用户手放的 `KUAL-K4-custom.azw2`（83b1ddec，也 37 类但无法追溯 run）按用户指令删除，保留 run#3 可审计产物 | 设备上出现两本 custom | 最终：original `70c301f2…` + custom `22c5f74f…` 两本并存，gawk/cfg 在位 |

**当前状态：等用户设备端重测（弹出 USB → 重启 → 开 KUAL-custom）。预期本次能进入菜单（不再 NoClassDefFoundError）。**

## 六轮：v3 仍启动错误的核实（2026-11-06 深夜，进行中）

| # | 动作 | 结果 |
| --- | --- | --- |
| 40 | 设备证据收集：custom 仍为 v3（22c5f74f）；无 documents/KUAL-*.txt scream 日志 → parse.awk 阶段未跑到，错误在 Kindlet 框架加载阶段 | 错误定位收窄 |
| 41 | jar 深度对比：MANIFEST 主段语义等价（唯一差异=信息性 `Ant-Version`）；非 class 条目零差异；zip 压缩方法/目录条目/首条目顺序一致；CRC OK；`jarsigner -verify -strict` 的 "signer errors"（PKIX/self-signed）在**官方上同样存在**（自签名正常现象）→ 签名嫌疑排除 | **jar 结构/签名层面与官方无实质差异**；唯一实质差异=class 字节码（CI JDK 8u504 vs 官方 8u442 编译 target 1.4） |
| 42 | **二分法布防**：官方原版改名拷贝 `documents/KUAL-official-copy.azw2`（70c301f2…同哈希） | 等用户三本书测试：official-copy 错→框架/注册层（冷启动已知问题/注册缓存）；official-copy 正常而 custom 错→聚焦 class 编译器差异（考虑 CI 固定 8u442 或换官方构建路径） |

**待用户提供：三本书（original / KUAL-custom / official-copy）各自的结果 + 错误提示原文。**

## 七轮：第二个根因（Booklet 化源码）与 v4 重建（2026-11-07，进行中）

| # | 动作 | 结果 |
| --- | --- | --- |
| 43 | 真机二分法结果（用户提供）：①原版 ✓ ②official-copy ✓ ③custom ✗（"An error occurred while trying to open this item"）→ **问题锁死在 custom jar 内容本身** | 排除框架/注册/信任链 |
| 44 | 37 类 Amazon 引用面全量对比：**master src 是 Booklet 化 KUAL**（KualKindlet/UIAdapter/JUIAdapter 用 BookletContext、KTimerAdapter 用 java.util.Timer、ui/v1 无 kindlet.ui.KPanel/KButton/KLabel）——master ≠ 发行版 KDK-1.0 源码；在 K4 上生命周期签名不匹配 → 必报该错误 | 第二根因实锤 |
| 45 | 方案 C 重建：`kdk1/` = 官方 v2.7 忠实反编译源（23 文件）+ C-3/C-2 补丁 + 1 处反编译 cast 修复 + **9 类编译期 stub**（从官方 37 类的精确成员调用面反推：KindletContext/AbstractKindlet/KindleKeyCodes/KPanel/KButton/KLabel/Timer/TimerTask/ParseException）+ 发行版 parse.awk 原文；本地 30 类编译通过 | 引用面对比：与官方唯一差异=KUIAdapter 的 KindleKeyCodes 常量内联（数值相同，无害） |
| 46 | workflow 重写（kdk1 树 + 断言 37 类/major48/jailbreak/parse.awk）+ 35 文件经 gh api git data API 推送（git over HTTPS 持续抖动） | 远程 master=bca2322，CI run 42313813485 success |
| 47 | **v4 产物**：37×major 48、jailbreak ✓、parse.awk ✓、签名待 CI 确认（本地 jarsigner verified）；SHA `6fde95bf…28bb7`；已装设备 | 等用户重测 |

**预期：v4 与官方在 Kindlet 框架视角等价（同 API 面/同类清单/同签名链），C-3/C-2 补丁已包含。**

## 八轮：v4 真机打开成功（2026-11-07）

- 用户确认：**KUAL-custom（v4，SHA 6fde95bf…28bb7）在 K4 上正常打开**——两层根因修复（缺 jailbreak 类 / master Booklet 化源码）全部验证生效。
- 剩余：按 REAL_DEVICE_TEST.md 完成 B2–B6（Unicode 标签 / 反复重绘 / 刷新 +750ms 体感 / 动作执行 / 退出），gawk 指纹验证；B4 结论决定 C-2 保留或 revert；K4 全绿后复制到黑色 K4B。

## 九轮：ub-mode 固化修复（2026-11-07 上午）

| # | 动作 | 原因 | 结果/证据 |
| --- | --- | --- | --- |
| 48 | 加载 ub-mode（CodeBuddy skill 列表无此项，直接读本机 `.zcode` / `.trae-cn` / `LibreTv4Droid` 三份定义） | 用户指令 | 三份 `SKILL.md` sha256 同为 `4120873d…3202`；三原则约束本轮：TDD 先看失败、最小改动、Keep a Log |
| 49 | 建立与 CI 同源的 ABI 断言工具 `/data/tmp/kual-abi-assert.py`，先跑红绿 | TDD：先看它按预期原因失败 | 官方 v2.7 九项全绿；v4 因「61 条目 + 3 个 `.java`」失败；v3 因「Booklet API + 错 Timer + strip 版 parse.awk」失败 |
| 50 | `build-kdk1.yml` 两处最小改动：`unzip` 后补 `find … -name '*.java' -delete`；把原有的 class 数量断言升级为 9 项 ABI parity 门禁（不新增第二个断言 step） | v4 已能启动，但 CI 仍出 61 条目，且没有任何断言阻止 Booklet 化 master `src/` 再次被编译 | YAML 解析 8 steps；从 workflow 抽出的 heredoc 与本地工具 `check()` 逐字一致 |
| 51 | `gh api` git data API 基于 `bca2322` 快进推送（本地 HEAD 仍是 `e312b14`，直接 commit 会分叉） | git over HTTPS 持续抖动 | master=`658b1cd`；CI run `42317437549` success；门禁日志 9×PASS + `ABI GATE PASS` |
| 52 | v5 产物留档并复核 | 产物必须可追溯 | `ci-artifact/v5/`：131355 B、sha256 `1368b95a…f0aa`、58 条目 / 37×major48 / 零 Booklet / 官方 parse.awk / 3×`.RSA` 1370 B，本地门禁全绿。**未装机**：设备 v4 已可启动，保持不动 |
| 53 | 核实 `k4refresh/config.xml` 旧式 `<menu>` | 排查菜单项 | 官方 `parse.awk:487` 只匹配带 `type="json"` 的 `<menu …>`，该扩展菜单不显示（不影响启动）。涉及另一仓库与真机文件，用户取消了本项排查 → 未改动，待决定 |
| 54 | 真机 B2–B6 / gawk 指纹 / K4B 复制 | 八轮遗留 | 待用户设备配合 |

**约定**：设备保持 v4（可用状态优先，First Make it Work）；v5 仅作 CI 防回归验证留档。

## 十轮：ub-mode 安装 + k4refresh 菜单修复（2026-11-07 上午）

| # | 动作 | 原因 | 结果/证据 |
| --- | --- | --- | --- |
| 55 | 核实 ub-mode 上游仓库 | 用户给 `unclebod/ub-mode` 并授权自行下载 | 该名 404；实际为 `unclebob/ub-mode`（master，2026-11-06 更新）。上游 `SKILL.md` 与本机 `.zcode`/`.trae-cn`/`LibreTv4Droid` 三份副本 sha256 同为 `4120873d…3202` |
| 56 | 下载并安装 ub-mode 三件套到 `~/.codebuddy/skills/ub-mode/` | 用户授权 | `SKILL.md`/`README.md`/`LICENSE` 就位，SKILL.md 与上游逐字节一致；本环境 skill 列表为静态，需刷新 IDE 后才能 `/ub-mode` 调用 |
| 57 | 核实两仓拓扑与 1B 风险 | 用户追问「handoff 或 git 中没有记录吗」 | 有记录：HANDOFF.md:11,121、LOG.md:30、README.md:48；git 侧 `fork-Booklet/.git/config` 有 `origin=https://github.com/dororo42/KUAL_Booklet.git`。**缺口**是「本仓无 remote」从未明写 → 已补进交接文档。另发现 `KUAL_Booklet` 是公开 fork，而本仓已跟踪 3 个 `upstream-denswitch/*.p12` 私钥、官方 azw2、PEKI jar、26 个反编译 .java → 推到公开 fork 分支会公开私钥/版权物，B 不推荐 |
| 58 | k4refresh 菜单 TDD 红灯 | 先证明 bug 再改 | 官方 parse.awk 跑合成 extensions：旧 `config.xml` → 输出 `● No extensions found`、0/7 菜单项 |
| 59 | 修 k4refresh：`config.xml` 改 `<menu type="json">menu.json</menu>`，新增 `menu.json`（7 项 1:1 映射 name/priority/action/params） | KUAL 2.7 的 parse.awk 只识别 json 菜单 | 修后同一 parse.awk 输出 7 个标签，action = `<ext>;bin/k4r.sh <arg>`，无 `No extensions found`，exit=0；7 个参数与 `k4r.sh` 的 case 分支逐一对应；JSON/XML 解析通过 |
| 60 | 打通交付链 | 只改 config+menu 不进 Release zip 等于白改 | `release.yml` 打包 + INSTALL.txt、`readme.md` 包清单 + 部署命令、`k4r.sh` 注释均加 `menu.json`；SHA256SUMS 由 `find` 自动生成已覆盖；YAML 解析通过 |
| 61 | 提交并推送 | ub-mode: Commit and Push | `dororo42/k4refresh` master `44028b7`（`c0cbb8f..44028b7`），远端 HEAD 一致、工作区干净 |
| 62 | 真机文件同步 | 设备侧生效 | 设备未连接 → 未写；连上后把 `config.xml` + `menu.json` 拷到 `/mnt/us/extensions/k4refresh/` 并**重启 Kindle**（KUAL 只在启动时扫描 extensions/） |

**待用户决定**：本仓 `/home/doro/KUAL` 仍无 remote（含 p12 私钥，勿推公开仓），1A/1B/1C 未选；真机 B2–B6 与 K4B 复制未做。

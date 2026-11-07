# 已安装原版 KUAL → 安装新构建（改码版 azw2）的方法

> 适用：**两台设备**——Kindle 4 (K4NT/D01100, `0E`) 与黑色 Kindle 5 = Kindle 5th Gen / K4B (D01200, `23`)，**都是 FW 4.1.4 / KDK 1.0 / 非触摸，同一份 azw2、同一套流程**（`docs/DEVICE_CLARIFICATION.md`）。
> 新构建 = `dororo42/KUAL_Booklet` 的 CI 产物 `KUAL-KDK-1.0-custom.azw2`（C-3 + 审查修复 + 已修正的 C-2）。
> **本机无法构建**（缺 JDK 8/Ant/Kindlet SDK），改由 GitHub Actions 产出：workflow `.github/workflows/build-kdk1.yml`，首次运行 [run 42143295293](https://github.com/dororo42/KUAL_Booklet/actions/runs/42143295293) ✅ success。

> ⚠️ **2026-11-06 真机修正（两次）**
> 1. 首个 CI 产物 `614a31e0…8842` 缺 `lib/jailbreak.jar` 的 7 个 `ixtab/jailbreak/*.class`（`KualKindlet` 的父类），K4 上一点开即报 "main class not found"（crash.log：`NoClassDefFoundError: ixtab.jailbreak.SuicidalKindlet`）。已在 fork `e312b14` 修复打包。
> 2. 用 JDK21 本地重签的版本虽 `jarsigner -verify` 通过，但设备报 "This item is not signed by an authorized developer."——JDK9+ 的 jarsigner 会给 PKCS#7 加 CMS 认证属性（signingTime、CMSAlgorithmProtection），K4 的 KDK 不接受。
> **重签必须用 JDK8 的 `jarsigner`**（CI 的 setup-java 8 本身没问题，问题只出在本地重签）。
> 当前可用产物：`documents/KUAL-K4-custom.azw2`，SHA-256 `83b1ddecd435c9c98a1f2d17180415900fec747af701369a44b7fda2f003522b`（127181 B / 58 条目 / 37 class 全 major 48 / 三个签名块均无认证属性 / .RSA 1370 B，与可用原版同构）。

## 0. 防变砖与可回滚（先读，结论先行）

**本操作无变砖路径**：
- 只向 `/mnt/us/documents/` 新增**一个** azw2 文件；不删除、不覆盖原版 `KUAL-KDK-1.0.azw2`，不写入 rootfs/内核/引导区。
- Kindle 的 rootfs 是只读的（NiLuJe 在 [KOReader #12692](https://github.com/koreader/koreader/issues/12692) 的结论），社区所谓"变砖"多为老设备软锁，重启/恢复模式可解。
- 最坏情况：新 azw2 打不开（Kindlet 框架错误框）或菜单空白——**删掉新文件即完整回滚**，原版 KUAL 分毫未动。KUAL 白屏类故障的社区常规处置也是重启/重跑 hotfix/换回 KUAL 文件（[#13602](https://github.com/koreader/koreader/issues/13602)、[#14204](https://github.com/koreader/koreader/issues/14204)）。
- 前提：设备已越狱且 MKK（MobileRead Kindlet Kit）信任链 intact——新 azw2 用的是与官方 v2.7 **指纹完全一致**的三把开发者证书签名（CI 里 `developer.keystore` 三别名），所以能拿 AllPermission。若 MKK 缺失，会提示 keys/developer/测试设备错误（此时补装 MKK，而非动系统）。

## 1. 准备（电脑上）

1. 从 GitHub Actions 下载产物：
   ```
   https://github.com/dororo42/KUAL_Booklet/actions → 最新成功 run → Artifacts: KUAL-KDK-1.0-custom
   ```
   本地留档副本：`ci-artifact/KUAL-KDK-1.0-custom.azw2`（**当前待测版** SHA-256 `614a31e02ba4b0771ce601535a83400e7708320ec9b703ba7955dbced2af8842`，含 C-2 模型名修正；旧版 `803bce83…5d69` 已弃用）。
2. 校验（下载后必做）：
   ```
   sha256sum KUAL-KDK-1.0-custom.azw2
   jarsigner -verify KUAL-KDK-1.0-custom.azw2          # 应输出 "jar verified."
   unzip -l KUAL-KDK-1.0-custom.azw2 | grep -E "DNTEST|DITEST|DKTEST|parse.awk"   # 三签名 + 解析引擎齐全
   ```
   CI 内已断言：30 个 class 全部 **major 48（Java 1.4）**——KDK 1.0 运行时只认这个字节码版本。
3. 把文件重命名为**与原版不同的名字**（关键：让两本并存在书架，可对照、可秒回滚）：
   ```
   KUAL-KDK-1.0-custom.azw2    ← 新构建（保持此名即可）
   KUAL-KDK-1.0.azw2           ← 原版，原封不动
   ```

## 2. 安装（真机）

```
1. USB 连接 Kindle，打开大容量存储模式
2. 拷贝新文件到 /mnt/us/documents/（与原版 KUAL 同目录，不要覆盖任何文件）
3. 安全弹出 USB → 重启一次 Kindle（让书架扫描注册新书）
4. 书架出现第二本 "KUAL"（封面同为 KindleLauncher.jpg，可按 azw2 文件名区分）
5. 先点开【原版】确认一切正常（对照基线）
6. 再点开【新构建】，观察：
   - 菜单正常显示、扩展齐全（含含 Unicode 符号 ✓▽✯ 的条目 —— C-3 改动面）
   - 执行菜单 r（刷新）看有无丢帧/白屏（C-2 改动面）
```

## 3. 验证新构建真的生效

- **C-3 面**：Unicode 标签条目显示应无错位（offsets 缓存 + 字体 run 合绘，像素等价）。
- **C-2 面**：K4 刷新后 `afterParser` 从 750ms 变 1500ms——应在日志/体感上表现为刷新更稳（若你觉得反而变慢，说明 K4 不需要这个缓冲，见下回滚）。
- **崩溃日志**：异常会写 `/var/tmp/KUAL.log` 与 `KUAL-*.txt`（parse.awk scream 机制），可 USB 拷回电脑查看。
- **缓存自愈**：新旧 azw2 共用 `/var/tmp/KUAL.cache`；parse.awk 校验版本串+md5，不匹配自动删除重建，不会互相污染。

## 4. 回滚（任何一步不对，30 秒完成）

| 症状 | 动作 |
| --- | --- |
| 新构建打不开 / 菜单空白 / 报错 | USB 删除 `/mnt/us/documents/KUAL-KDK-1.0-custom.azw2` → 重启 → 用原版 |
| 只是想退回 C-2（+750ms 让刷新变慢） | 无需删文件：C-2 是独立提交，在 fork 里 `git revert 332659e` → CI 重新出包；或直接装回原版 |
| 缓存异常 | USB 删除 `/var/tmp/KUAL.*`（KUAL 下次启动自动重建） |
| MKK 提示（keys/developer） | 补装/修复 MKK 与 hotfix（重启两次、重跑 hotfix book，同 #13602 处置） |
| 老设备软锁（"needs repair"） | 长按电源重启；必要时 Wall charger 静置（KOReader #12692 社区处置）；仍不行走 Amazon 恢复模式线刷——与本次改动无关 |

## 5. 两台设备（K4 银 + 黑色 K4B）：流程完全相同

用户两台都是 FW 4.1.4 / KDK 1.0 / Yoshi 平台，parse.awk 把 `0E|23` 都识别为 "Kindle4"——**本文 §1–§4 的安装、验证、回滚在两台上一字不差地各执行一遍**，同一份 `KUAL-KDK-1.0-custom.azw2`、同一套 gawk 扩展、同一份 KUAL.cfg。逐项判定与差分记录表见 `docs/REAL_DEVICE_TEST.md`。

（PEKI/`KUAL.jar` 属于 FW 5.x+ 世代的 Kindle Touch 等机型，**不适用于这两台**；`kual-audit/peki/` 仅作反例留档。）

## 6. 构建侧说明（为何用 CI）

- 本机缺 JDK 8 / Ant / Amazon Kindlet SDK jar；且 `lib/README.txt` 明确这些 jar **因版权不能分发**（需从 K3/K4 的 `/opt/amazon/ebook/lib/` 提取）。
- workflow 的解决办法：只生成 **6 个 Amazon API 的编译期 stub 类**（不进产物），配合仓内 `lib/jailbreak.jar` 与 `developer.keystore` 即可编译+签名；`javac -source 1.4 -target 1.4` 由 `actions/setup-java@v4` 的 JDK 8 提供，产物断言 major 48。
- `parse.awk` 由 `tool/stripit.sh` 从 `parse-commented.awk` 生成，CI 注入 git sha/日期（与官方 `v2.7.37-gfcb45b5 (20250419)` 同语义）。
- 产物已实测：30×major 48、三套 RSA 签名 `jar verified`、parse.awk 33,143 B。

> ⚠️ 真机最终验收（含 C-3 像素目检、C-2 刷新体感、KOReader 退出键码耦合 [#15919](https://github.com/koreader/koreader/issues/15919)）仍需你在 K4 上执行；本文档所有"可回滚/不变砖"结论均有代码与社区实证支撑，但**不要在未备份原版 azw2 前删除它**。

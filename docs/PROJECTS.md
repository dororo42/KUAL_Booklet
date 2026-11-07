# 相关研究项目清单（GitHub / Reddit / MobileRead，2026-11-06 检索）

## KUAL 本体与源码（补丁重建基线）

### Fork 谱系（GitHub API 实证，2026-11-06）

```
coplate/KUAL_Booklet (origin, 2018-02 停更, "staging to track bitbucket")
  └─ NiLuJe/KUAL_Booklet (现役正主, 2025-04-19 push = "Resync w/ KUAL 2.7.37" = 发行版 azw2 本源;
  │    has_issues=False, 历史 0 PR → GitHub 协作流关闭, 正道是 MobileRead 主帖;
  │    master 自带 developer.keystore 指纹=官方 v2.7 三证书 ✓, build.xml: 1.4 目标 + SHA256withRSA 三别名签名)
  └─ yparitcher/KUAL_Booklet (100★, fork of NiLuJe, +20 commits = KOL 品牌化"只启动 KOReader";
       PR 通道有先例: NiLuJe 本人 2022 年提过 #1 "Fix ant packaging")
```

| 项目 | 说明 | 与本工作的关系 |
| --- | --- | --- |
| [NiLuJe/KUAL_Booklet](https://github.com/NiLuJe/KUAL_Booklet) | **现役正主**：master 即 v2.7.37 发行版源码（KualKindlet/Util/resources/timer/ui 全在），自带可签出受信 azw2 的 developer.keystore；README 明确 K4 及更老用 KDK-1.0、KOA+ 用 Booklet；记载 gawk 约快 2 倍 | 已以 tarball 留档为 `upstream-kual/`（补丁正基线）；**PR 不走它家 GitHub，走 MobileRead** |
| [coplate/KUAL_Booklet](https://github.com/coplate/KUAL_Booklet) | fork 源头仓，2018 年停更 | 旧 staging（我们最初误用作基线，已弃） |
| [yparitcher/KUAL_Booklet](https://github.com/yparitcher/KUAL_Booklet) | KOL fork（KOReader 快启），唯一有 PR 文化的 GitHub 目标 | fork-PR 路线的现实选项 |
| [MobileRead KUAL v2.7 主帖](https://www.mobileread.com/forums/showthread.php?t=203326) | 官方发布/支持帖；现成 "GNU Awk Installer" 扩展（一键装 gawk）；v2.7 支持机型表无 K5 | **补丁贡献正道**；本地留档 `upstream-kual/MR_THREAD.txt` |
| [KindleModding 安装页](https://kindlemodding.org/jailbreaking/Legacy/post-jailbreak/installing-kual-mrpi/) | 官方路由：K4 and older → KDK-1.0 azw2；**K5 and newer → PEKI** | 快照在 `kual-audit/page.html` |

## K5 / 原生启动器方向

> ⚠️ **命名提示（四轮澄清）**：社区 "K5" 有两个含义——(a) **Kindle Touch（2011, FW 5.x, usid 0F/11/10/12）**，官方机型库给的绰号就是 K5；(b) 用户手里的**黑色 Kindle (5th Gen, 2012, K4B, usid 23, FW 4.1.4)**，与 K4 同平台，走 **KDK-1.0 azw2**。下表 PEKI 一项针对 (a)，**不适用于用户的两台设备**；实证见 `docs/DEVICE_CLARIFICATION.md`。

| 项目 | 说明 | 与本工作的关系 |
| --- | --- | --- |
| [KindleTweaks/PEKI](https://github.com/KindleTweaks/PEKI) | **FW 5.x+ 世代**的 KUAL 交付链（KUAL.sh + KUAL.jar）；其 jar 与 KDK-1.0 同引擎（gawk 探测/EXTENSIONDIR/CONFIGFILE 已逐一实证） | 留档 `kual-audit/peki/` 作反例参考；用户的两台 FW 4.1.4 设备**不用它** |
| [thiagokokada/kual-next](https://github.com/thiagokokada/kual-next) | 原生启动器（FBInk），兼容 KUAL menu.json 扩展；**要求 hard-float FW 5.16.3+** | 报告 §5.④"原生启动器"的现实参照，但 **K4/K5 旧固件不适用** |
| [ufuchs/kite-kindle4nt](https://github.com/ufuchs/kite-kindle4nt) | Kite 热键守护进程的 K4NT 定制版 | 报告 §5.④ 先例（非 Kindlet 路线） |
| [SotongDJ/launchpad-kindle](https://github.com/SotongDJ/launchpad-kindle) | Launchpad 热键守护（Kindle Keyboard 世代） | 同上 |
| [DenSwitch/kindle4-kindlet-launcher](https://github.com/DenSwitch/kindle4-kindlet-launcher) | 纯 Python 生成**签名 azw2** 的工具；`keys/dntest|ditest|dktest.p12` 与官方 KUAL v2.7 三张证书**指纹逐一比对一致**（本仓已留档 `upstream-denswitch/`） | **重签名路径已打通**：C-2/C-3 补丁可用公开 MR 开发者钥匙签出受 MKK 信任的 azw2 |
| [jim/kindleland](https://github.com/jim/kindleland) | Kindle framebuffer `/dev/fb0` 直绘实验 | 原生启动器的 fb 层参考（与 k4refresh 同层） |
| [KOReader #15919](https://github.com/koreader/koreader/issues/15919) | K4 (FW 4.1.4) 退出键码差异（139 vs 102）与本地 launcher 变通 | K4 输入细节参考 |

## 社区讨论（Reddit）

- [KUAL Next 发布帖](https://www.reddit.com/r/kindlejailbreak/comments/1vtxtns/kual_next_a_native_launcher_for_existing_kual)（r/kindlejailbreak）
- [What happened to KUAL?](https://www.reddit.com/r/kindlejailbreak/comments/1vp29ug/what_happened_to_kual) — 新机型上 KUAL 现状
- [Kindle 4 non-touch plugins](https://www.reddit.com/r/koreader/comments/1w0xnyp/kindle_4_non_touch_plugins_for_ui_and_libby)（r/koreader）— K4NT 越狱生态
- [Trouble getting KUAL on jailbroken Kindle Touch](https://www.reddit.com/r/kindlejailbreak/comments/1u72tkm/trouble_getting_kual_on_jailbroken_kindle_touch) — 老设备 KUAL 排障

## 结论映射（对审查报告 §5 的修正/强化）

1. **②（parse.awk→Rust）**：社区没有现成 Rust 引擎；但 **gawk 路线已有官方背书**（NiLuJe README ≈2×）+ 现成 GNU Awk Installer 扩展——Z-1 的正确性被独立佐证。
2. **④（K4 原生启动器）**：kual-next 证明 menu.json 兼容的原生启动器可行，但其 hard-float 5.16.3+ 门槛把 K4/K5 排除在外；K4/K5 世代原生路线的现成先例是 kite/launchpad（热键守护）而非全功能菜单——若立项，可参考 kual-next 的扩展兼容层设计。
3. **重签名**：**NiLuJe master 自带 `developer.keystore` 指纹=官方 v2.7 三证书（二轮实证），fork 即含完整受信构建链**（源码+1.4 构建+SHA256withRSA 三别名签名）；DenSwitch p12 为等价备用。报告"改码必失 MKK 信任"的阻断不成立——但这也印证报告 S-1 的信任模型：任何持有公开钥匙者都可签出受信 Kindlet。

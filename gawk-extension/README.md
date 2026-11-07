# KUAL gawk 扩展（Z-1 优化交付，**K4 + 黑色 K4B/Kindle 5th Gen 同构适用**）

为 Kindle 4（`0E`, Tequila）与黑色 Kindle 5 = Kindle 5th Gen / K4B（`23`, Sauza）预编译的**静态 gawk 5.3.2**。两台都是 FW 4.1.4 / KDK 1.0 / Yoshi 平台，KUAL 同走 KDK-1.0 azw2；把 `bin/gawk` 放到 `/mnt/us/extensions/gawk/` 后，两台的解析引擎都自动从 busybox awk 切换到 gawk 快路径。

## 双机型适用性核实（2026-11-06 四轮澄清后定稿）

> ⚠️ 早期版本把"K5"误归类为 Kindle Touch/PEKI 链——**已作废**。官方机型库（`kindlemodding` 仓库 `static/kindles.json`）实证：K4=`0E`/4.1.4，黑色 K5(5th Gen, 2012)=`23`/4.1.4，两者同平台同固件；parse.awk:1095 把 `0E|23` 都归 "Kindle4"；UIAdapter 的 KPanel 探测在两台都命中 KDK1 → KUIAdapter。详见 `docs/DEVICE_CLARIFICATION.md`。

| 设备 | KUAL 交付链 | gawk 探测证据 |
| --- | --- | --- |
| **K4** (`0E`) | `KUAL-KDK-1.0.azw2`（Kindlet, KDK 1.0） | `Util.java:27-28`：`new File("/mnt/us/extensions/gawk/bin/gawk").exists()` → `gawk -O --non-decimal-data` |
| **黑色 K5 / K4B** (`23`) | **同一份** `KUAL-KDK-1.0.azw2`（Kindlet, KDK 1.0） | 同一份 jar 内同一段代码，无需区分 |

（PEKI/`KUAL.jar` 是 FW 5.x+ 世代的路由，与用户两台无关；其 jar 留档 `kual-audit/peki/` 仅作反例。）

**gawk 二进制跨机型通用**：静态链接（不依赖设备 libc），ARMv7-A / Thumb-2 / VFPv3——两台同为 i.MX50 系 Cortex-A8 平台；工具链本身即名 `arm-kindle5-linux-gnueabi`（NiLuJe 为该世代 Kindle 构建）。

## 安装（USB 方式，两台完全相同）

```
1. Kindle 连接电脑（USB 大容量存储）
2. 将整个 gawk 目录拷入 /mnt/us/extensions/，最终结构：
   /mnt/us/extensions/gawk/bin/gawk
3. 安全弹出 USB
4. 打开 KUAL（菜单刷新 r，或重开 KUAL）
```

无需 config.xml——gawk 是"库型"扩展，故意不注册菜单项，KUAL 的 `find … -name config.xml` 扫描会自动跳过它。

## 验证（真机，两台同法）

1. **生效指纹**：打开 KUAL 后查看缓存头两行：
   ```
   head -2 /var/tmp/KUAL.cache
   ```
   第 2 行版本串尾部为 **`(20250419,g)`**（两台相同）——`g` 取自 `ARGV[0]` 首字母（parse.awk:4-5），即 gawk 快路径已生效。
2. **二进制自检**（kterm/KUAL 动作里执行）：
   ```
   /mnt/us/extensions/gawk/bin/gawk --version    # 应输出 GNU Awk 5.3.2
   ```

## 产物与验证记录

| 项 | 值 |
| --- | --- |
| 文件 | `bin/gawk`（1,070,948 B, stripped） |
| SHA-256 | `7ae94d1f79db1dcaf9fa0ba5bad5bdcbe45edfedaba843c99b443dace986e6e6` |
| 构建 | GNU gawk 5.3.2, `--disable-nls --disable-extensions --without-readline --without-mpfr --without-gmp`，`-O2 -static` |
| 目标 | ARM EABI5, ARMv7-A / Thumb-2 / VFPv3（readelf -A 实证） |
| 链接 | **静态**（规避 K4/K5 固件 glibc 2.12.x 兼容问题） |
| 工具链 | crosstool-NG (NiLuJe) `arm-kindle5-linux-gnueabi` GCC 14.4.0（与 k4refresh 同链） |

**行为验证（qemu-arm 用户态模拟）**：`--version` ✓；核心语言 ✓；**原版 1276 行 parse.awk 全流程**（合成扩展）输出 30 行完整 KUAL 协议 + cache + cfg，exit=0 ✓。

测试注记：qemu 下需 `LC_ALL=C`（静态 glibc 2.12.2 读宿主新版 locale-archive 的已知环境冲突，真机 libc 自洽不受影响）；本沙箱 exec 监控使 proot 不可用，故采用 `-v` 注入路径方式，未改动 parse.awk 任何字节。

## 对照：社区已有产物

MobileRead KUAL 主帖存在现成的 **GNU Awk Installer** 扩展（KUAL 菜单内一键装 gawk，NiLuJe README 称解析约快 2 倍）。本交付的差异：静态自建（不依赖论坛附件的供应链，对应审查报告 S-4）、哈希固定、qemu 行为验证、与 k4refresh 工具链同源可复现。两者可互为备份。

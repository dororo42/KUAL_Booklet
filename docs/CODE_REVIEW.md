# Fork 改动代码审查报告（code-review-skill + eagle-eye-senior-dev，2026-11-06）

审查对象：`dororo42/KUAL_Booklet` 分支 `feature/k4-c3-glyph-cache`（master `770cd82`），相对基线 `c6ac782 Resync w/ KUAL 2.7.37` 的 3 个提交（C-3 两文件、C-2 一行、审查修复）。Java 1.4 / AWT / 单线程事件分发 / 256MB RAM 的 K4 设备约束。

## 审查结论：✅ Approve（含 3 项已修复发现）

**审核范围**：逐行 diff（79 行）+ 完整源码上下文 + 编译验证 + 边界行为测试 + 设备约束（Java 1.4 语法、KDK1 AWT、内存、签名/字节码版本）。

### 发现与处置

| # | 级别 | 发现 | 处置 |
| --- | --- | --- | --- |
| 1 | 🟡 important | `calculateOffsets` 直接返回缓存的 `int[]`，调用方若修改数组会污染缓存（API 契约脆弱） | **已修**：返回防御性副本，缓存内也存副本（770cd82） |
| 2 | 🟡 important | `OFFSET_CACHE` 按 String 无限增长；菜单标签非严格有限（mru 排序、动态状态标签、扩展增删），K4 仅 256MB RAM | **已修**：上限 256 条，超出清空（bounded，770cd82） |
| 3 | 🔴 blocking（验证中拦截） | 一次"改用 isUnicode 布尔分组"的尝试引用了 `private isUnicode(char)` → 编译失败 | **已拦截并回退**：改回公开的 `getFontFor()` + 身份比较并加注释说明其稳定性；`javac --release 8` 通过 |
| 4 | 🟢 nit | 字体 run 用 `Font ==` 身份比较 | 保留：`getFontFor()` 返回两个实例字段之一，KualFonts 实例生命周期内恒等；已加注释 |
| 5 | 🟢 nit | raw `HashMap`（无泛型） | 保留：Java 1.4 目标不允许泛型，与全仓风格一致 |
| 6 | 💡 suggestion | C-2（K4 +750ms）方向性风险：缓冲本为慢 JVM 准备，K4 可能不需要，应用后只会增加刷新延迟 | 保留为**独立提交**，reviewer 可单独 revert；commit message 与文档均标注"真机实测门控" |
| 7 | 🎉 praise | 两个功能提交 + 审查修复分 3 个语义提交，`--no-ff` 合并保留拓扑，便于单独回退 | — |

### 验证记录（eagle-eye: write → verify → report）

- ✅ `javac --release 8` 桩编译（`KUIAdapter` 静态方法桩）通过；仅 Java 1.4 raw-HashMap 固有 unchecked 提示
- ✅ 边界行为测试：空标签（offsets=[0]，run 循环零次）、纯 ASCII 单 run、混合 Unicode 多 run（`A✯B` → D@0x1 / U@1x1 / D@2x1）、全 Unicode 单 run
- ✅ 像素等价论证：offsets 为累计字符宽度，run 从 `offsets[start]` 起整段绘制 == 逐字符位置
- ✅ `git diff --check` 干净；未使用 `Font` import 已清理
- ⏭️ 设备级 AWT 目检（Unicode 标签、刷新帧率）仍需真机——不阻塞合并，按真机门控执行

### 未审查/超出范围

- 未做 ant 全量构建（本机缺 JDK 8 / Ant / Kindle SDK jar，见 `docs/INSTALL_NEW_BUILD.md`）
- 未改 parse.awk、Util、签名链（均无改动）
- 无 LICENSE 头变更（三仓本身无 LICENSE 文件）

## 提交拓扑（fork master）

```
770cd82 review: bound the glyph-offset cache and harden its API
6619bcc Merge K4 KDK1 UI optimizations (--no-ff)
├─ 332659e KualKindlet (KDK1/K4): include K4 in the slow-CPU refresh buffer  [可单独 revert]
└─ 3dea78e ui/v1 (KDK1/K4): cache glyph offsets and batch same-font runs
c6ac782 Resync w/ KUAL 2.7.37 (NiLuJe master 基线)
```

##############################################
# KUAL: Kindle Unified Applications Launcher #
##############################################
http://www.mobileread.com/forums/showthread.php?p=2389078

Supporting the DX, K2, K2i, K3 Keyboard, Kindle 4, Kindle Touch, PW, PW2,
along with the KV, KT2, PW3, KOA, KT3, KOA2, PW4, KT4, KOA3, PW5, KT5, KS, KT6, PW6, KS2 & CS.

NOTE: Flashed 3.2.1 DX's may fail to handle kindlets properly.
There is a workaround tested by wakawakawaka (thanks to him for the DX testing)
http://www.mobileread.com/forums/showpost.php?p=1747631&postcount=354
Workaround 3.2.1 DX Link is above.
In addition, REALLY old firmwares of the Kindle 2 did not support kindlets.
Please update these to later firmwares to get support.
NOTE: On the KV, KT2 & PW3, we may only run on select firmware versions.
NOTE: On the KOA, KT3, KOA2, PW4, KT4, KOA3, PW5, KT5, KS, KT6, PW6, KS2 & CS, Kindlets are deprecated. As such, only the Booklet version is usable.


SUPPORT: READ THIS >>>

BitRot, No Magic, Voodoo (or Unicorns)  (what will work, what won't work)
SOME OF THE OLDER PROGRAMS NO LONGER WORK ON THE NEWER KINDLES
(nothing to do with this launcher they just dont work properly anymore)
SUCH AS MANY IN THIS OLD LIST:
http://wiki.mobileread.com/wiki/Kindle_Touch_Hacking#Extensions_for_the_GUI_Launcher
SOME OF THESE DONT WORK, SOME WORK ON CERTAIN FIRMWARES ONLY.
AS A COURTESY THESE APPLICATIONS ARE LISTED ON THE MAIN THREAD UNDER KNOWN ISSUES.

ALSO: SOME EXTENSION SUCH AS Kterm ARE DEVICE SPECIFIC.
THIS LAUNCHER WON'T MAGICALLY MAKE DEVICE-SPECIFIC CODE WORK ON OTHER DEVICES.
If it doesn't normally work on that type of device? It won't work via this either.

Enough shouting.
It's a launcher of "other things" not an exciting thing in itself.

We have now included a few helper scripts to get you going called "Helper"
(attached on main thread - unzip to [FAT-ROOT])
And also a set of extensions for the main KUAL helpers
(also available from main thread)

#####################################
# Requirements, Installation & Use: #
#####################################

Jailbreak:
Go find the relevant Jailbreak for your device.
Links are provided on the main thread.

Mobileread Kindlet Kit:
http://www.mobileread.com/forums/showthread.php?t=233932

Running the kindlet:
Depending on your device:

(k2, dx, K3, K4)
Put KUAL-KDK-1.0.azw2 in documents folder.
Run it by clicking new kindlet document in your list.

(Touch, PW, PW2, KV, KT2, PW3)
Put KUAL-KDK-2.0.azw2 in documents folder.
Run it by clicking icon.

(KOA, KT3, KOA2, PW4, KT4, KOA3, PW5, KT5, KS, KT6, PW6, KS2, CS)
Install the KUALBooklet update package via MRPI.
Run it by clicking KUAL document.

(K2, DX, K3, K4, and the black Kindle 5 / 5th Gen "K4B")
Prefer a rebuilt one? Grab KUAL-neo.azw2 from the Releases page.
Same documents-folder install, signed with the same MKK-trusted keys,
shows up as "KUAL-neo" on the shelf (the original KUAL entry stays).
Rebuilt from the official v2.7 kindlet decompiled sources (see kdk1/ in
the repo, with CI asserting the 37-class inventory byte for byte) plus
two small UI fixes: cached glyph offsets and batched same-font runs.
Deleting either azw2 rolls that one back, the other is untouched.

Expected output:
It should read thru the /mnt/us/extensions folder and build an extension list.
If you have NO extensions? It will show a message telling you that.
You then select a button to run the relevant command.
Paginated results are provided.
If the device shouts at you about keys, developer access, a testing device or a jailbreak,
you forgot to install the Mobileread Kindlet Kit ;).

NOTE: /mnt/us/extensions is simply a folder called "extensions"
a normal folder you can access when you connect via normal USB.
Usbnetworking is not "required" to access this folder.

TO EXIT:
Generally the kindlet will exit when you click a choice
Currently just press the HOME key if you have one or the < icon.
Or select an item, the menu is now self-terminating.

NOTE:
Additionally, you can also install a custom awk build (GNU awk), making the
parser around two times faster, by installing gawk with this extension:
http://www.mobileread.com/forums/showpost.php?p=2636883&postcount=59
(KUAL-neo keeps this probe unchanged, so the gawk note applies to it too.)


##############################################
# KUAL-neo (中文说明 / Chinese notes)        #
##############################################
KUAL-neo 是什么：
  面向 K2/DX/K3/K4（含黑色 Kindle 5，5th Generation，社区称 K4B）的
  KDK-1.0 重建版。源码来自官方 v2.7 Kindlet 的忠实反编译（仓库 kdk1/
  目录，CI 逐类断言 37 个 class 与官方清单一致），在此之上叠加两处
  界面层优化，使用与官方相同的 MKK 信任开发者证书签名。

安装（与原版并存，互不干扰）：
  把 KUAL-neo.azw2 放入 documents 文件夹（与 KUAL-KDK-1.0.azw2 并存即可）。
  书架显示为 "KUAL-neo"，与原版 "KUAL" 一眼区分。
  删除任意一本即回滚该本，另一本不受影响。

改动点（仅两处界面层优化，均为像素等价）：
  1) ui/v1/KualFonts: 按标签缓存字形偏移（上限 256 条 + 防御性副本）
  2) ui/v1/KualPainter: 连续同字体字符合并为一次绘制（减少字体切换）
  3) 可选项: K4 加入刷新后 +750ms 慢 CPU 缓冲（真机实测门控保留）
  注: gawk 加速路径与原版完全一致（/mnt/us/extensions/gawk/bin/gawk）。

真机验证（K4 + 黑色 K5）：
  B1-B6 全绿；gawk 指纹 "(20250419,g)" 两台确认；benchmark 60 条 0 error；
  KOReader 会话退出码 0。构建与断言见仓库 kdk1/ 与 CI 工作流。

Credits.
Twobob Ixtab Stepk Niluje
Thanks to KNC1 for download authentication support.
wakawakawaka for testing the DX prerequisites.
Yifan was the original chap whoms configs we parse.
All the unsung demo authors whose work we draw from on a daily basis.
And everyone else along the way.

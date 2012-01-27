adb remount
adb push sqlite3 /system/xbin
adb push sqlite3 /system/bin
adb shell chmod 777 /system/bin/sqlite3
adb shell chmod 777 /system/xbin/sqlite3

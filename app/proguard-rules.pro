# ProGuard rules for HomeMess-Tiffin-Android
-keepattributes JavascriptInterface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keep class com.homemess.tiffinservice.bridge.** { *; }
-keep class com.homemess.tiffinservice.db.** { *; }
-keep class com.homemess.tiffinservice.model.** { *; }

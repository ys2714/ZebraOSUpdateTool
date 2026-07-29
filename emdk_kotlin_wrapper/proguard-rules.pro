# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# keep all classes of EMDK
-keep class com.symbol.** { *; }
# keep public interface public methods https://techdocs.zebra.com/emdk-for-android/13-0/api/reference/com/symbol/emdk/package-summary
-keep public interface com.symbol.emdk.EMDKManager$EMDKListener {
    public <methods>;
}
-keep public interface com.symbol.emdk.EMDKManager$StatusListener {
    public <methods>;
}
-keep public interface com.symbol.emdk.ProfileManager$DataListener {
    public <methods>;
}
# keep classes which implemented EMDK interfaces (avoid java.lang.AbstractMethodError)
-keep class * implements com.symbol.emdk.EMDKManager$EMDKListener
-keep class * implements com.symbol.emdk.EMDKManager$StatusListener
-keep class * implements com.symbol.emdk.ProfileManager$DataListener
# this is for java reflection call inside EMDK working correctly
-keepattributes Signature

# Tells R8 to not complain about missing classes from this package
-dontwarn com.symbol.emdk.**

# ---- consumer rules: the same EMDK keeps must also apply when R8 runs on the app ----
# keep all classes of EMDK
-keep class com.symbol.** { *; }
# keep public interface public methods
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

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

# Enable for export the R8 configuration
#-printconfiguration proguard_config.txt

# Disable obfuscate
-dontobfuscate

# Keep Gson data classes with annotations SerializedName
-keepclasseswithmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep inherited services.
-if interface * { @retrofit2.http.* <methods>; }
-keep interface * extends <1>

# Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
-keep,allowshrinking interface retrofit2.Call
-keep,allowshrinking class retrofit2.Response

# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowshrinking class kotlin.coroutines.Continuation

# Suppress warnings about missing class com.google.firebase.iid.FcmBroadcastProcessor
-dontwarn com.google.firebase.iid.FcmBroadcastProcessor
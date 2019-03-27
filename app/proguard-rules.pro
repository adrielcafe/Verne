# Verne
-keepclasseswithmembers class cafe.adriel.verne.presentation.model.** { *; }
-keepclasseswithmembers class cafe.adriel.verne.domain.model.** { *; }

# Kotlin
-dontwarn kotlin.**
-dontwarn org.jetbrains.annotations.**
-keepattributes EnclosingMethod
-keepattributes Signature
-keepattributes Annotation
-keep public class kotlin.reflect.jvm.internal.impl.builtins.* { public *; }
-keep public class kotlin.reflect.jvm.internal.impl.serialization.deserialization.builtins.* { public *; }
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** { volatile <fields>; }

# Android
-keepclassmembers class * extends androidx.lifecycle.ViewModel { <init>(...); }
-keep class com.android.vending.billing.**

# Firebase
-keep class io.grpc.** { *; }

# Crashlytics
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

# Prettytime
-keep class org.ocpsoft.prettytime.i18n.**

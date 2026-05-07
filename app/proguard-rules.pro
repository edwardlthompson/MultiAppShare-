# Project R8 / ProGuard rules (release minify). Library consumer rules are merged from dependencies.

# Play Console / crash deobfuscation–friendly stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Kotlin serialization (@Serializable models for backup JSON in :core-database / :core-domain)
-keepattributes Signature
-keepattributes *Annotation*, InnerClasses, EnclosingMethod

-keepclassmembers @kotlinx.serialization.Serializable class ** {
    *** Companion;
}
-keepclasseswithmembers @kotlinx.serialization.Serializable class * {
    kotlinx.serialization.KSerializer serializer(...);
}

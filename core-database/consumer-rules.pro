# Preserving database structures layout triggers down downstream
-keep class com.edwardlthompson.multiappshare.database.entity.** { *; }

# Actual mapped package entities layout triggers down downstream 
-keep class com.multiappshare.model.** { *; }
-keep class com.multiappshare.data.local.** { *; }

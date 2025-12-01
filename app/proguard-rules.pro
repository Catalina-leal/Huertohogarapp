# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep data classes
-keep class com.huertohogar.data.model.** { *; }

# Keep DTOs
-keep class com.huertohogar.data.api.** { *; }

# Keep Retrofit interfaces
-keep interface com.huertohogar.data.api.** { *; }

# Retrofit
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# Compose
-keep class androidx.compose.** { *; }
-keep class kotlin.coroutines.** { *; }

# Keep ViewModels
-keep class * extends androidx.lifecycle.ViewModel { *; }

# Keep Parcelable implementations
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Mantenemos todas las clases de presentación para evitar crashes
-keep class com.huertohogar.presentation.** { *; }

# Mantenemos las clases de repositorio
-keep class com.huertohogar.data.repository.** { *; }

# Mantenemos los ViewModels y sus factories
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}
-keep class *$Factory {
    <init>(...);
    <methods>;
}

# Mantenemos las clases de datos utilizadas por Room
-keep class com.huertohogar.data.model.** { *; }

# Mantenemos las clases de base de datos de Room
-keep class * extends androidx.room.RoomDatabase {
    <methods>;
}

# Mantenemos los métodos de acceso a datos (DAO)
-keep interface com.huertohogar.data.model.*Dao {
    <methods>;
}
-keep class * implements com.huertohogar.data.model.*Dao { *; }

# Mantenemos las clases de Factory de ViewModels
-keep class *$Factory {
    <init>(...);
    <methods>;
}

# Mantenemos las clases selladas (Sealed Classes)
-keep class * extends kotlin.Enum { *; }
-keepclassmembers class * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Mantenemos los métodos de extensión de Kotlin
-keepclassmembers class * {
    *** Companion;
}

# Mantenemos los métodos de reflección usados por Compose
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

# Mantenemos todas las funciones suspend
-keepclassmembers class * {
    suspend <methods>;
}

# Mantenemos las clases de utilidad
-keep class com.huertohogar.utils.** { *; }

# Mantenemos las clases de navegación
-keep class com.huertohogar.presentation.Screen { *; }

# Mantenemos las clases de estado selladas
-keep class com.huertohogar.presentation.*.*UiState { *; }

# Mantenemos las funciones suspend para Retrofit
-keepclassmembers class * {
    @retrofit2.http.* <methods>;
}

# Mantenemos las clases serializables
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Mantenemos las clases de reflección usadas por Compose
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault

# Mantenemos los constructores de data classes
-keepclassmembers class * {
    <init>(...);
}

# Mantenemos las clases usadas por Kotlin Coroutines
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-keep class kotlinx.coroutines.** { *; }

# Mantenemos las clases de Kotlin metadata
-keep class kotlin.Metadata { *; }
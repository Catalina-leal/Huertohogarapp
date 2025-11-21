# Guía de Build y Firma de APK

## 🔐 Generar Keystore

### Paso 1: Crear el Keystore

```bash
keytool -genkey -v -keystore keystore/release.keystore -alias huertohogar -keyalg RSA -keysize 2048 -validity 10000
```

**Información solicitada:**
- Contraseña del keystore: (guárdala de forma segura)
- Contraseña de la clave: (puede ser la misma)
- Nombre y apellidos: HuertoHogar
- Unidad organizativa: (opcional)
- Organización: HuertoHogar
- Ciudad: (tu ciudad)
- Estado/Provincia: (tu región)
- Código de país: CL

### Paso 2: Configurar Credenciales

**Opción A: Variables de Entorno**

```bash
export KEYSTORE_FILE=/ruta/completa/keystore/release.keystore
export KEYSTORE_PASSWORD=tu_password_aqui
export KEY_ALIAS=huertohogar
export KEY_PASSWORD=tu_password_aqui
```

**Opción B: Archivo local.properties**

Agregar al archivo `local.properties` (no versionado):

```properties
KEYSTORE_FILE=keystore/release.keystore
KEYSTORE_PASSWORD=tu_password_aqui
KEY_ALIAS=huertohogar
KEY_PASSWORD=tu_password_aqui
```

## 📦 Generar APK Firmado

### Build de Release

```bash
./gradlew assembleRelease
```

El APK estará en: `app/build/outputs/apk/release/app-release.apk`

### Build de Debug (sin firma)

```bash
./gradlew assembleDebug
```

## 📱 Generar AAB (Android App Bundle)

Para subir a Google Play Store, usa AAB:

```bash
./gradlew bundleRelease
```

El AAB estará en: `app/build/outputs/bundle/release/app-release.aab`

## ✅ Verificar Firma

```bash
jarsigner -verify -verbose -certs app-release.apk
```

O con apksigner (recomendado):

```bash
apksigner verify --verbose app-release.apk
```

## 🔍 Ver Información del APK

```bash
aapt dump badging app-release.apk
```

## 📊 Analizar Tamaño del APK

```bash
./gradlew analyzeReleaseBundle
```

## 🚀 Optimizaciones Incluidas

- **R8/ProGuard**: Ofuscación y minificación activadas
- **Shrink Resources**: Eliminación de recursos no usados
- **Packaging Options**: Exclusión de archivos innecesarios

## ⚠️ Importante

1. **NUNCA** subas el keystore al repositorio
2. **NUNCA** compartas las contraseñas
3. **Guarda** una copia segura del keystore (si lo pierdes, no podrás actualizar la app)
4. El keystore está en `.gitignore`

## 🔄 Actualizar App Existente

Para actualizar una app ya publicada:
- Debes usar el **mismo keystore**
- Incrementa `versionCode` en `build.gradle.kts`
- Actualiza `versionName` si es necesario

## 📝 Versionado

En `build.gradle.kts`:

```kotlin
defaultConfig {
    versionCode = 2  // Incrementar en cada release
    versionName = "1.1"  // Versión visible al usuario
}
```


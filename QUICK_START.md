# 🚀 Quick Start - Configuración Rápida

## 1️⃣ Generar Keystore (2 minutos)

```bash
./scripts/generate-keystore.sh
```

O manualmente:
```bash
keytool -genkey -v -keystore keystore/release.keystore \
  -alias huertohogar -keyalg RSA -keysize 2048 -validity 10000
```

## 2️⃣ Configurar Credenciales (1 minuto)

```bash
# Copiar template
cp local.properties.template local.properties

# Editar local.properties y agregar tus credenciales
nano local.properties  # o usa tu editor favorito
```

## 3️⃣ Ejecutar Tests (opcional)

```bash
# Si hay problemas con todos los tests, ejecuta individualmente:
./gradlew test --tests "HomeViewModelTest"
./gradlew test --tests "CartViewModelTest"
```

## 4️⃣ Generar APK (3 minutos)

```bash
./gradlew assembleRelease
```

El APK estará en: `app/build/outputs/apk/release/app-release.apk`

## ✅ Verificar

```bash
# Verificar firma del APK
apksigner verify --verbose app/build/outputs/apk/release/app-release.apk
```

## 🆘 Si algo falla

1. **Tests:** Ver `PASOS_COMPLETADOS.md` sección "Solución de Problemas"
2. **Keystore:** Ver `keystore/README.md`
3. **Build:** Ver `BUILD_AND_SIGN.md`


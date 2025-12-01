#!/bin/bash

# Script para firmar manualmente el APK si no se firmó automáticamente

echo "=== Firmando APK Manualmente ==="
echo ""

APK_UNSIGNED="app/build/outputs/apk/release/app-release-unsigned.apk"
APK_SIGNED="app/build/outputs/apk/release/app-release.apk"
KEYSTORE="keystore/EquiposHuertoHogar"
KEYSTORE_PASS="huertohogar2025"
KEY_ALIAS="huertohogar2025"
KEY_PASS="huertohogar2025"

# Verificar que el APK sin firmar existe
if [ ! -f "$APK_UNSIGNED" ]; then
    echo "ERROR: APK sin firmar no encontrado: $APK_UNSIGNED"
    echo "Genera el APK primero con: ./gradlew assembleRelease"
    exit 1
fi

# Verificar que el keystore existe
if [ ! -f "$KEYSTORE" ]; then
    echo "ERROR: Keystore no encontrado: $KEYSTORE"
    exit 1
fi

echo "1. Firmando APK con jarsigner..."
jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 \
  -keystore "$KEYSTORE" \
  -storepass "$KEYSTORE_PASS" \
  "$APK_UNSIGNED" \
  "$KEY_ALIAS"

if [ $? -ne 0 ]; then
    echo "ERROR: Fallo al firmar el APK"
    exit 1
fi

echo ""
echo "2. Verificando firma..."
jarsigner -verify -verbose -certs "$APK_UNSIGNED" | head -20

if [ $? -eq 0 ]; then
    echo ""
    echo "3. Renombrando APK firmado..."
    mv "$APK_UNSIGNED" "$APK_SIGNED"
    echo "✓ APK firmado: $APK_SIGNED"
    echo ""
    echo "=== Firma completada exitosamente ==="
    ls -lh "$APK_SIGNED"
else
    echo "ERROR: La verificación de firma falló"
    exit 1
fi


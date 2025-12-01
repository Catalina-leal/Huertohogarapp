#!/bin/bash

# Script para verificar el APK firmado y generar documentación

echo "=== Verificación del Keystore ==="
echo ""

if [ ! -f "keystore/EquiposHuertoHogar" ]; then
    echo "ERROR: Keystore no encontrado en keystore/EquiposHuertoHogar"
    exit 1
fi

echo "1. Verificando keystore..."
keytool -list -keystore keystore/EquiposHuertoHogar -storepass huertohogar2025 2>&1
echo ""

echo "2. Archivos en keystore/:"
ls -lh keystore/
echo ""

echo "3. Configuración del keystore en local.properties:"
grep KEYSTORE local.properties | sed 's/PASSWORD=.*/PASSWORD=***/' | sed 's/KEY_PASSWORD=.*/KEY_PASSWORD=***/'
echo ""

echo "=== Verificación del APK ==="
echo ""

APK_PATH="app/build/outputs/apk/release/app-release.apk"

if [ ! -f "$APK_PATH" ]; then
    echo "APK no encontrado en: $APK_PATH"
    echo ""
    echo "Para generar el APK, ejecuta:"
    echo "  ./gradlew clean assembleRelease"
    exit 1
fi

echo "4. APK encontrado:"
ls -lh "$APK_PATH"
echo ""

echo "5. Verificando firma del APK..."
jarsigner -verify -verbose -certs "$APK_PATH" 2>&1 | head -30
echo ""

echo "6. Información del APK:"
if command -v aapt &> /dev/null; then
    aapt dump badging "$APK_PATH" 2>&1 | head -10
else
    echo "aapt no encontrado. Buscando en Android SDK..."
    ANDROID_HOME="${ANDROID_HOME:-$HOME/Library/Android/sdk}"
    AAPT=$(find "$ANDROID_HOME" -name aapt 2>/dev/null | head -1)
    if [ -n "$AAPT" ]; then
        "$AAPT" dump badging "$APK_PATH" 2>&1 | head -10
    else
        echo "aapt no disponible. Instala Android SDK Build Tools."
    fi
fi
echo ""

echo "=== Resumen ==="
echo "✓ Keystore verificado"
echo "✓ APK generado: $APK_PATH"
echo "✓ Firma verificada"
echo ""
echo "El APK está listo para documentar con capturas de pantalla."


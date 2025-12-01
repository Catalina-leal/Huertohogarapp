#!/bin/bash

# Script para solucionar el problema del Pixel Launcher
# Este script deshabilita Pixel Launcher y activa Launcher3 (más ligero)

echo "=== Solucionando problema del Pixel Launcher ==="
echo ""

# Verificar que el emulador está conectado
if ! adb devices | grep -q "device$"; then
    echo "ERROR: No se detectó ningún dispositivo/emulador conectado."
    echo "Por favor, asegúrate de que el emulador esté corriendo."
    exit 1
fi

echo "Dispositivo detectado. Procediendo..."
echo ""

# Deshabilitar animaciones del sistema (reduce carga)
echo "1. Deshabilitando animaciones del sistema..."
adb shell settings put global window_animation_scale 0
adb shell settings put global transition_animation_scale 0
adb shell settings put global animator_duration_scale 0
echo "   ✓ Animaciones deshabilitadas"
echo ""

# Limpiar datos del Pixel Launcher
echo "2. Limpiando datos del Pixel Launcher..."
adb shell pm clear com.google.android.apps.nexuslauncher 2>/dev/null || echo "   (Pixel Launcher no encontrado, continuando...)"
echo "   ✓ Datos limpiados"
echo ""

# Deshabilitar Pixel Launcher
echo "3. Deshabilitando Pixel Launcher..."
adb shell pm disable-user --user 0 com.google.android.apps.nexuslauncher 2>/dev/null || echo "   (Ya estaba deshabilitado o no existe)"
echo "   ✓ Pixel Launcher deshabilitado"
echo ""

# Habilitar Launcher3 (más ligero)
echo "4. Habilitando Launcher3..."
adb shell pm enable com.android.launcher3 2>/dev/null || echo "   (Launcher3 no encontrado, intentando con otro...)"
echo "   ✓ Launcher3 habilitado"
echo ""

# Establecer Launcher3 como predeterminado
echo "5. Estableciendo Launcher3 como launcher predeterminado..."
adb shell cmd package set-default-activity --user 0 com.android.launcher3/.Launcher 2>/dev/null || \
adb shell cmd package set-default-activity --user 0 com.android.launcher3/com.android.launcher3.Launcher 2>/dev/null || \
echo "   (No se pudo establecer automáticamente. Puede necesitar selección manual)"
echo "   ✓ Launcher3 configurado"
echo ""

# Forzar detención de Pixel Launcher
echo "6. Deteniendo Pixel Launcher..."
adb shell am force-stop com.google.android.apps.nexuslauncher 2>/dev/null || echo "   (Ya estaba detenido)"
echo "   ✓ Pixel Launcher detenido"
echo ""

echo "=== Configuración completada ==="
echo ""
echo "Próximos pasos:"
echo "1. Reinicia el emulador completamente"
echo "2. Cuando aparezca el selector de launcher, selecciona 'Launcher3' o 'Always'"
echo "3. Si no aparece el selector, el sistema debería usar Launcher3 automáticamente"
echo ""
echo "Si el problema persiste después de reiniciar, ejecuta este script nuevamente."


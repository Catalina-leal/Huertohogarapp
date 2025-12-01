#!/bin/bash

# Script para generar keystore de release
# Uso: ./scripts/generate-keystore.sh

echo "Generador de Keystore para HuertoHogar"
echo "=========================================="
echo ""

# Crear directorio si no existe
mkdir -p keystore

# Solicitar información
read -p "Nombre del keystore [release.keystore]: " KEYSTORE_NAME
KEYSTORE_NAME=${KEYSTORE_NAME:-release.keystore}

read -p "Alias de la clave [huertohogar]: " KEY_ALIAS
KEY_ALIAS=${KEY_ALIAS:-huertohogar}

read -sp "Contraseña del keystore: " KEYSTORE_PASSWORD
echo ""

read -sp "Contraseña de la clave (puede ser la misma): " KEY_PASSWORD
echo ""

if [ -z "$KEY_PASSWORD" ]; then
    KEY_PASSWORD=$KEYSTORE_PASSWORD
fi

read -p "Validez en días [10000]: " VALIDITY
VALIDITY=${VALIDITY:-10000}

# Generar keystore
echo ""
echo "Generando keystore..."
keytool -genkey -v \
    -keystore "keystore/$KEYSTORE_NAME" \
    -alias "$KEY_ALIAS" \
    -keyalg RSA \
    -keysize 2048 \
    -validity "$VALIDITY" \
    -storepass "$KEYSTORE_PASSWORD" \
    -keypass "$KEY_PASSWORD" \
    -dname "CN=HuertoHogar, OU=Development, O=HuertoHogar, L=Santiago, ST=Region Metropolitana, C=CL"

if [ $? -eq 0 ]; then
    echo ""
    echo "Keystore generado exitosamente en: keystore/$KEYSTORE_NAME"
    echo ""
    echo "Configura las siguientes variables de entorno:"
    echo "export KEYSTORE_FILE=\$(pwd)/keystore/$KEYSTORE_NAME"
    echo "export KEYSTORE_PASSWORD=$KEYSTORE_PASSWORD"
    echo "export KEY_ALIAS=$KEY_ALIAS"
    echo "export KEY_PASSWORD=$KEY_PASSWORD"
    echo ""
    echo "O agrega al archivo local.properties:"
    echo "KEYSTORE_FILE=keystore/$KEYSTORE_NAME"
    echo "KEYSTORE_PASSWORD=$KEYSTORE_PASSWORD"
    echo "KEY_ALIAS=$KEY_ALIAS"
    echo "KEY_PASSWORD=$KEY_PASSWORD"
else
    echo ""
    echo "Error al generar el keystore"
    exit 1
fi

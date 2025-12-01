# Solución para Problemas de Autenticación con GitHub

## Problema

GitHub ya no acepta contraseñas para autenticación. Necesitas usar:
- **Personal Access Token (PAT)** con HTTPS, o
- **SSH keys** con SSH

---

## Solución 1: Usar Personal Access Token (PAT) - Más Rápido

### Paso 1: Crear un Personal Access Token en GitHub

1. Ve a GitHub: https://github.com/settings/tokens
2. Haz clic en **"Generate new token"** > **"Generate new token (classic)"**
3. Nombre del token: `HuertoHogar-Project` (o el que prefieras)
4. Selecciona los permisos (scopes):
   - ✅ `repo` (acceso completo a repositorios)
   - ✅ `workflow` (si usas GitHub Actions)
5. Haz clic en **"Generate token"**
6. **IMPORTANTE**: Copia el token inmediatamente (solo lo verás una vez)
   - Se verá como: `ghp_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx`

### Paso 2: Usar el Token para Autenticación

**Opción A: Usar el token directamente en la URL (temporal)**

```bash
# Cambiar el remoto a HTTPS con token
git remote set-url origin https://TU_TOKEN@github.com/Catalina-leal/Huertohogarapp.git

# Hacer push (ya no pedirá contraseña)
git push -u origin main
```

**Opción B: Usar Git Credential Manager (recomendado)**

```bash
# Al hacer push, cuando pida usuario y contraseña:
# Username: Rock14k (o tu usuario)
# Password: pega tu Personal Access Token (NO tu contraseña de GitHub)

git push -u origin main
```

**Opción C: Configurar credenciales en macOS Keychain**

```bash
# Configurar Git para guardar credenciales
git config --global credential.helper osxkeychain

# Al hacer push, ingresa:
# Username: Rock14k
# Password: tu_Personal_Access_Token
```

---

## Solución 2: Configurar SSH (Más Seguro a Largo Plazo)

### Paso 1: Verificar tu clave SSH pública

```bash
# Ver tu clave SSH pública (muestra la primera que encuentre)
cat ~/.ssh/id_ed25519.pub

# O si prefieres usar RSA:
cat ~/.ssh/id_rsa.pub
```

### Paso 2: Agregar la clave SSH a GitHub

1. Copia tu clave pública completa (debe empezar con `ssh-ed25519` o `ssh-rsa`)

2. Ve a GitHub: https://github.com/settings/ssh/new

3. **Title**: `MacBook Pro - HuertoHogar` (o el nombre que prefieras)

4. **Key**: Pega tu clave pública completa

5. Haz clic en **"Add SSH key"**

### Paso 3: Verificar conexión SSH

```bash
# Probar la conexión
ssh -T git@github.com

# Deberías ver: "Hi Rock14k! You've successfully authenticated..."
```

### Paso 4: El remoto ya está configurado para SSH

El remoto ya está configurado como:
```
git@github.com:Catalina-leal/Huertohogarapp.git
```

Ahora puedes hacer push:
```bash
git push -u origin main
```

---

## Solución 3: Usar GitHub CLI (gh) - Alternativa

```bash
# Instalar GitHub CLI (si no lo tienes)
brew install gh

# Autenticarse
gh auth login

# Seguir las instrucciones en pantalla
# Luego hacer push normalmente
git push -u origin main
```

---

## Recomendación

**Para esta vez (rápido)**: Usa un **Personal Access Token** con HTTPS

**Para el futuro (seguro)**: Configura **SSH keys** en GitHub

---

## Verificar que Funcionó

Después de configurar cualquiera de las opciones:

```bash
# Verificar remoto
git remote -v

# Intentar push
git push -u origin main
```

---

## Notas Importantes

- ⚠️ **Nunca compartas tu Personal Access Token** públicamente
- ⚠️ Si usas PAT, guárdalo de forma segura (usar un gestor de contraseñas)
- ✅ SSH es más seguro porque no necesitas tokens
- ✅ SSH es más conveniente una vez configurado (no pide contraseña)


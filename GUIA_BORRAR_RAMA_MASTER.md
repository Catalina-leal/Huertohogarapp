# Guía para Borrar la Rama 'master'

## ⚠️ IMPORTANTE - Antes de Borrar

1. **Verifica que 'main' tenga todo el contenido importante**
2. **NO borres 'master' si es la rama por defecto** (cambia la rama por defecto primero)
3. **Asegúrate de que 'main' tenga todos los commits importantes**

---

## Opción 1: Borrar desde GitHub (Recomendado - Más Seguro)

### Paso 1: Verificar y Cambiar la Rama por Defecto

1. Ve a tu repositorio: `https://github.com/Catalina-leal/Huertohogarapp`
2. Ve a **Settings** (Configuración) > **Branches**
3. En la sección **Default branch**, verás la rama actual
4. Si dice `master`, haz clic en el ícono de cambio (🔄) o en **"Switch to another branch"**
5. Selecciona `main` como la nueva rama por defecto
6. Confirma el cambio

### Paso 2: Borrar la Rama 'master'

1. Ve a la página principal del repositorio
2. Haz clic en el botón que muestra el número de branches (por ejemplo: "2 branches")
   - O ve directamente a: `https://github.com/Catalina-leal/Huertohogarapp/branches`
3. Encuentra la rama `master` en la lista
4. Haz clic en el ícono de **papelera** 🗑️ o el botón **"Delete branch"**
5. Confirma la eliminación

---

## Opción 2: Borrar desde la Línea de Comandos

### Si ya tienes el repositorio conectado localmente:

```bash
# 1. Primero, asegúrate de estar en la rama 'main'
git checkout main

# 2. Verificar que estás en la rama correcta
git branch

# 3. Borrar la rama local 'master' (si existe)
git branch -d master

# 4. Borrar la rama remota 'master'
git push origin --delete master
```

### Si no tienes repositorio local (como tu caso actual):

Primero necesitas conectarte al repositorio:

```bash
# Clonar el repositorio (si quieres trabajar localmente)
git clone git@github.com:Catalina-leal/Huertohogarapp.git
cd Huertohogarapp

# O si ya tienes el proyecto, conectarlo:
cd /Users/james14k/Desktop/huertohogar
git remote add origin git@github.com:Catalina-leal/Huertohogarapp.git
git fetch origin

# Luego borrar la rama remota
git push origin --delete master
```

---

## Verificación después de Borrar

1. Verifica que `main` sigue siendo la rama por defecto
2. Verifica que todos los commits importantes están en `main`
3. Confirma que `master` ya no aparece en la lista de branches

---

## ¿Qué hacer si 'master' tiene commits que no están en 'main'?

Si `master` tiene commits importantes que no están en `main`, primero fusiona:

```bash
# Cambiar a 'main'
git checkout main

# Fusionar 'master' en 'main'
git merge master

# Subir los cambios
git push origin main

# Ahora sí puedes borrar 'master' de forma segura
git push origin --delete master
```

---

## Nota Final

- **GitHub no te dejará borrar la rama por defecto directamente** - por eso debes cambiarla primero
- Si borras accidentalmente, puedes recuperar la rama desde el historial de Git (tiene 30 días de protección)
- Es una buena práctica mantener solo una rama principal (`main`)


# Guía para Inicializar Nuevo Repositorio Git

## Paso 1: Crear el repositorio en GitHub/GitLab

1. Crea un nuevo repositorio en tu plataforma Git (GitHub, GitLab, etc.)
2. **NO inicialices con README, .gitignore o licencia** (ya los tienes)
3. Anota la URL del repositorio (por ejemplo: `https://github.com/tu-usuario/tu-repo.git`)

## Paso 2: Inicializar repositorio local

Ejecuta estos comandos en orden:

```bash
# Inicializar repositorio
git init

# Configurar la rama por defecto como 'main' (estándar moderno)
git branch -M main

# Agregar todos los archivos (respetando .gitignore)
git add .

# Verificar qué archivos se van a subir (opcional)
git status

# Hacer el primer commit
git commit -m "Initial commit - Proyecto HuertoHogar"

# Agregar el remoto (reemplaza con tu URL)
git remote add origin <URL_DEL_NUEVO_REPOSITORIO>

# Verificar que el remoto se agregó correctamente
git remote -v

# Subir al repositorio
git push -u origin main
```

## Nota sobre la rama

- **`main`**: Estándar moderno (recomendado desde 2020)
- **`master`**: Nombre tradicional (ya no se usa por defecto)

El comando `git branch -M main` asegura que tu rama local se llame `main`, que es el estándar actual.

## Archivos que NO se subirán (protegidos por .gitignore)

- `local.properties` (claves API)
- `keystore/` (archivos de firma digital)
- `build/` (archivos compilados)
- `.idea/` (configuración de IDE)
- `.DS_Store` (archivos del sistema)

## Si el repositorio remoto ya tiene contenido

Si el repositorio remoto tiene una rama `main` o `master` con contenido, primero haz:

```bash
# Traer el contenido remoto
git pull origin main --allow-unrelated-histories

# Resolver conflictos si los hay, luego:
git push -u origin main
```

## Verificar después de subir

1. Ve a tu repositorio en GitHub/GitLab
2. Verifica que todos los archivos estén presentes
3. Verifica que `local.properties` y `keystore/` NO estén (deben estar protegidos)


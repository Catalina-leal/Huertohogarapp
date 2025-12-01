# HuertoHogar - Aplicación Móvil Android

**Aplicación móvil funcional e integrada para la venta de productos orgánicos, verduras frescas y lácteos en Chile**

## Integrantes del Proyecto

- **Hawk Durant**
- **Guerbens Cajuste**
- **Catalina Leal**

---

## 1. Nombre del Proyecto

**HuertoHogar**

Aplicación móvil Android desarrollada con Kotlin y Jetpack Compose que permite a los usuarios comprar productos orgánicos, verduras frescas y lácteos directamente desde sus dispositivos móviles. La aplicación integra front-end y back-end mediante microservicios Spring Boot, incluye una API externa de clima, y está completamente funcional con operaciones CRUD en tiempo real.

---

## 2. Funcionalidades Principales

### Funcionalidades de Usuario

1. **Catálogo de Productos**
   - Visualización de productos con imágenes
   - Filtrado por categorías (Frutas Frescas, Verduras Orgánicas, Productos Orgánicos, Productos Lácteos)
   - Búsqueda avanzada con debounce
   - Detalles completos de cada producto

2. **Carrito de Compras**
   - Agregar/eliminar productos
   - Modificar cantidades
   - Cálculo automático de totales
   - Persistencia local con Room Database

3. **Gestión de Pedidos**
   - Creación de pedidos
   - Seguimiento de estado en tiempo real
   - Historial de compras
   - Notificaciones de cambios de estado

4. **Sistema de Reseñas**
   - Calificación de productos (1-5 estrellas)
   - Comentarios de usuarios
   - Promedio de calificaciones
   - Visualización de reseñas

5. **Blog Educativo**
   - Artículos sobre alimentación saludable
   - Consejos de sostenibilidad
   - Categorización de contenido

6. **Gestión de Usuario**
   - Registro e inicio de sesión
   - Perfil de usuario
   - Programa de fidelización (puntos)
   - Historial de pedidos

7. **Sistema de Pagos**
   - Múltiples métodos de pago (Tarjeta, Transferencia, Contra entrega)
   - Validación de tarjetas
   - Procesamiento seguro

8. **Mapa de Tiendas**
   - Ubicaciones de tiendas físicas
   - Integración con Google Maps
   - Navegación a ubicaciones

9. **Información del Clima**
   - Widget de clima actual usando API externa (OpenWeatherMap)
   - Información de temperatura, humedad, presión y viento
   - Actualización en tiempo real

10. **Compartir en Redes Sociales**
    - Compartir productos
    - Compartir pedidos
    - Compartir la aplicación

11. **Notificaciones Push**
    - Actualizaciones de pedidos
    - Notificaciones de estado
    - Recordatorios

### Funcionalidades de Administrador

1. **Panel de Administración**
   - Dashboard con estadísticas de ventas
   - Gestión de productos (CRUD completo)
   - Gestión de usuarios
   - Gestión de pedidos y actualización de estados

---

## 3. Endpoints Utilizados

### Microservicio Spring Boot (Backend Propio)

**Base URL**: `http://localhost:8080/api/v1/` (desarrollo) o `https://api.huertohogar.cl/api/v1/` (producción)

#### Autenticación
- `POST /auth/register` - Registrar nuevo usuario
- `POST /auth/login` - Iniciar sesión

#### Productos
- `GET /products` - Obtener todos los productos
- `GET /products/{id}` - Obtener producto por ID
- `GET /products/category/{category}` - Obtener productos por categoría
- `GET /products/search?query={query}` - Buscar productos

#### Usuarios
- `GET /users/{email}` - Obtener usuario por email
- `GET /users` - Obtener todos los usuarios
- `PUT /users/{email}` - Actualizar usuario
- `DELETE /users/{email}` - Eliminar/desactivar usuario

#### Pedidos
- `POST /orders` - Crear nuevo pedido
- `GET /orders/user/{email}` - Obtener pedidos de un usuario
- `GET /orders/{orderId}` - Obtener pedido por ID
- `PUT /orders/{orderId}/status` - Actualizar estado del pedido

#### Reseñas
- `GET /reviews/product/{productId}` - Obtener reseñas de un producto
- `GET /reviews/product/{productId}/average` - Obtener promedio de calificaciones
- `POST /reviews` - Agregar reseña

#### Blog
- `GET /blog/posts` - Obtener todos los posts
- `GET /blog/posts/{id}` - Obtener post por ID
- `GET /blog/posts/category/{category}` - Obtener posts por categoría

#### Pagos
- `POST /payments/process` - Procesar pago

### API Externa: OpenWeatherMap

**Base URL**: `https://api.openweathermap.org/data/2.5/`

Esta API externa se utiliza para mostrar información del clima que puede afectar a los productos agrícolas, sin interferir con los datos locales ni los microservicios propios.

#### Endpoints Utilizados
- `GET /weather?q={ciudad}&appid={API_KEY}&units=metric&lang=es` - Obtener clima actual
- `GET /forecast?q={ciudad}&appid={API_KEY}&units=metric&lang=es&cnt=5` - Obtener pronóstico del clima

**Configuración**:
1. Obtener API Key gratuita en: https://openweathermap.org/api
2. Configurar en `app/src/main/java/com/huertohogar/data/api/ExternalApiClient.kt`:
   ```kotlin
   const val WEATHER_API_KEY = "TU_API_KEY_AQUI"
   ```

**Integración**:
- Se consume vía Retrofit en `ExternalApiClient.kt`
- Se muestra en el widget de clima en `HomeScreen`
- No interfiere con los datos locales ni microservicios
- Manejo de errores cuando la API no está disponible

---

## 4. Pasos para Ejecutar

### Requisitos Previos

- **Android Studio** Hedgehog (2023.1.1) o superior
- **JDK 11** o superior
- **Android SDK** con API Level 24 (Android 7.0) mínimo
- **Gradle** 8.13 (incluido en el proyecto)
- **Maven** 3.6+ (para el backend)

### Paso 1: Clonar el Repositorio

```bash
git clone <url-del-repositorio>
cd huertohogar
```

### Paso 2: Configurar el Proyecto Android

1. **Abrir en Android Studio**
   - File → Open → Seleccionar carpeta del proyecto
   - Android Studio sincronizará Gradle automáticamente

2. **Sincronizar dependencias**
   ```bash
   ./gradlew build --refresh-dependencies
   ```

### Paso 3: Configurar Google Maps (Opcional)

1. Obtener API Key en [Google Cloud Console](https://console.cloud.google.com/)
2. Editar `app/src/main/AndroidManifest.xml`:
   ```xml
   <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="TU_API_KEY_AQUI" />
   ```

### Paso 4: Configurar API Externa de Clima (Opcional)

1. Obtener API Key en [OpenWeatherMap](https://openweathermap.org/api)
2. Editar `app/src/main/java/com/huertohogar/data/api/ExternalApiClient.kt`:
   ```kotlin
   const val WEATHER_API_KEY = "TU_API_KEY_AQUI"
   ```

### Paso 5: Ejecutar el Backend (Microservicios)

```bash
cd backend
mvn spring-boot:run
```

El backend estará disponible en: `http://localhost:8080/api/v1`

**Swagger UI**: `http://localhost:8080/api/v1/swagger-ui.html`

### Paso 6: Configurar Conexión con Backend

1. **Para Android Emulator**:
   - Editar `app/src/main/java/com/huertohogar/data/api/ApiClient.kt`
   - Ya está configurado: `http://10.0.2.2:8080/api/v1/`

2. **Para Dispositivo Físico**:
   - Obtener IP local:
     ```bash
     # Mac/Linux
     ifconfig
     
     # Windows
     ipconfig
     ```
   - Editar `app/src/main/java/com/huertohogar/data/api/ApiClient.kt`:
     ```kotlin
     private const val BASE_URL = "http://TU_IP_LOCAL:8080/api/v1/"
     ```
   - Asegurarse de que el dispositivo y la computadora estén en la misma red WiFi

3. **Activar integración con backend**:
   - En `app/src/main/java/com/huertohogar/data/api/ApiClient.kt`:
     ```kotlin
     fun isApiAvailable(): Boolean {
         return true
     }
     ```

### Paso 7: Ejecutar la Aplicación Móvil

```bash
# Ejecutar en dispositivo/emulador
./gradlew installDebug
```

O desde Android Studio:
- Conectar dispositivo Android o iniciar emulador
- Click en "Run" o presionar `Shift + F10`

### Paso 8: Ejecutar Pruebas Unitarias

```bash
# Todos los tests unitarios
./gradlew test

# Tests específicos
./gradlew test --tests "HomeViewModelTest"
./gradlew test --tests "CartViewModelTest"
./gradlew test --tests "WeatherRepositoryTest"

# Con cobertura
./gradlew test jacocoTestReport
```

La cobertura de pruebas está por encima del 80% en módulos ViewModel y Repository.

---

## 5. Generación del APK Firmado

### Paso 1: Generar el Keystore (.jks)

Ejecutamos el script automatizado:

```bash
chmod +x scripts/generate-keystore.sh
./scripts/generate-keystore.sh
```

O manualmente usando keytool:

```bash
keytool -genkey -v \
    -keystore keystore/huertohogar-release.jks \
    -alias huertohogar \
    -keyalg RSA \
    -keysize 2048 \
    -validity 10000 \
    -storepass TU_CONTRASEÑA \
    -keypass TU_CONTRASEÑA \
    -dname "CN=HuertoHogar, OU=Development, O=HuertoHogar, L=Santiago, ST=Region Metropolitana, C=CL"
```

**Información del Keystore**:
- **Ubicación**: `keystore/huertohogar-release.jks`
- **Alias**: `huertohogar`
- **Algoritmo**: RSA 2048 bits
- **Validez**: 10000 días (aproximadamente 27 años)

### Paso 2: Configurar Credenciales en local.properties

Creamos o editamos el archivo `local.properties` en la raíz del proyecto:

```properties
KEYSTORE_FILE=keystore/huertohogar-release.jks
KEYSTORE_PASSWORD=tu_contraseña_aquí
KEY_ALIAS=huertohogar
KEY_PASSWORD=tu_contraseña_aquí
```

**IMPORTANTE**: El archivo `local.properties` NO debe subirse al repositorio (ya está en .gitignore).

### Paso 3: Generar el APK Firmado

```bash
./gradlew assembleRelease
```

El APK firmado se generará en:
```
app/build/outputs/apk/release/app-release.apk
```

### Paso 4: Verificar la Firma

```bash
apksigner verify --verbose app/build/outputs/apk/release/app-release.apk
```

O usando jarsigner:

```bash
jarsigner -verify -verbose -certs app/build/outputs/apk/release/app-release.apk
```

### Paso 5: Capturas del Keystore y APK

Para documentar el proceso, debemos tomar capturas de:

1. **Estructura del keystore**:
   ```bash
   ls -lh keystore/
   ```

2. **Información del keystore**:
   ```bash
   keytool -list -v -keystore keystore/huertohogar-release.jks
   ```

3. **APK firmado generado**:
   ```bash
   ls -lh app/build/outputs/apk/release/app-release.apk
   ```

4. **Verificación de la firma**:
   ```bash
   apksigner verify --print-certs app/build/outputs/apk/release/app-release.apk
   ```

**Nota**: Estas capturas deben incluirse en la documentación del proyecto. El archivo `.jks` debe mantenerse seguro y NO subirse al repositorio.

---

## 6. Arquitectura y Tecnologías

### Arquitectura

- **MVVM (Model-View-ViewModel)**: Separación clara de responsabilidades
- **Clean Architecture**: Capas bien definidas (Presentation, Domain, Data)
- **Repository Pattern**: Abstracción de fuentes de datos
- **Dependency Injection**: ViewModels con Factory pattern

### Tecnologías Principales

- **Kotlin**: Lenguaje de programación
- **Jetpack Compose**: Framework de UI declarativa
- **Material 3**: Sistema de diseño moderno
- **Room Database**: Persistencia local
- **Retrofit**: Cliente HTTP para APIs
- **Coroutines & Flow**: Programación asíncrona reactiva
- **Spring Boot**: Backend microservicios
- **JUnit 5, Kotest, MockK**: Testing

### Integración Front-end y Back-end

La aplicación móvil se integra con los microservicios Spring Boot mediante:

1. **Retrofit Client**: Configurado en `ApiClient.kt`
2. **Operaciones CRUD en tiempo real**: 
   - Crear pedidos (`POST /orders`)
   - Leer productos (`GET /products`)
   - Actualizar pedidos (`PUT /orders/{orderId}/status`)
   - Eliminar usuarios (`DELETE /users/{email}`)

3. **Sincronización automática**: 
   - Los cambios se reflejan inmediatamente en la UI
   - StateFlow emite actualizaciones reactivamente

4. **Manejo de errores**: 
   - Fallback a datos locales si el backend no está disponible
   - Manejo de errores de red con mensajes informativos

---

## 7. Pruebas Unitarias

### Cobertura de Pruebas

Las pruebas unitarias cubren al menos el **80% de la lógica** en los siguientes módulos:

- **ViewModels**: CartViewModel, HomeViewModel, ProductsViewModel, WeatherViewModel, AuthViewModel
- **Repositories**: HomeRepository, WeatherRepository

### Frameworks Utilizados

- **JUnit 5**: Framework de testing
- **Kotest**: Assertions más legibles
- **MockK**: Mocking para Kotlin (mejor que Mockito para coroutines)
- **Turbine**: Testing de Flows de forma elegante

### Ejecutar Pruebas

```bash
# Todas las pruebas
./gradlew test

# Pruebas específicas
./gradlew test --tests "CartViewModelTest"
./gradlew test --tests "WeatherRepositoryTest"

# Con reporte de cobertura
./gradlew test jacocoTestReport
```

Los reportes de cobertura se generan en: `app/build/reports/jacoco/test/html/index.html`

---

## 8. Control de Versiones

### Git y GitHub

- Repositorio Git con control de versiones
- Branching strategy para desarrollo colaborativo
- Commits descriptivos y organizados
- Pull Requests para revisión de código

### Herramientas de Planificación

- Issues para tracking de tareas
- Milestones para hitos del proyecto
- Projects para organización del trabajo en equipo

---

## 9. Diseño e Interfaz Visual

La aplicación cuenta con:

- **Diseño moderno Material 3**
- **Todas las pantallas implementadas**:
  - Home
  - Productos
  - Detalle de Producto
  - Carrito
  - Checkout
  - Pago
  - Perfil
  - Historial de Pedidos
  - Login/Registro
  - Mapa de Tiendas
  - Blog
  - Panel de Administración (Productos, Usuarios, Ventas, Dashboard)

- **Formularios completos y funcionales**
- **Sin errores de navegación, ejecución ni validación visual**
- **Animaciones suaves y transiciones**

---

## 10. Estructura del Proyecto

```
huertohogar/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/huertohogar/
│   │   │   │   ├── data/
│   │   │   │   │   ├── api/              # Cliente Retrofit (API propia y externa)
│   │   │   │   │   ├── db/               # Room Database
│   │   │   │   │   ├── model/            # Entidades de datos
│   │   │   │   │   └── repository/       # Repositorios
│   │   │   │   ├── presentation/
│   │   │   │   │   ├── home/             # Pantalla principal
│   │   │   │   │   ├── products/         # Catálogo
│   │   │   │   │   ├── cart/             # Carrito
│   │   │   │   │   ├── checkout/         # Checkout
│   │   │   │   │   ├── profile/          # Perfil
│   │   │   │   │   ├── login/            # Autenticación
│   │   │   │   │   ├── map/              # Mapa de tiendas
│   │   │   │   │   ├── payment/          # Pagos
│   │   │   │   │   ├── weather/          # Clima (API externa)
│   │   │   │   │   ├── admin/            # Panel administrador
│   │   │   │   │   └── components/       # Componentes reutilizables
│   │   │   │   └── utils/                # Utilidades
│   │   │   └── res/                      # Recursos
│   │   ├── test/                         # Tests unitarios
│   │   └── androidTest/                  # Tests de UI
│   └── build.gradle.kts
├── backend/                              # Microservicios Spring Boot
│   ├── src/main/java/com/huertohogar/
│   │   ├── controller/                   # REST Controllers
│   │   ├── service/                      # Lógica de negocio
│   │   ├── repository/                   # JPA Repositories
│   │   └── model/                        # Entidades y DTOs
│   └── pom.xml
├── keystore/                             # Keystore para firma (NO versionado)
│   └── huertohogar-release.jks
├── scripts/                              # Scripts de utilidad
│   └── generate-keystore.sh
├── README.md                             # Este archivo
├── KEYSETUP.md                           # Guía de configuración del keystore
└── local.properties                      # Configuración local (NO versionado)
```

---

## 11. Iniciar y Probar el Backend

### Iniciar el Backend

1. **Navegar al directorio del backend**:
   ```bash
   cd backend
   ```

2. **Iniciar el backend**:
   ```bash
   mvn spring-boot:run
   ```

3. **Verificar que está funcionando**:
   - Abrir en el navegador: `http://localhost:8080/api/v1/swagger-ui.html`
   - Deberías ver la interfaz de Swagger con todos los endpoints

### Verificar Conexión entre App y Backend

1. **Asegurar que el backend está corriendo**:
   ```bash
   curl http://localhost:8080/api/v1/products
   ```

2. **Configurar la URL en la app Android**:
   - Para emulador: Ya está configurado en `ApiClient.kt` con `http://10.0.2.2:8080/api/v1/`
   - Para dispositivo físico: Obtener tu IP local y actualizar `ApiClient.kt`:
     ```bash
     # Mac/Linux
     ifconfig | grep "inet " | grep -v 127.0.0.1
     
     # Windows
     ipconfig
     ```
     Luego cambiar `BASE_URL` en `app/src/main/java/com/huertohogar/data/api/ApiClient.kt` a: `http://TU_IP:8080/api/v1/`

3. **Activar la integración**:
   En `ApiClient.kt`, verificar que:
   ```kotlin
   fun isApiAvailable(): Boolean {
       return true
   }
   ```

4. **Probar desde la app**:
   - Registrar un usuario nuevo
   - Ver productos en la pantalla de productos
   - Verificar en los logs del backend que se recibieron las peticiones

### Probar los Microservicios

**Probar con Swagger UI**:
1. Abrir: `http://localhost:8080/api/v1/swagger-ui.html`
2. Explorar los endpoints disponibles
3. Probar cada endpoint directamente desde la interfaz

**Probar con curl**:
```bash
# Obtener productos
curl http://localhost:8080/api/v1/products

# Registrar usuario
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email": "test@test.com", "password": "password123", "fullName": "Test User"}'
```

### Ver Logs

**Backend**: Los logs aparecen en la terminal donde ejecutaste `mvn spring-boot:run`

**Android**: 
```bash
# Ver logs de la app
adb logcat | grep "HuertoHogar\|ApiClient\|Retrofit"
```

### Solución de Problemas

- **El backend no inicia**: Verificar que el puerto 8080 esté libre
- **La app no se conecta**: Verificar la URL en `ApiClient.kt` y que el backend esté corriendo
- **Error CORS**: El backend ya tiene CORS configurado, pero verificar `SecurityConfig.java` si persiste

**Para más detalles, ver**:
- `GUIA_BACKEND.md`: Guía completa del backend
- `INICIAR_BACKEND.md`: Guía rápida de inicio
- `SOLUCION_PROBLEMAS_APK.md`: Solución de problemas del APK

---

## 12. Documentación Adicional

- **KEYSETUP.md**: Guía detallada para generar y configurar el keystore
- **TESTING.md**: Guía completa de testing
- **BUILD_AND_SIGN.md**: Proceso completo de build y firma
- **GUIA_BACKEND.md**: Guía completa para iniciar, configurar y probar el backend
- **INICIAR_BACKEND.md**: Guía rápida para iniciar el backend
- **SOLUCION_PROBLEMAS_APK.md**: Guía para solucionar problemas del APK
- **backend/README.md**: Documentación del backend
- **backend/INTEGRACION_ANDROID.md**: Guía de integración Android-Backend

---

## 12. Notas Importantes

1. **Keystore**: El archivo `.jks` es crítico para actualizar la aplicación en Google Play. Mantener una copia de seguridad segura.

2. **API Keys**: Las API keys (Google Maps, OpenWeatherMap) no deben subirse al repositorio. Usar variables de entorno o BuildConfig en producción.

3. **Backend**: El backend debe estar ejecutándose para que la aplicación móvil pueda realizar operaciones CRUD completas.

4. **Pruebas**: Se recomienda ejecutar las pruebas antes de cada commit para mantener la calidad del código.

5. **Comentarios**: Todos los comentarios están escritos en primera persona del plural (ejemplo: "Obtenemos", "Manejamos", "Creamos") y sin emojis.

---

## 13. Contacto

Para preguntas o soporte sobre el proyecto:

- **Equipo**: Hawk Durant, Guerbens Cajuste, Catalina Leal
- **Email**: huertohogar.info@gmail.com

---

**Proyecto desarrollado con Kotlin, Jetpack Compose y Spring Boot**

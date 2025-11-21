# 🌱 HuertoHogar - Aplicación Móvil Android

<div align="center">

![HuertoHogar Logo](https://img.shields.io/badge/HuertoHogar-Organic%20Store-2E8B57?style=for-the-badge)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpack-compose&logoColor=white)

**Tienda online de productos orgánicos, verduras y lácteos en Chile**

[Características](#-características) • [Tecnologías](#-tecnologías-y-herramientas) • [Instalación](#-instalación) • [Uso](#-uso) • [Arquitectura](#-arquitectura) • [Testing](#-testing) • [Build](#-build-y-distribución)

</div>

---

## 📋 Tabla de Contenidos

- [Descripción](#-descripción)
- [Características](#-características)
- [Tecnologías y Herramientas](#-tecnologías-y-herramientas)
- [Arquitectura](#-arquitectura)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Uso](#-uso)
- [Animaciones](#-animaciones)
- [Testing](#-testing)
- [Build y Distribución](#-build-y-distribución)
- [Backend API](#-backend-api)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Contribución](#-contribución)
- [Licencia](#-licencia)

---

## 🎯 Descripción

**HuertoHogar** es una aplicación móvil Android desarrollada con **Kotlin** y **Jetpack Compose** que permite a los usuarios comprar productos orgánicos, verduras frescas y lácteos directamente desde sus dispositivos móviles. La aplicación ofrece una experiencia de compra completa con funcionalidades como catálogo de productos, carrito de compras, procesamiento de pedidos, sistema de reseñas, blog educativo y más.

### Objetivo del Proyecto

Proporcionar una plataforma móvil moderna y eficiente para la venta de productos orgánicos, conectando a los consumidores con productos frescos del campo, con un enfoque en la sostenibilidad y la calidad.

---

## ✨ Características

### Funcionalidades Principales

- 🛒 **Catálogo de Productos**
  - Visualización de productos con imágenes
  - Filtrado por categorías (Frutas, Verduras, Orgánicos, Lácteos)
  - Búsqueda avanzada
  - Detalles completos de cada producto

- 🛍️ **Carrito de Compras**
  - Agregar/eliminar productos
  - Modificar cantidades
  - Cálculo automático de totales
  - Persistencia local con Room

- 📦 **Gestión de Pedidos**
  - Creación de pedidos
  - Seguimiento de estado
  - Historial de compras
  - Notificaciones de estado

- ⭐ **Sistema de Reseñas**
  - Calificación de productos (1-5 estrellas)
  - Comentarios de usuarios
  - Promedio de calificaciones
  - Visualización de reseñas

- 📝 **Blog Educativo**
  - Artículos sobre alimentación saludable
  - Consejos de sostenibilidad
  - Categorización de contenido

- 👤 **Gestión de Usuario**
  - Registro e inicio de sesión
  - Perfil de usuario
  - Programa de fidelización (puntos)
  - Historial de pedidos

- 💳 **Sistema de Pagos**
  - Múltiples métodos de pago
  - Validación de tarjetas
  - Procesamiento seguro

- 🗺️ **Mapa de Tiendas**
  - Ubicaciones de tiendas físicas
  - Integración con Google Maps
  - Navegación a ubicaciones

- 📱 **Compartir en Redes Sociales**
  - Compartir productos
  - Compartir pedidos
  - Compartir la aplicación

- 🔔 **Notificaciones Push**
  - Actualizaciones de pedidos
  - Notificaciones de estado
  - Recordatorios

---

## 🛠️ Tecnologías y Herramientas

### Lenguaje y Framework Principal

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Kotlin** | 1.9+ | Lenguaje de programación principal |
| **Jetpack Compose** | BOM 2024.02.00 | Framework de UI declarativa |
| **Material 3** | Latest | Sistema de diseño moderno |

### Arquitectura y Patrones

| Componente | Versión | ¿Por qué se usa? |
|------------|---------|------------------|
| **MVVM (Model-View-ViewModel)** | - | Separación de responsabilidades, testabilidad, mantenibilidad |
| **Android Architecture Components** | Latest | Componentes oficiales de Google para arquitectura robusta |
| **ViewModel** | 2.7.0 | Gestión del estado de la UI y datos |
| **LiveData/StateFlow** | Latest | Observables reactivos para actualización automática de UI |

### Persistencia de Datos

| Tecnología | Versión | ¿Por qué se usa? |
|------------|---------|------------------|
| **Room Database** | 2.8.3 | Base de datos local SQLite con abstracción, type-safe, fácil de usar |
| **DataStore Preferences** | 1.0.0 | Almacenamiento de preferencias moderno, reemplazo de SharedPreferences |
| **Kotlin Coroutines** | 1.7.3 | Operaciones asíncronas, mejor que callbacks |

### Navegación

| Tecnología | Versión | ¿Por qué se usa? |
|------------|---------|------------------|
| **Navigation Compose** | 2.7.5 | Navegación declarativa, type-safe, integración nativa con Compose |
| **Animaciones de Transición** | Built-in | Transiciones suaves entre pantallas |

### Networking y API

| Tecnología | Versión | ¿Por qué se usa? |
|------------|---------|------------------|
| **Retrofit** | 2.9.0 | Cliente HTTP type-safe, fácil de usar, extensible |
| **Gson** | 2.9.0 | Serialización/deserialización JSON |
| **OkHttp** | 4.12.0 | Cliente HTTP eficiente, logging, interceptors |
| **Coroutines** | 1.7.3 | Llamadas asíncronas sin callbacks |

### UI y Diseño

| Tecnología | Versión | ¿Por qué se usa? |
|------------|---------|------------------|
| **Material 3** | Latest | Sistema de diseño moderno, Material You, theming avanzado |
| **Compose Animation** | Built-in | Animaciones declarativas, performantes, fáciles de implementar |
| **Coil** (implícito) | - | Carga eficiente de imágenes (si se implementa) |

### Mapas y Ubicación

| Tecnología | Versión | ¿Por qué se usa? |
|------------|---------|------------------|
| **Google Maps for Compose** | 4.3.0 | Integración nativa con Compose, fácil de usar |
| **Play Services Maps** | 18.2.0 | SDK oficial de Google Maps |
| **Play Services Location** | 21.0.1 | Servicios de ubicación precisos |

### Notificaciones

| Tecnología | Versión | ¿Por qué se usa? |
|------------|---------|------------------|
| **WorkManager** | 2.9.0 | Tareas en background confiables, incluso después de reiniciar |
| **NotificationManager** | Built-in | Sistema nativo de notificaciones de Android |

### Testing

| Tecnología | Versión | ¿Por qué se usa? |
|------------|---------|------------------|
| **JUnit 5** | 5.10.0 | Framework de testing moderno, más expresivo que JUnit 4 |
| **Kotest** | 5.8.0 | Assertions más legibles, property-based testing |
| **MockK** | 1.13.8 | Mocking para Kotlin, mejor que Mockito para coroutines |
| **Compose UI Test** | Latest | Testing de componentes Compose, interacciones |
| **Turbine** | 1.0.0 | Testing de Flows de forma elegante |
| **Coroutines Test** | 1.7.3 | Testing de código asíncrono con control de tiempo |

### Build y Distribución

| Tecnología | Versión | ¿Por qué se usa? |
|------------|---------|------------------|
| **Gradle** | 8.13 | Sistema de build moderno, incremental, paralelo |
| **KSP (Kotlin Symbol Processing)** | Latest | Procesamiento de anotaciones más rápido que KAPT |
| **ProGuard/R8** | Built-in | Minificación, ofuscación, optimización de código |

---

## 🏗️ Arquitectura

### Patrón MVVM (Model-View-ViewModel)

```
┌─────────────────────────────────────────────────────────┐
│                      UI Layer                            │
│  (Compose Screens, Components)                          │
│  - HomeScreen, ProductsScreen, CartScreen, etc.          │
└────────────────────┬────────────────────────────────────┘
                     │ Observa
                     ▼
┌─────────────────────────────────────────────────────────┐
│                   ViewModel Layer                        │
│  - HomeViewModel, CartViewModel, AuthViewModel, etc.    │
│  - Expone StateFlow/LiveData                             │
│  - Maneja lógica de presentación                         │
└────────────────────┬────────────────────────────────────┘
                     │ Usa
                     ▼
┌─────────────────────────────────────────────────────────┐
│                    Repository Layer                      │
│  - HomeRepository, UserRepository, OrderRepository      │
│  - Abstrae fuente de datos (API o Local)                 │
│  - Implementa lógica de negocio                         │
└────────────────────┬────────────────────────────────────┘
                     │
         ┌───────────┴───────────┐
         ▼                       ▼
┌─────────────────┐    ┌──────────────────┐
│   Data Sources  │    │   Local Storage   │
│   - Retrofit    │    │   - Room          │
│   - API Client  │    │   - DataStore     │
└─────────────────┘    └──────────────────┘
```

### Flujo de Datos

1. **UI** observa `StateFlow` del `ViewModel`
2. **ViewModel** expone estado y maneja eventos
3. **Repository** obtiene datos de API o base de datos local
4. **Room/API** proporciona datos persistentes
5. **Cambios** se propagan reactivamente a la UI

### Principios Aplicados

- **Single Responsibility**: Cada clase tiene una responsabilidad única
- **Dependency Injection**: ViewModels reciben dependencias vía Factory
- **Separation of Concerns**: UI, lógica y datos separados
- **Reactive Programming**: StateFlow para actualizaciones automáticas
- **Testability**: Capas desacopladas, fácil de testear

---

## 📦 Instalación

### Requisitos Previos

- **Android Studio** Hedgehog (2023.1.1) o superior
- **JDK 11** o superior
- **Android SDK** con API Level 24 (Android 7.0) mínimo
- **Gradle** 8.13 (incluido en el proyecto)
- **Kotlin** 1.9+ (incluido en el proyecto)

### Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/tu-usuario/huertohogar.git
   cd huertohogar
   ```

2. **Abrir en Android Studio**
   - File → Open → Seleccionar carpeta del proyecto
   - Android Studio sincronizará Gradle automáticamente

3. **Sincronizar dependencias**
   ```bash
   ./gradlew build --refresh-dependencies
   ```

4. **Configurar SDK**
   - File → Project Structure → SDK Location
   - Verificar que Android SDK esté configurado

5. **Ejecutar la aplicación**
   - Conectar dispositivo Android o iniciar emulador
   - Click en "Run" o `Shift + F10`

---

## ⚙️ Configuración

### 1. Google Maps API Key

1. Obtener API Key en [Google Cloud Console](https://console.cloud.google.com/)
2. Habilitar "Maps SDK for Android"
3. Editar `app/src/main/AndroidManifest.xml`:
   ```xml
   <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="TU_API_KEY_AQUI" />
   ```

### 2. Backend API (Opcional)

Si tienes el backend Spring Boot ejecutándose:

1. Editar `app/src/main/java/com/huertohogar/data/api/ApiClient.kt`:
   ```kotlin
   private const val BASE_URL = "http://TU_IP:8080/api/v1/"
   ```

2. Activar API:
   ```kotlin
   fun isApiAvailable(): Boolean = true
   ```

### 3. Keystore para Firma (Producción)

Ver sección [Build y Distribución](#-build-y-distribución)

---

## 🚀 Uso

### Ejecutar en Desarrollo

```bash
# Ejecutar en dispositivo/emulador
./gradlew installDebug

# O desde Android Studio
# Click en "Run" (▶️)
```

### Navegación en la App

1. **Pantalla Principal (Home)**
   - Carousel de productos destacados
   - Sección "Acerca de"
   - Productos populares
   - Testimonios

2. **Catálogo de Productos**
   - Filtros por categoría
   - Búsqueda de productos
   - Vista de grid

3. **Carrito de Compras**
   - Ver productos agregados
   - Modificar cantidades
   - Proceder al checkout

4. **Checkout**
   - Ingresar dirección de envío
   - Seleccionar método de pago
   - Confirmar pedido

5. **Perfil de Usuario**
   - Ver información personal
   - Historial de pedidos
   - Puntos de fidelización
   - Cerrar sesión

---

## 🎬 Animaciones

### Tipos de Animaciones Implementadas

#### 1. **Animaciones de Navegación**

**Ubicación**: `MainScreen.kt`

```kotlin
// Transiciones entre pantallas
enterTransition = fadeIn(animationSpec = tween(300)) + 
                 slideInHorizontally(animationSpec = tween(300))
exitTransition = fadeOut(animationSpec = tween(300)) + 
                slideOutHorizontally(animationSpec = tween(300))
```

**¿Por qué se usan?**
- **Fade (Desvanecimiento)**: Transición suave, no distrae
- **Slide Horizontal**: Indica dirección de navegación, familiar para usuarios
- **Duración 300ms**: Balance entre suavidad y velocidad

#### 2. **Animaciones de Tarjetas (AboutScreen)**

**Ubicación**: `AboutScreen.kt`

```kotlin
// Animación de aparición escalonada
val animatedAlpha by animateFloatAsState(
    targetValue = if (isVisible) 1f else 0f,
    animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
)
```

**Características**:
- **Alpha Animation**: Fade in/out de elementos
- **Staggered Animation**: Elementos aparecen uno tras otro
- **Easing FastOutSlowIn**: Acelera al inicio, desacelera al final (natural)

**¿Por qué se usan?**
- Mejora la percepción de carga
- Guía la atención del usuario
- Experiencia más pulida y profesional

#### 3. **Animaciones de Estado**

**Ubicación**: Varios ViewModels

```kotlin
// Transición de estados (Loading → Success/Error)
Crossfade(targetState = uiState) { state ->
    when (state) {
        is Loading -> LoadingScreen()
        is Success -> ContentScreen()
        is Error -> ErrorScreen()
    }
}
```

**¿Por qué se usa?**
- **Crossfade**: Transición suave entre estados diferentes
- Evita parpadeos bruscos
- Mejor UX durante cambios de estado

#### 4. **Animaciones de Interacción**

**Ubicación**: Componentes UI

- **Ripple Effect**: Click en botones (Material 3 built-in)
- **Elevation Changes**: Tarjetas al presionar
- **Icon Animations**: Rotación, escala en iconos

**¿Por qué se usan?**
- Feedback visual inmediato
- Confirmación de interacción
- Estándar Material Design

### Configuración de Animaciones

Las animaciones están configuradas con:
- **Duración**: 300-600ms (óptimo para percepción humana)
- **Easing**: FastOutSlowInEasing (curva natural)
- **Compose Animation API**: Declarativa, performante, fácil de mantener

---

## 🧪 Testing

### Estrategia de Testing

Implementamos una **estrategia híbrida** que combina:

1. **Pruebas Unitarias** (70%)
   - ViewModels
   - Repositories
   - Utilidades
   - Lógica de negocio

2. **Pruebas de UI** (20%)
   - Componentes Compose
   - Interacciones
   - Navegación

3. **Pruebas de Integración** (10%)
   - Flujos completos
   - Integración con Room
   - Integración con API

### Ejecutar Tests

```bash
# Todos los tests unitarios
./gradlew test

# Tests específicos
./gradlew test --tests "HomeViewModelTest"

# Tests de UI (requiere dispositivo/emulador)
./gradlew connectedAndroidTest

# Con cobertura
./gradlew test jacocoTestReport
```

### Cobertura Objetivo

- **ViewModels**: >80%
- **Repositories**: >70%
- **UI Components**: >60%
- **Overall**: >75%

Ver `TESTING.md` para documentación completa.

---

## 📱 Build y Distribución

### Build de Debug

```bash
./gradlew assembleDebug
```

APK en: `app/build/outputs/apk/debug/app-debug.apk`

### Build de Release (Firmado)

#### 1. Generar Keystore

```bash
./scripts/generate-keystore.sh
```

O manualmente:
```bash
keytool -genkey -v -keystore keystore/release.keystore \
  -alias huertohogar -keyalg RSA -keysize 2048 -validity 10000
```

#### 2. Configurar Credenciales

**Opción A: Variables de Entorno**
```bash
export KEYSTORE_FILE=$(pwd)/keystore/release.keystore
export KEYSTORE_PASSWORD=tu_password
export KEY_ALIAS=huertohogar
export KEY_PASSWORD=tu_password
```

**Opción B: local.properties**
```properties
KEYSTORE_FILE=keystore/release.keystore
KEYSTORE_PASSWORD=tu_password
KEY_ALIAS=huertohogar
KEY_PASSWORD=tu_password
```

#### 3. Generar APK Firmado

```bash
./gradlew assembleRelease
```

APK en: `app/build/outputs/apk/release/app-release.apk`

#### 4. Generar AAB (Google Play)

```bash
./gradlew bundleRelease
```

AAB en: `app/build/outputs/bundle/release/app-release.aab`

#### 5. Verificar Firma

```bash
apksigner verify --verbose app-release.apk
```

### Optimizaciones Aplicadas

- ✅ **R8/ProGuard**: Minificación y ofuscación
- ✅ **Shrink Resources**: Eliminación de recursos no usados
- ✅ **Code Shrinking**: Eliminación de código muerto
- ✅ **Resource Optimization**: Optimización de recursos

Ver `BUILD_AND_SIGN.md` para detalles completos.

---

## 🔌 Backend API

### Microservicio Spring Boot

El proyecto incluye un backend REST completo desarrollado con Spring Boot.

**Ubicación**: `backend/`

**Características**:
- Arquitectura en capas (Controller → Service → Repository)
- CRUD completo para todas las entidades
- Autenticación JWT
- Documentación Swagger/OpenAPI
- Base de datos H2 (desarrollo) / PostgreSQL (producción)

### Ejecutar Backend

```bash
cd backend
mvn spring-boot:run
```

**API disponible en**: `http://localhost:8080/api/v1`

**Swagger UI**: `http://localhost:8080/api/v1/swagger-ui.html`

### Integración con Android

1. Configurar URL en `ApiClient.kt`
2. Activar `isApiAvailable() = true`
3. La app usará automáticamente el backend cuando esté disponible

Ver `backend/README.md` y `backend/INTEGRACION_ANDROID.md` para más detalles.

---

## 📁 Estructura del Proyecto

```
huertohogar/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/huertohogar/
│   │   │   │   ├── data/
│   │   │   │   │   ├── api/          # Cliente Retrofit, DTOs
│   │   │   │   │   ├── db/           # Room Database
│   │   │   │   │   ├── model/        # Entidades de datos
│   │   │   │   │   └── repository/   # Repositorios
│   │   │   │   ├── presentation/
│   │   │   │   │   ├── home/         # Pantalla principal
│   │   │   │   │   ├── products/     # Catálogo
│   │   │   │   │   ├── cart/         # Carrito
│   │   │   │   │   ├── checkout/     # Checkout
│   │   │   │   │   ├── profile/      # Perfil
│   │   │   │   │   ├── login/        # Autenticación
│   │   │   │   │   ├── map/          # Mapa de tiendas
│   │   │   │   │   ├── payment/      # Pagos
│   │   │   │   │   └── components/   # Componentes reutilizables
│   │   │   │   ├── utils/            # Utilidades
│   │   │   │   └── ui/theme/         # Tema Material 3
│   │   │   └── res/                  # Recursos (drawables, etc.)
│   │   ├── test/                     # Tests unitarios
│   │   └── androidTest/              # Tests de UI
│   └── build.gradle.kts
├── backend/                           # Backend Spring Boot
│   ├── src/main/java/com/huertohogar/
│   │   ├── controller/               # REST Controllers
│   │   ├── service/                   # Lógica de negocio
│   │   ├── repository/                # JPA Repositories
│   │   ├── model/                     # Entidades y DTOs
│   │   └── config/                    # Configuraciones
│   └── pom.xml
├── scripts/                           # Scripts de utilidad
├── keystore/                          # Keystore (no versionado)
├── README.md                          # Este archivo
├── TESTING.md                         # Guía de testing
├── BUILD_AND_SIGN.md                  # Guía de build
└── gradle.properties
```

---

## 🎨 Diseño y Temas

### Paleta de Colores (Material 3)

```kotlin
// Colores principales
- Verde Esmeralda (#2E8B57): Botones, enlaces, elementos interactivos
- Amarillo Mostaza (#FFD700): Ofertas, promociones
- Marrón Claro (#8B4513): Títulos, subtítulos
- Blanco Suave (#F7F7F7): Fondo principal
- Gris Oscuro (#333333): Texto principal
- Gris Medio (#666666): Texto secundario
```

### Tipografía

- **Montserrat**: Texto general (legible, moderna)
- **Playfair Display**: Headers (elegante, distintiva)

### Principios de Diseño

- **Material 3**: Sistema de diseño moderno de Google
- **Responsive**: Adaptable a diferentes tamaños de pantalla
- **Accesible**: Cumple con estándares de accesibilidad
- **Consistente**: Componentes reutilizables

---

## 🔐 Seguridad

### Implementaciones de Seguridad

1. **Almacenamiento de Credenciales**
   - Contraseñas hasheadas con BCrypt (backend)
   - DataStore para preferencias sensibles
   - Keystore para firma de APK

2. **Comunicación**
   - HTTPS para comunicación con API
   - Validación de certificados
   - Interceptors de seguridad

3. **Validaciones**
   - Validación de email
   - Validación de contraseñas
   - Sanitización de inputs

4. **ProGuard/R8**
   - Ofuscación de código
   - Protección contra reverse engineering

---

## 📊 Rendimiento

### Optimizaciones Implementadas

1. **Lazy Loading**
   - `LazyColumn` y `LazyVerticalGrid` para listas grandes
   - Carga diferida de imágenes

2. **Caché**
   - Room para caché local
   - Reducción de llamadas a API

3. **Coroutines**
   - Operaciones asíncronas no bloqueantes
   - Mejor uso de recursos

4. **Minificación**
   - R8 para optimización de código
   - Reducción de tamaño del APK

---

## 🐛 Troubleshooting

### Problemas Comunes

**1. Error de compilación con Room**
```bash
./gradlew clean build
```

**2. Tests no se ejecutan**
- Verificar que estén en `app/src/test/`
- Ejecutar `./gradlew clean test`

**3. Google Maps no funciona**
- Verificar API Key en AndroidManifest
- Verificar permisos de ubicación

**4. Error de firma de APK**
- Verificar que keystore exista
- Verificar credenciales en variables de entorno

**5. Backend no conecta**
- Verificar que backend esté ejecutándose
- Verificar URL en `ApiClient.kt`
- Verificar CORS en backend

---

## 🤝 Contribución

### Cómo Contribuir

1. Fork el proyecto
2. Crea una rama (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

### Estándares de Código

- Seguir [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Usar nombres descriptivos
- Comentar código complejo
- Escribir tests para nuevas funcionalidades

---

## 📄 Licencia

Este proyecto es parte de HuertoHogar. Todos los derechos reservados.

---

## 👥 Autores

- **Equipo de Desarrollo HuertoHogar**

---

## 🙏 Agradecimientos

- Google por Jetpack Compose y Material 3
- Comunidad de Kotlin
- Todos los contribuidores de las librerías open source utilizadas

---

## 📞 Contacto

Para preguntas o soporte:
- Email: huertohogar.info@gmail.com


---

<div align="center">

**Hecho con ❤️ usando Kotlin y Jetpack Compose**

⭐ Si te gusta este proyecto, dale una estrella!

</div>

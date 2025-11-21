# Configuración de Funcionalidades Avanzadas

Este documento explica cómo configurar las funcionalidades avanzadas de la aplicación HuertoHogar.

## 1. Google Maps API

### Pasos para configurar Google Maps:

1. **Obtener una API Key de Google Maps:**
   - Ve a [Google Cloud Console](https://console.cloud.google.com/)
   - Crea un nuevo proyecto o selecciona uno existente
   - Habilita la API de Maps SDK for Android
   - Crea credenciales (API Key)
   - Restringe la API Key a tu aplicación Android (package name: `com.huertohogar`)

2. **Configurar la API Key en la aplicación:**
   - Abre `app/src/main/AndroidManifest.xml`
   - Reemplaza `YOUR_GOOGLE_MAPS_API_KEY` con tu API Key real:
   ```xml
   <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="TU_API_KEY_AQUI" />
   ```

3. **Verificar permisos:**
   - Los permisos de ubicación ya están configurados en el AndroidManifest
   - La aplicación solicitará permisos en tiempo de ejecución

## 2. Notificaciones Push

### Configuración de notificaciones:

1. **Canal de notificaciones:**
   - Ya está creado automáticamente en `MainActivity.onCreate()`
   - El canal se llama "Pedidos HuertoHogar"

2. **Permisos:**
   - El permiso `POST_NOTIFICATIONS` ya está en el AndroidManifest
   - Se solicita automáticamente en Android 13+

3. **WorkManager para notificaciones periódicas:**
   - El `OrderNotificationWorker` está configurado para verificar cambios de estado
   - Para activarlo, programa un trabajo periódico:
   ```kotlin
   val workRequest = PeriodicWorkRequestBuilder<OrderNotificationWorker>(
       15, TimeUnit.MINUTES
   ).build()
   WorkManager.getInstance(context).enqueue(workRequest)
   ```

## 3. Integración con API Backend

### Configuración de la API:

1. **Configurar URL del backend:**
   - Abre `app/src/main/java/com/huertohogar/data/api/ApiClient.kt`
   - Reemplaza `BASE_URL` con la URL real de tu backend:
   ```kotlin
   private const val BASE_URL = "https://api.huertohogar.cl/api/v1/"
   ```

2. **Estructura de la API:**
   - La aplicación espera los siguientes endpoints:
     - `GET /products` - Lista de productos
     - `POST /auth/register` - Registro de usuario
     - `POST /auth/login` - Login
     - `POST /orders` - Crear pedido
     - `GET /orders/user/{email}` - Pedidos del usuario
     - `POST /payments/process` - Procesar pago
     - `GET /reviews/product/{productId}` - Reseñas de producto
     - `POST /reviews` - Agregar reseña
     - `GET /blog/posts` - Posts del blog

3. **Activar uso de API:**
   - En `ApiClient.kt`, cambia `isApiAvailable()` para retornar `true` cuando el backend esté listo
   - Mientras tanto, la app funciona con datos locales (Room)

## 4. Sistema de Pagos

### Métodos de pago implementados:

1. **Tarjeta de Crédito/Débito:**
   - Validación básica de formato
   - En producción, integrar con pasarela de pagos real (Stripe, PayPal, etc.)

2. **Transferencia Bancaria:**
   - Muestra datos bancarios para transferencia
   - El usuario debe confirmar la transferencia manualmente

3. **Pago Contra Entrega:**
   - Opción para pagar en efectivo al recibir el pedido

### Integración con pasarela de pagos:

Para integrar con una pasarela real (ej: Stripe, PayPal):

1. Agregar dependencias en `build.gradle.kts`:
   ```kotlin
   implementation("com.stripe:stripe-android:20.0.0")
   ```

2. Modificar `PaymentViewModel.processPayment()` para usar la SDK de la pasarela

3. Actualizar `PaymentScreen` con los campos específicos requeridos

## 5. Compartir en Redes Sociales

### Funcionalidad implementada:

- **Compartir Producto:** Desde `ProductDetailScreen` (FAB)
- **Compartir Pedido:** Desde `OrderHistoryScreen` (botón en cada pedido)
- **Compartir App:** Desde `AppBar` (icono de compartir)

### Personalización:

Los textos de compartir están en `ShareHelper.kt` y pueden personalizarse según necesidades.

## Notas Importantes

1. **Google Maps API Key:**
   - ⚠️ **IMPORTANTE:** No subas tu API Key a repositorios públicos
   - Usa variables de entorno o un archivo `local.properties` (ya está en .gitignore)
   - Restringe la API Key en Google Cloud Console

2. **Backend API:**
   - La aplicación funciona completamente offline con Room
   - Cuando el backend esté listo, solo cambia `isApiAvailable()` a `true`
   - Los datos locales se sincronizarán automáticamente

3. **Notificaciones:**
   - Las notificaciones funcionan localmente
   - Para notificaciones push remotas, integrar Firebase Cloud Messaging (FCM)

4. **Pagos:**
   - El sistema actual es una simulación
   - Para producción, integrar con pasarela de pagos certificada
   - Nunca almacenar datos de tarjetas en la app

## Próximos Pasos Recomendados

1. Configurar Firebase para notificaciones push remotas
2. Integrar pasarela de pagos real (Stripe, PayPal, etc.)
3. Implementar sincronización bidireccional con backend
4. Agregar autenticación con tokens JWT
5. Implementar caché inteligente para productos

